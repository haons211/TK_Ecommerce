package com.example.ecommerce.shopbase.service.report;

import com.example.ecommerce.shopbase.dto.report.ProductReportDTO;
import com.example.ecommerce.shopbase.entity.Seller;
import com.example.ecommerce.shopbase.entity.User;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.ProductRepository;
import com.example.ecommerce.shopbase.repository.SellerRepository;
import com.example.ecommerce.shopbase.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductReportServiceImpl implements ProductReportService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SecurityUtils securityUtils;

    @Autowired
    SellerRepository sellerRepository;

    @Override
    public List<ProductReportDTO> generateProductReport(LocalDate startDate, LocalDate endDate) {
        User currentUser = securityUtils.getCurrentUserLogin();
        if(currentUser == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        Seller seller = sellerRepository.findSellersByUserId(currentUser.getId());
        if(seller == null) {
            throw new AppException(ErrorCode.SELLER_NOT_EXISTED);
        }
        List<Object> t = productRepository.getProductReport(seller.getId(), startDate, endDate);
        return t.stream().map(o -> {
            Object[] obj = (Object[]) o;
            return new ProductReportDTO((Integer) obj[0], (String) obj[1], ((BigDecimal) obj[2]).intValue());
        }).collect(Collectors.toList());
    }
}
