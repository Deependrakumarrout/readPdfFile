package com.readPdfFile.readPdfFile.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TransactionDetails {
    private String date;
    private String transactionType;
    private String amount;
    private String description;
}
