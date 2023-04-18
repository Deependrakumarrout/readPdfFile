package com.readPdfFile.readPdfFile.service.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.readPdfFile.readPdfFile.entity.Account;
import com.readPdfFile.readPdfFile.entity.Transaction;
import com.readPdfFile.readPdfFile.service.AccountInformationService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

@Service
public class AccountInformationServiceImpl implements AccountInformationService {

    @Value("${pdf.directory}")
    private final String pdfDirectory;

    private final Map<String, List<String>> pdfCategories;

    public AccountInformationServiceImpl(@Qualifier(value = "myPdfDirectory") String pdfDirectory, @Value("${pdf.categories}") String[] pdfCategories) {
        this.pdfDirectory = pdfDirectory;
        this.pdfCategories = new HashMap<>();

        for (String category : pdfCategories) {
            String[] parts = category.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid category format: " + category);
            }
            String key = parts[0];
            List<String> values = Arrays.asList(parts[1].split(","));
            this.pdfCategories.put(key, values);
        }
    }


    @Override
    public String extractAndSaveAccountInfo() {
        try {
            for (Map.Entry<String, List<String>> entry : pdfCategories.entrySet()) {
                String category = entry.getKey();
                List<String> fileNames = entry.getValue();

                for (String fileName : fileNames) {
                    String filePath = pdfDirectory + fileName;
                    File file = new File(filePath);

                    // Extract account info from PDF
                    List<Account> accounts = extractAccountInfo(file);

                    // Convert to JSON
                    JSONArray jsonArray = new JSONArray();
                    for (Account account : accounts) {
                        JSONObject jsonObj = new JSONObject();

                        jsonObj.put("accountNumber", account.getAccountNumber());
                        jsonObj.put("accountType", account.getAccountType());
                        jsonObj.put("openingDate", account.getOpeningDate());
                        jsonObj.put("balance", account.getBalance());

                        jsonArray.put(jsonObj);
                    }

                    // Save to file
                    String jsonFilePath = pdfDirectory + "/" + fileName+"2" + ".json";
                    FileWriter fileWriter = new FileWriter(jsonFilePath);
                    fileWriter.write(jsonArray.toString());
                    fileWriter.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    @Override
    public List<Account> extractAccountInfo(File file) throws Exception {
        List<Account> accounts = new ArrayList<>();

        // Load PDF document
        PdfReader reader = new PdfReader(file.getAbsolutePath());

        // Create a PdfReaderContentParser from the PdfReader
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);

        // Iterate through pages
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            // Create a TextExtractionStrategy from the parser
            TextExtractionStrategy strategy = parser.processContent(i, new SimpleTextExtractionStrategy());

            // Extract text from page
            String pageText = strategy.getResultantText();

            // Split text into lines
            String[] lines = pageText.split("\\r?\\n");


            // Initialize variables to keep track of account info
            String accountNumber = null;
            String accountType = null;
            String openingDate = null;
            String balance = null;

            System.out.println("length: " + lines.length);

            // Search for account information
            for (int j = 0; j < lines.length; j++) {
                String line = lines[j].trim();


                String[] parts = line.split(":");
                if (!line.contains(":")) {  
                    System.out.println("trigger");

                    // Check if line contains account date
                    if (line.equalsIgnoreCase("account Information")) {
                        System.out.println("entering account...");
                        String[] transArray = new String[lines.length];

                        int count = 0;
                        for (int x = j + 1; x < lines.length; x++) {

                            line = lines[x].trim();
                            System.out.println("line: "+ line);
                            parts = line.split(":");

                            if (!line.contains(":")) {
                                System.out.println("inside for and if");
                                break;
                            }
                            transArray[count] = parts[1];

                            count++; //8

                        }


                        for (int a = 0; a < count; a++) {
                            accountNumber = transArray[a];
                            a++;
                            accountType = transArray[a];
                            a++;
                            openingDate = transArray[a];
                            a++;
                            balance = transArray[a];
                            
                            Account account = new Account(accountNumber, accountType, openingDate, balance);
                            accounts.add(account);

                        }
                        continue;
                    }

                    int partsCount = parts.length;

                    System.out.println("parts[0]: " + parts[0]); 


                    // Check if all account info has been found
                    if (accountNumber != null && accountType != null && openingDate != null && balance != null) {
                        // Reset account  info variables
                        accountNumber = null;
                        accountType = null;
                        openingDate = null;
                        balance = null;
                    }
                }
            }
        }

        // Close PDF document
        reader.close();
        return accounts;
    }
}

