package com.readPdfFile.readPdfFile.service;


import com.readPdfFile.readPdfFile.entity.Transaction;

import java.io.File;
import java.util.List;

public interface TransactionInformationService {
    public String extractAndSaveTransactionInfo();
    public List<Transaction> extractTransactionInfo(File file) throws Exception;

    }
