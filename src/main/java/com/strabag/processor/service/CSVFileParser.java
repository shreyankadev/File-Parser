package com.strabag.processor.service;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.strabag.processor.config.AppConfig;
import com.strabag.processor.controller.ResultController;

@Service
@Qualifier("csvFileParser")
public class CSVFileParser extends FileParserService {

	private static final Logger logger = LoggerFactory.getLogger(CSVFileParser.class);
	
	@Override
	public CompletableFuture<String> parseFile(String fileName) throws Exception {
		logger.info("Parsing csv file :{}",fileName);
		String data ="";
		try {
			String filePath = config.getImportFolder();
			filePath = (filePath.endsWith("/"))?filePath:filePath+"/";
			ClassPathResource resource = new ClassPathResource(filePath+fileName);
			InputStreamReader reader = new InputStreamReader(resource.getInputStream());

			// Use CSVReader to read the CSV file
			CSVReader csvReader = new CSVReader(reader);
			List<String[]> csvLines = csvReader.readAll();
			csvReader.close();

			if (csvLines.isEmpty()) {
				throw new Exception("CSV file is empty");
			}

			// Get headers from the first row
			String[] headers = csvLines.get(0);
			List<Map<String, String>> dataList = new ArrayList<>();

			// Process rows
			for (int i = 1; i < csvLines.size(); i++) {
				String[] row = csvLines.get(i);
				Map<String, String> dataMap = new HashMap<>();
				for (int j = 0; j < headers.length; j++) {
					dataMap.put(headers[j], row[j]);
				}
				dataList.add(dataMap);
			}

			// Convert the data list to JSON
			ObjectMapper mapper = new ObjectMapper();
			data = mapper.writeValueAsString(dataList);
			System.out.println("parse csv file data" + data);

		}catch(FileNotFoundException ex) {
			ex.printStackTrace();
			logger.error("File Not found",ex.getMessage());
			throw ex;
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception while parsing file ",e.getMessage());
			throw e;
		}

		logger.info("Parsed data of csv file :{}",data);
		return CompletableFuture.completedFuture(data);

	}

}
