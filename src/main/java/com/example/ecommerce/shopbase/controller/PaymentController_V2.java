package com.example.ecommerce.shopbase.controller;

import com.example.ecommerce.shopbase.api.thirdparty.payment.VNPay.VNPayPaymentRequest;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.entity.Order;
import com.example.ecommerce.shopbase.entity.OrderItem;
import com.example.ecommerce.shopbase.entity.Payment;
import com.example.ecommerce.shopbase.entity.User;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.OrderRepository;
import com.example.ecommerce.shopbase.repository.PaymentRepository;
import com.example.ecommerce.shopbase.repository.UserRepository;
import com.example.ecommerce.shopbase.service.order.OrderService;
import com.example.ecommerce.shopbase.service.payment.IpnResponse;
import com.example.ecommerce.shopbase.service.payment.URLResponse;
import com.example.ecommerce.shopbase.service.payment.VNPayService;
import com.example.ecommerce.shopbase.service.redis.BaseRedisService;
import com.example.ecommerce.shopbase.utils.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController_V2 {

    VNPayService vnPayService;
    UserRepository userRepository;
    PaymentRepository paymentRepository;
    OrderService orderService;
    OrderRepository orderRepository;
    BaseRedisService redisService;
    SecurityUtils  securityUtils;

    @PostMapping("/create_payment_url")
    public ApiResponse<URLResponse> createPaymentUrl(HttpServletRequest request, @RequestBody VNPayPaymentRequest paymentRequest, @RequestParam("orderId") String orderId) throws JsonProcessingException {
        URLResponse response = vnPayService.createPaymentUrl(request, paymentRequest,orderId);
        return ApiResponse.<URLResponse>builder()
                .code(1000) // Mã trạng thái thành công
                .message("URL created successfully")
                .result(response) // Kết quả là URL thanh toán
                .build();
    }

    @GetMapping("/vnpay_ipn")
    public ApiResponse<IpnResponse> processIpn(HttpServletRequest request){

        return ApiResponse.<IpnResponse>builder()
                .result(vnPayService.handlePaymentNotification(request)) // Kết quả là URL thanh toán
                .build();
    }
    @GetMapping("/status")
    public ApiResponse<Order.Status> getstatus(@RequestParam String order_code){
        Order order  =orderRepository.findByOrderCode(order_code);
        return ApiResponse.<Order.Status>builder()
        .result(order.getStatus()) // Kết quả là URL thanh toán
                .build();
    }
    @GetMapping("/vnpay-payment-return")
    public ApiResponse<Void> changetest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
            // Retrieve the order code from the request parameter
            String order_code = request.getParameter("vnp_TxnRef");

            // Redirect to the frontend with the order_code as a query parameter
            response.sendRedirect("http://localhost:5173/order?order_code=" + order_code); // Replace with your actual URL
        }else{
            response.sendRedirect("http://localhost:5173/failPayment");
        }
        return null; // No need to return anything since redirection is handled
    }
    /*@GetMapping("/vnpay-payment-return")
    public ApiResponse<Void> processReturn(HttpServletRequest request){
       IpnResponse response =  vnPayService.handlePaymentNotification(request);
       if (response.getResponseCode().equals("00"){
           redirect.
        }
*/


    }




   /* @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model) throws JsonProcessingException {
        int paymentStatus =  vnPayService.handlePaymentNotification(request);
         User user1 = securityUtils.getCurrentUserLogin();
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        String vnp_TxnRef=request.getParameter("vnp_TxnRef");
        String[] parts = vnp_TxnRef.split(":");
        String orderId = parts[0];
        String userId_raw = parts[1];
        String shippingFee_raw = parts[2];

        User user = userRepository.findById(Integer.parseInt(userId_raw))
                .orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
        Integer shippingFee = Integer.parseInt(shippingFee_raw);
        String key = orderId;

        if(paymentStatus ==1){
            redisService.delete(key);
            Payment payment = vnPayService.buildPayment(request,true);
            paymentRepository.save(payment);
            Order order =vnPayService.buildOrder(request,payment,true,user,shippingFee);
            orderRepository.save(order);
             vnPayService.processOrderItems(key,order,"SHIPPING");
        }else{
            Payment payment = vnPayService_V2.buildPayment(request,false);
            paymentRepository.save(payment);
            Order order =vnPayService_V2.buildOrder(request,payment,false,user,shippingFee);
            orderRepository.save(order);
            vnPayService_V2.processOrderItems(key,order,"NOTCREATED");
        }

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "order success" : "order fail";
    }
    @GetMapping("/vnpay_ipn")
    public String handleReturnFromVNPay(HttpServletRequest request) {
        String response = vnPayService_V2.handlePaymentNotification(request);
        if (response.contains("Success")) {
            return "redirect:/api/v1/payments/save";
        } else {
            return "redirect:/api/v1/payments/testPaymentReturn";
        }
    }*/

    /*@GetMapping("/vnpay-ipn")
    public RedirectView handleReturnFromVNPay(HttpServletRequest request, @RequestParam Map<String, String> vnpParams) {
        String response = vnPayService_V2.handlePaymentNotification(vnpParams);
        if (response.contains("Success")) {
            return new RedirectView("http://localhost:8080/api/v1/payments2/save");
        } else {
            return new RedirectView("http://localhost:8080/api/v1/payments2/testPaymentReturn");
        }
    }

    @PostMapping("/testPaymentReturn")
    public void testA(){
        vnPayService_V2.testCall();
    }*/




