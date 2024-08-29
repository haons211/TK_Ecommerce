package com.example.ecommerce.shopbase.controller.product;

import com.example.ecommerce.shopbase.dto.report.ProductReportDTO;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.service.report.ProductReportService;
import com.example.ecommerce.shopbase.service.report.ReportService;
import com.google.api.client.util.DateTime;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product/")
@RequiredArgsConstructor
public class ReportRestController {

    private final ReportService reportService;

    private final ProductReportService productReportService;

    @GetMapping("/report-1")
    public ApiResponse<List<ProductReportDTO>> generateReport(@RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                        @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<ProductReportDTO> productReportDTOList = productReportService.generateProductReport(startDate, endDate);
        return ApiResponse.<List<ProductReportDTO>>builder()
                .result(productReportDTOList)
                .build();
    }

    @GetMapping("/report-excel-1")
    public void generateExcelReport(HttpServletResponse response,
                                    @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                    @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
                                    ) throws IOException {

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=report.xls";

        response.setHeader(headerKey, headerValue);

        List<ProductReportDTO> productReportDTOList = productReportService.generateProductReport(startDate, endDate);

        reportService.generateExcel(response, productReportDTOList);

        response.flushBuffer();
    }
}
