package com.readPdfFile.readPdfFile.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AccountDetails {
    private String accountNumber;
    private String accountType;
    private String openingDate;
    private String balance;
}
