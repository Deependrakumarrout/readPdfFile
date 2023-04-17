package com.readPdfFile.readPdfFile.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Account {
    private String accountNumber;
    private String accountType;
    private String openingDate;
    private String balance;

}
