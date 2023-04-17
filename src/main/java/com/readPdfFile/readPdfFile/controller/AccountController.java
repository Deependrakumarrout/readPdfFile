package com.readPdfFile.readPdfFile.controller;


import com.readPdfFile.readPdfFile.service.AccountInformationService;
import com.readPdfFile.readPdfFile.service.TransactionInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class AccountController {


    // http://localhost:8080/api/account

    @Autowired
    AccountInformationService accountInformationService;

    @GetMapping("/account")
    public ResponseEntity<String> extractAccountInfo() {
        try {
            accountInformationService.extractAndSaveAccountInfo();
            return ResponseEntity.ok("Account information extracted and saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to extract account information: " + e.getMessage());
        }
    }

}
