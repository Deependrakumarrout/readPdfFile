package com.readPdfFile.readPdfFile.service.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.readPdfFile.readPdfFile.entity.Customer;
import com.readPdfFile.readPdfFile.service.CustomerInformationService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

@Service
public class CustomerInformationServiceImpl implements CustomerInformationService {

    @Value("${pdf.directory}")
    private final String pdfDirectory;

    private final Map<String, List<String>> pdfCategories;

    public CustomerInformationServiceImpl(@Qualifier(value = "myPdfDirectory") String pdfDirectory, @Value("${pdf.categories}") String[] pdfCategories) {
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
    public String extractAndSaveCustomerInfo() {
        try {
            for (Map.Entry<String, List<String>> entry : pdfCategories.entrySet()) {
                String category = entry.getKey();
                List<String> fileNames = entry.getValue();

                for (String fileName : fileNames) {
                    String filePath = pdfDirectory + fileName;
                    File file = new File(filePath);

                    // Extract customer info from PDF
                    List<Customer> customers = extractCustomerInfo(file);

                    // Convert to JSON
                    JSONArray jsonArray = new JSONArray();
                    for (Customer customer : customers) {
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("name", customer.getName());
                        jsonObj.put("address", customer.getAddress());
                        jsonObj.put("city", customer.getCity());
                        jsonObj.put("phone", customer.getPhone());
                        jsonObj.put("email", customer.getEmail());

                        jsonArray.put(jsonObj);
                    }

                    // Save to file
                    String jsonFilePath = pdfDirectory + "/" + fileName+"1" + ".json";
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
    public List<Customer> extractCustomerInfo(File file) throws Exception {
        List<Customer> customers = new ArrayList<>();

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
            String name = null;
            String address = null;
            String city = null;
            String phone = null;
            String email = null;

            System.out.println("length: " + lines.length);

            // Search for transaction information
            for (int j = 0; j < lines.length; j++) {
                String line = lines[j].trim();


                String[] parts = line.split(":");
                if (!line.contains(":")) { 
                    System.out.println("trigger");

                    // Check if line contains customer date
                    if (line.equalsIgnoreCase("customer Information")) {
                        System.out.println("entering customer...");
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

                            count++; 

                        }


                        for (int a = 0; a < count; a++) {
                            name = transArray[a];
                            a++;
                            address = transArray[a];
                            a++;
                            city = transArray[a];
                            a++;
                            phone = transArray[a];
                            a++;
                            email = transArray[a];
                            
                            Customer customer = new Customer(name, address, city, phone, email);
                            customers.add(customer);

                        }
                        continue;
                    }

                    int partsCount = parts.length;

                    System.out.println("parts[0]: " + parts[0]); 


                    // Check if all customer info has been found
                    if (name != null && address != null && city != null && phone != null && email != null) {
                        // Reset customer info variables
                        name = null;
                        address = null;
                        city = null;
                        phone = null;
                        email = null;
                    }
                }
            }
        }

        // Close PDF document
        reader.close();
        return customers;
    }
}

