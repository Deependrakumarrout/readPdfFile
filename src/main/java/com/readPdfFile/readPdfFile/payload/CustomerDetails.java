package com.readPdfFile.readPdfFile.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CustomerDetails {

    private String name;
    private String address;
    private String city;
    private String phone;
    private String email;

}
