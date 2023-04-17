package com.readPdfFile.readPdfFile.controller;

import com.readPdfFile.readPdfFile.service.AccountInformationService;
import com.readPdfFile.readPdfFile.service.CustomerInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class CustomerController {

    // http://localhost:8080/api/customer

    @Autowired
    CustomerInformationService customerInformationService;

    @GetMapping("/customer")
    public ResponseEntity<String> extractAccountInfo() {
        try {
            customerInformationService.extractAndSaveCustomerInfo();
            return ResponseEntity.ok("Customer information extracted and saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to extract customer information: " + e.getMessage());
        }
    }
}
