package com.readPdfFile.readPdfFile.controller;

import com.readPdfFile.readPdfFile.service.TransactionInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class TransactionController {


    // http://localhost:8080/api/transaction

    @Autowired
    TransactionInformationService transactionService;

    @GetMapping("/transaction")
    public ResponseEntity<String> extractTransactionInfo() {
        try {
            transactionService.extractAndSaveTransactionInfo();
            return ResponseEntity.ok("Transaction information extracted and saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to extract transaction information: " + e.getMessage());
        }
    }

}
