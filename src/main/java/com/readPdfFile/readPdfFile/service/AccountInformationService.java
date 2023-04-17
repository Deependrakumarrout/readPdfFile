package com.readPdfFile.readPdfFile.service;

import com.readPdfFile.readPdfFile.entity.Account;

import java.io.File;
import java.util.List;

public interface AccountInformationService {

    public String extractAndSaveAccountInfo();
    public List<Account> extractAccountInfo(File file) throws Exception;

}
