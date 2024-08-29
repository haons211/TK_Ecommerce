package com.example.ecommerce.shopbase.service.payment;

import com.example.ecommerce.shopbase.api.thirdparty.payment.VNPay.VNPayPaymentRequest;
import com.example.ecommerce.shopbase.config.vnpay.VNPayConfig;
import com.example.ecommerce.shopbase.dto.CartItemDetails;
import com.example.ecommerce.shopbase.entity.*;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.*;
import com.example.ecommerce.shopbase.service.order.OrderService;
import com.example.ecommerce.shopbase.service.payment.constant.VNPayParams;
import com.example.ecommerce.shopbase.service.payment.constant.VnpIpnResponseConst;
import com.example.ecommerce.shopbase.service.redis.BaseRedisService;
import com.example.ecommerce.shopbase.utils.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VNPayService {

      int DEFAULT_MULTIPLIER = 100;
      String TIME_ZONE = "Asia/Ho_Chi_Minh";
      String VERSION   = "2.1.0";
      String COMMAND   = "pay";
      String ORDER_TYPE = "190000";



    SecurityUtils securityUtils;
        OrderService orderService;
        BaseRedisService redisService;
        CartItemRepository cartItemRepository;
        ValueRepository valueRepository;
        OrderItemRepository orderItemRepository;
        ValueCombinationRepository valueCombinationRepository;
       ProductRepository productRepository;
       SellerRepository sellerRepository;
       OrderRepository orderRepository;
        PaymentRepository paymentRepository;

    public String createPaymentUrl1(HttpServletRequest request, @RequestBody VNPayPaymentRequest paymentRequest,String orderIdd) throws JsonProcessingException {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String  vnp_CreatedDate= formatter.format(cld.getTime());



        //save on database *******************************
        // save Payment
        Payment payment = Payment.builder()
                .amount(paymentRequest.getVnp_amount().intValue())
                .payment_time(new Timestamp(System.currentTimeMillis()))
                .status(Payment.Status.CREATING)
                .provider(null)
                .transaction_id(null)
                .bank_code(null)
                .build();
        paymentRepository.save(payment);

        // save order
        Integer shippingFee= paymentRequest.getVnp_amount().intValue()-orderService.getRealPrice(orderService.getPreOrderDetail(orderIdd));
        LocalDateTime localDateTime1 = LocalDateTime.now();
        DateTimeFormatter formatter_0 = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate_0 = localDateTime1.format(formatter_0);
        String order_code = "OD" + formattedDate_0 + generateRandomString(8);
        User user = securityUtils.getCurrentUserLogin();
        Order order = Order.builder()
                .order_code(order_code)
                .user(user)
                .total(paymentRequest.getVnp_amount().intValue())
                .payment(payment)
                .coupon(null)
                .shippingfee(shippingFee)
                .created_at(new Timestamp(System.currentTimeMillis()))
                .completed_at(null)
                .canceled_at(null)
                .delivery_at(null)
                .status(Order.Status.CREATING)
                .build();
        orderRepository.save(order);
        // save order item
        List<CartItemDetails> items= orderService.getPreOrderDetail(orderIdd);
        for(CartItemDetails item : items) {
            LocalDateTime localDateTime = order.getCreated_at().toLocalDateTime();
            DateTimeFormatter formatter_1 = DateTimeFormatter.ofPattern("yyMMdd");
            String formattedDate = localDateTime.format(formatter_1);
            String ord_item_code = "OTC" + formattedDate + generateRandomString(8);

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(()-> new AppException(ErrorCode.SELLER_NOT_EXISTED));
            CartItem cartItem = cartItemRepository.findById(item.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.LOILO));
            Seller seller = product.getSeller();
            OrderItem orderItem = OrderItem.builder()
                    .ord_item_status(OrderItem.Status.CREATING)
                    .ord_item_code(ord_item_code)
                    .ord_item_quantity(item.getQuantity())
                    .order(order)
                    .cartItem(cartItem)
                    .price(item.getPrice())
                    .seller(seller)
                    .build();
            orderItemRepository.save(orderItem);
        }
        //Get Ip adress
        String vnp_IpAddr = request.getHeader("X-Forwarded-For");
        if(vnp_IpAddr == null || vnp_IpAddr.isEmpty()){
            vnp_IpAddr = request.getRemoteAddr();
        }
        String  userId_raw= String.valueOf(securityUtils.getCurrentUserLogin().getId());
        //Config vallues
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String secretKey = VNPayConfig.vnp_HashSecret;
        String vnpUrl = VNPayConfig.vnp_PayUrl;
        String returnUrl= baseUrl+"/api/v1/payments2" + VNPayConfig.vnp_Returnurl;
        String orderId = VNPayConfig.getRandomNumber(8) + new SimpleDateFormat("ddHHmmss").format(cld.getTime());
        String amount= String.valueOf(paymentRequest.getVnp_amount()*100);
        String bankCode=paymentRequest.getVnp_bank_code();
        String locale = paymentRequest.getVnp_locale();
        if (locale == null || locale.isEmpty()) {
            locale = "vn";
        }
        String currCode="VND";
        // litmit time for transaction
        cld.add(Calendar.MINUTE,2);
        String vnp_ExpireDate= formatter.format(cld.getTime());

        //Setup vnp Params

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Locale", locale);
        vnp_Params.put("vnp_CurrCode", currCode);
        vnp_Params.put("vnp_TxnRef", orderIdd+":"+VNPayConfig.getRandomNumber(5));
        vnp_Params.put("vnp_OrderInfo", "Thanh toan cho ma GD:" + orderId);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Amount", amount);
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreatedDate);
        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_ExpireDate",vnp_ExpireDate);
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;

    }
    public URLResponse createPaymentUrl(HttpServletRequest request, @RequestBody VNPayPaymentRequest paymentRequest,String orderIdd) throws JsonProcessingException {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String  vnp_CreatedDate= formatter.format(cld.getTime());

        //Get Ip adress
        String vnp_IpAddr = request.getHeader("X-Forwarded-For");
        if(vnp_IpAddr == null || vnp_IpAddr.isEmpty()){
            vnp_IpAddr = request.getRemoteAddr();
        }
        redisService.delete(orderIdd);
        redisService.delete("backup"+orderIdd);
        String  userId_raw= String.valueOf(securityUtils.getCurrentUserLogin().getId());
        Integer shippingFee= paymentRequest.getVnp_amount().intValue()-orderService.getRealPrice(orderService.getPreOrderDetail(orderIdd));
        String shippingFee_raw= String.valueOf(shippingFee);
        //save on database *******************************
        // save Payment
        Payment payment = Payment.builder()
                .amount(paymentRequest.getVnp_amount().intValue())
                .payment_time(new Timestamp(System.currentTimeMillis()))
                .status(Payment.Status.CREATING)
                .provider(null)
                .transaction_id(null)
                .bank_code(null)
                .build();
        paymentRepository.save(payment);

        // save order
        LocalDateTime localDateTime1 = LocalDateTime.now();
        DateTimeFormatter formatter_0 = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate_0 = localDateTime1.format(formatter_0);
        String order_code = "OD" + formattedDate_0 + generateRandomString(8);
        User user = securityUtils.getCurrentUserLogin();
        Order order = Order.builder()
                .order_code(order_code)
                .user(user)
                .total(paymentRequest.getVnp_amount().intValue())
                .payment(payment)
                .coupon(null)
                .shippingfee(shippingFee)
                .created_at(new Timestamp(System.currentTimeMillis()))
                .completed_at(null)
                .canceled_at(null)
                .delivery_at(null)
                .status(Order.Status.CREATING)
                .build();
        orderRepository.save(order);

        // save order item
        List<CartItemDetails> items= orderService.getPreOrderDetail(orderIdd);
        for(CartItemDetails item : items) {
            LocalDateTime localDateTime = order.getCreated_at().toLocalDateTime();
            DateTimeFormatter formatter_1 = DateTimeFormatter.ofPattern("yyMMdd");
            String formattedDate = localDateTime.format(formatter_1);
            String ord_item_code = "OTC" + formattedDate + generateRandomString(8);
            CartItem cartItem = cartItemRepository.findById(item.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.SELLER_NOT_EXISTED));
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(()-> new AppException(ErrorCode.SELLER_NOT_EXISTED));
            Seller seller = product.getSeller();
            OrderItem orderItem = OrderItem.builder()
                    .ord_item_status(OrderItem.Status.CREATING)
                    .ord_item_code(ord_item_code)
                    .ord_item_quantity(item.getQuantity())
                    .order(order)
                    .cartItem(cartItem)
                    .price(item.getPrice())
                    .seller(seller)
                    .build();
            orderItemRepository.save(orderItem);
        }




        String returnUrlFormat = " http://localhost:8080/api/v1/payments/vnpay-payment-return";
        String returnUrl = buildReturnUrl(order_code,returnUrlFormat);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String secretKey = VNPayConfig.vnp_HashSecret;
        String vnpUrl = VNPayConfig.vnp_PayUrl;
        /*String returnUrl= baseUrl+"/api/v1/payments" + VNPayConfig.vnp_Returnurl;*/
        String orderId = order_code;
        String amount= String.valueOf(paymentRequest.getVnp_amount() * DEFAULT_MULTIPLIER);
        String bankCode=paymentRequest.getVnp_bank_code();
        String locale = paymentRequest.getVnp_locale();
        if (locale == null || locale.isEmpty()) {
            locale = "vn";
        }
        String currCode="VND";
        cld.add(Calendar.MINUTE,5);
        String vnp_ExpireDate= formatter.format(cld.getTime());

        //Setup vnp Params

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put(VNPayParams.VERSION, VERSION);
        vnp_Params.put(VNPayParams.COMMAND, COMMAND);
        vnp_Params.put(VNPayParams.TMN_CODE, vnp_TmnCode);
        vnp_Params.put(VNPayParams.LOCALE, locale);
        vnp_Params.put(VNPayParams.CURRENCY, currCode);
        /*vnp_Params.put(VNPayParams.TXN_REF, orderId + ":" + userId_raw + ":" + shippingFee_raw + ":" + VNPayConfig.getRandomNumber(5));*/
        vnp_Params.put(VNPayParams.TXN_REF,order_code);
        vnp_Params.put(VNPayParams.ORDER_INFO, "Thanh toan cho ma GD:" + orderId);
        vnp_Params.put(VNPayParams.ORDER_TYPE, ORDER_TYPE);
        vnp_Params.put(VNPayParams.AMOUNT, amount);
        vnp_Params.put(VNPayParams.RETURN_URL, "http://localhost:8080/api/v1/payments/vnpay-payment-return");
        vnp_Params.put(VNPayParams.IP_ADDRESS, vnp_IpAddr);
        vnp_Params.put(VNPayParams.CREATED_DATE, vnp_CreatedDate);
        vnp_Params.put(VNPayParams.EXPIRE_DATE, vnp_ExpireDate);

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put(VNPayParams.BANK_CODE, bankCode);
        }

        URLResponse response1 = new URLResponse();
         response1.setUrl(buildInitPaymentUrl(vnp_Params));
         response1.setOrder_code(order_code);
      return response1;
    }

    public String buildInitPaymentUrl(Map<String,String> vnp_Params ){

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&"+VNPayParams.SECURE_HASH+"=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
    }


    public IpnResponse handlePaymentNotification(HttpServletRequest request) {
        if (!verifyIpn(request)) {
            return VnpIpnResponseConst.SIGNATURE_FAILED;
        }
        IpnResponse response = VnpIpnResponseConst.SUCCESS;
        String order_code = request.getParameter(VNPayParams.TXN_REF);
        Order order = orderRepository.findByOrderCode(order_code);
        order.setStatus(Order.Status.SUCCESSFUL);
        order.setCompleted_at(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);
        Payment payment = order.getPayment();
        payment.setPayment_time(new Timestamp(System.currentTimeMillis()));
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getID());
        for(OrderItem item : orderItems){
            item.setOrd_item_status(OrderItem.Status.SUCCESSFUL);
            orderItemRepository.save(item);
        }
        return response;
    }
    public boolean verifyIpn(HttpServletRequest request) {
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VNPayConfig.hashAllFields(fields);
        return signValue.equals(vnp_SecureHash);
    }


    /*public Payment buildPayment(HttpServletRequest request, boolean isSuccess) {
        String amountStr = request.getParameter("vnp_Amount");
        String paymentTimeStr = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String bankCode = request.getParameter("vnp_BankCode");
        String transactionRef = request.getParameter("vnp_TxnRef");

        int amount = Integer.parseInt(amountStr);
        Timestamp paymentTime = convertVnpPayDateToTimestamp(paymentTimeStr);

        return Payment.builder()
                .amount(amount)
                .payment_time(paymentTime)
                .status(isSuccess ? Payment.Status.SUCCESS : Payment.Status.FAIL)
                .provider("VNPAY")
                .transaction_id(transactionId)
                .bank_code(bankCode)
                .build();
    }*/

    /*public Order buildOrder(HttpServletRequest request, Payment payment, boolean isSuccess,User user,Integer shippingFee) throws JsonProcessingException {
        String amountStr = request.getParameter("vnp_Amount");
        String paymentTimeStr = request.getParameter("vnp_PayDate");
        int totalAmount = Integer.parseInt(amountStr);
        Timestamp paymentTime = convertVnpPayDateToTimestamp(paymentTimeStr);
        return Order.builder()
                .user(user)
                .total(totalAmount)
                .payment(payment)
                .coupon(null)
                .shipping_fee(shippingFee)
                .completed_at(isSuccess ? paymentTime : null)
                .canceled_at(isSuccess ? null : paymentTime)
                .delivery_at(null)
                .status(isSuccess ? Order.Status.CREATED : Order.Status.REJECT)
                .build();
    }*/

  /*  public void processOrderItems(String key, Order order,String status) throws JsonProcessingException {
        List<CartItemDetails> listItemsFromOrder = orderService.getPreOrderDetail(key);
        for (CartItemDetails item : listItemsFromOrder) {
            LocalDateTime localDateTime = order.getCreated_at().toLocalDateTime();
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
            Seller seller = product.getSeller();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
            String formattedDate = localDateTime.format(formatter);
         String ord_item_code= "OTC"+ formattedDate + generateRandomString(8) ;

            CartItem cartItem = cartItemRepository.findById(item.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

            OrderItem orderItem = OrderItem.builder()
                    .ord_item_status(status)
                    .ord_item_code(ord_item_code)
                    .ord_item_quantity(item.getQuantity())
                    .order(order)
                    .cartItem(cartItem)
                    .price(item.getPrice()*item.getQuantity())
                    .seller(seller)
                    .build();

            orderItemRepository.save(orderItem);
        }
    }*/

    /*private ValueCombination fetchValueCombination(CartItemDetails item, Value value1) {
        if (item.getValueCombinationDetails().getValue2_id() == null) {
            return valueCombinationRepository.findFirstByValue1(value1)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        } else {
            Value value2 = valueRepository.findById(item.getValueCombinationDetails().getValue2_id())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
            return valueCombinationRepository.findFirstByValue1AndValue2(value1, value2)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        }
    }*/


    private Timestamp convertVnpPayDateToTimestamp(String payDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // Parse the date string
        LocalDateTime localDateTime = LocalDateTime.parse(payDateStr, formatter);
        // Convert LocalDateTime to Timestamp
        return Timestamp.valueOf(localDateTime);
    }
    // Get all cart items from order
    public List<CartItemDetails> getAllCartItemFromOrder(String key) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Object, Object> entries = redisService.getField(key);
        List<CartItemDetails> result = new ArrayList<>();

        for (Object value : entries.values()) {
            CartItemDetails cartItemDetails = objectMapper.convertValue(value, CartItemDetails.class);
            result.add(cartItemDetails);
        }

        return result;
    }
    public String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
    private String buildReturnUrl(String txnRef,String returnUrlFormat) {
        return String.format(returnUrlFormat, txnRef);
    }


}
