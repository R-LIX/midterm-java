package com.form.database;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Csv {
    private final File file;
    private final List<String> header;

    public Csv(File file, List<String> header) {
        this.file = file;
        this.header = header;
    }

    public void addRecord(String content) {
        var records = loadCsvToMap();
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(file, true));
            //Create record
            String[] record = content.split(",");
            //Check if the record already exist
            var username = record[0];
            if (records.containsKey(username)) throw new DuplicateRecordException(username);

            //Write the record to file
            writer.writeNext(record, false);

            //close the writer
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteRecordByKey(String key) {
        if (key.equalsIgnoreCase(header.getFirst())) throw new RuntimeException("Cannot delete header");
        var rowNumber = findRowByKey(key);
        if (rowNumber != null) deleteRecordByRow(rowNumber);
    }
    public void deleteRecordByRow(int rowNumber) {
        try {
            var allData = loadCsv();
            allData.remove(rowNumber);
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            writer.writeAll(allData,false);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Map<String,String> loadCsvToMap() {
        var map = new HashMap<String, String>();
        var records = loadCsv();
        if (records.isEmpty()) return new HashMap<>();

        for (String[] record : records) {
            if (record[0].equalsIgnoreCase(header.getFirst())) continue;
            map.put(record[0], record[1]);
        }
        return map;
    }

    public List<String[]> loadCsv() {
        List<String[]> allData = new ArrayList<>();

        try {
            // Create an object of a file reader class with a
            //  CSV file as a parameter.
            FileReader filereader = new FileReader(file);

            // create a csvReader object
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .build();
            allData = csvReader.readAll();

        } catch (IOException | CsvException e) {
            System.out.println(e.getMessage());
        }

        return allData;


    }
    public Integer findRowByKey(String key) {
        var records = loadCsv();
        for (String[] record : records) {
            if (record[0].equalsIgnoreCase(key)) return records.indexOf(record);
        }
        return null;
    }


}

