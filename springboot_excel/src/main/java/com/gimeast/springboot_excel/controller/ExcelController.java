package com.gimeast.springboot_excel.controller;

import com.gimeast.springboot_excel.service.ExcelService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Log4j2
public class ExcelController {

    private final ExcelService excelService;

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @GetMapping("/download")
    public void download(HttpServletResponse res, String fileName) throws Exception {
        excelService.excelDownload(res, fileName);
        log.info("Excel download success...");
    }

}
