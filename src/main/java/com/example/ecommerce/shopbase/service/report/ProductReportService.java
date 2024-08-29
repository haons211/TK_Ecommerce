package com.example.ecommerce.shopbase.service.report;

import com.example.ecommerce.shopbase.dto.report.ProductReportDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ProductReportService {

    List<ProductReportDTO> generateProductReport(LocalDate startDate,LocalDate endDate);

}
