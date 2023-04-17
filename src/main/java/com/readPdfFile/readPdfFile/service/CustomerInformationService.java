package com.readPdfFile.readPdfFile.service;

import com.readPdfFile.readPdfFile.entity.Customer;

import java.io.File;
import java.util.List;

public interface CustomerInformationService {
    public String extractAndSaveCustomerInfo();
    public List<Customer> extractCustomerInfo(File file) throws Exception;
    }
