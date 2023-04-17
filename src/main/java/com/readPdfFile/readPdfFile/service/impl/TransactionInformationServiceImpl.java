package com.readPdfFile.readPdfFile.service.impl;


import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.readPdfFile.readPdfFile.entity.Transaction;
import com.readPdfFile.readPdfFile.service.TransactionInformationService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.*;


@Service
public class TransactionInformationServiceImpl implements TransactionInformationService {

    @Value("${pdf.directory}")
    private final String pdfDirectory;

    private final Map<String, List<String>> pdfCategories;


    public TransactionInformationServiceImpl(@Qualifier(value = "myPdfDirectory") String pdfDirectory, @Value("${pdf.categories}") String[] pdfCategories) {
        this.pdfDirectory = pdfDirectory;
        this.pdfCategories = new HashMap<>();

        for (String category : pdfCategories) {
            String[] parts = category.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid category format: " + category);
            }
            String key = parts[0];
            List<String> values = Arrays.asList(parts[1].split(","));
            System.out.println("key and values:" + key + values);
            this.pdfCategories.put(key, values);
        }
    }

    @Override
    public String extractAndSaveTransactionInfo() {
        try {
            for (Map.Entry<String, List<String>> entry : pdfCategories.entrySet()) {
                String category = entry.getKey();
                List<String> fileNames = entry.getValue();

                for (String fileName : fileNames) {
                    String filePath = pdfDirectory + fileName;
                    File file = new File(filePath);

                    System.out.println("forLoop filePath:" + filePath);

                    // Extract transaction info from PDF
                    List<Transaction> transactions = extractTransactionInfo(file);// what is in file then.

                    System.out.println("transactions: " + transactions); // it has null

                    // Convert to JSON
                    JSONArray jsonArray = new JSONArray();
                    for (Transaction transaction : transactions) {
                        JSONObject jsonObj = new JSONObject();// transactionId

                        jsonObj.put("date", transaction.getDate());
                        jsonObj.put("transactionType", transaction.getTransactionType());
                        jsonObj.put("amount", transaction.getAmount());
                        jsonObj.put("description", transaction.getDescription());


                        System.out.println(transaction.getDate());
                        System.out.println(transaction.getTransactionType());
                        System.out.println(transaction.getAmount());
                        System.out.println(transaction.getDescription());


                        jsonArray.put(jsonObj);

                        System.out.println("jsonObj: " + jsonObj);

                    }

                    // Save to file
                    System.out.println("show pdf directory: " + pdfDirectory);
                    System.out.println("show category: " + category);
                    System.out.println("show pdf fileName:" + fileName);

                    String jsonFilePath = pdfDirectory + "/" + fileName + ".json";
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
    public List<Transaction> extractTransactionInfo(File file) throws Exception {
        List<Transaction> transactions = new ArrayList<>();

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


            // Initialize variables to keep track of transaction info
            String date = null;
            String transactionType = null;
            String amount = null;
            String description = null;

            System.out.println("length: " + lines.length);

            // Search for transaction information
            for (int j = 0; j < lines.length; j++) {
                String line = lines[j].trim();


                String[] parts = line.split(":");
                if (!line.contains(":")) {  // my logic implementation
                    System.out.println("trigger");

                    // Check if line contains transaction date
                    if (line.equalsIgnoreCase("transactional Information")) {
                        System.out.println("entering transactional...");
                        String[] transArray = new String[lines.length];

                        int count = 0;

                        for (int x = j + 1; x < lines.length; x++) {

                                    line = lines[x].trim();
                                    System.out.println("line: "+ line);
                                    parts = line.split(":");

                                    if (!line.contains(":")) {
                                        System.out.println("inside for and if");
                                        continue;
                                    }
                                    transArray[count] = parts[1];

                                    count++; //8

                        }

                        for (int a = 0; a < count; a++) {
                            date = transArray[a];
                            a++;
                            transactionType = transArray[a];
                            a++;
                            amount = transArray[a];
                            a++;
                            description = transArray[a];

                            System.out.println("date: "+date);
                            System.out.println("transactionType: "+transactionType);
                            System.out.println("amount: "+amount);
                            System.out.println("description: "+description);

                            Transaction transaction = new Transaction(date, transactionType, amount, description);
                            transactions.add(transaction);

                        }

                        continue;
                    }

                    int partsCount = parts.length;

                    // Check if all transaction info has been found
                    if (date != null && transactionType != null && amount != null && description != null) {
                        // Reset transaction info variables
                        date = null;
                        transactionType = null;
                        amount = null;
                        description = null;
                    }
                }

            }
        }
        // Close PDF document
        reader.close();
        return transactions;
    }
}

