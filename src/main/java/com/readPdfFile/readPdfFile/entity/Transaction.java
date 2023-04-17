package com.readPdfFile.readPdfFile.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Transaction {
    private String date;
    private String transactionType;
    private String amount;
    private String description;
}
