package com.example.ecommerce.shopbase.service.report;

import java.io.IOException;
import java.util.List;

import java.io.OutputStream;


import com.example.ecommerce.shopbase.dto.report.ProductReportDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    public void generateExcel(HttpServletResponse response,List<ProductReportDTO> list) throws IOException {


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Shop Report");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("ID");
        row.getCell(0).setCellStyle(createStyleForHeader(sheet));
        row.createCell(1).setCellValue("Name");
        row.getCell(1).setCellStyle(createStyleForHeader(sheet));
        row.createCell(2).setCellValue("Quantity");
        row.getCell(2).setCellStyle(createStyleForHeader(sheet));

        int dataRowIndex = 1;

        for (ProductReportDTO product : list) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(product.getId());
            dataRow.createCell(1).setCellValue(product.getName());
            dataRow.createCell(2).setCellValue(product.getQuantity());
            dataRowIndex++;
        }

        OutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();

    }

    private static CellStyle createStyleForHeader(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.WHITE.getIndex());

        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }

}