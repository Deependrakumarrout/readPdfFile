package com.readPdfFile.readPdfFile.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Customer {
    private String name;
    private String address;
    private String city;
    private String phone;
    private String email;
}
