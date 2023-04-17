package com.readPdfFile.readPdfFile.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean("myPdfDirectory")
    public String getPdfDirectory() {
        return "/D:/project assignment/readPdfFile/pdfFiles";
    }

}
