package com.strabag.processor.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strabag.processor.config.AppConfig;
import com.strabag.processor.controller.ResultController;

@Service
@Qualifier("xlsxFileParser")
public class XLSXFileParserService extends FileParserService {

	private static final Logger logger = LoggerFactory.getLogger(XLSXFileParserService.class);
	
	@Override
	@Async
	public CompletableFuture<String> parseFile(String fileName) throws Exception  {
		String data = "";
		logger.info("Parsing xlsx file:{} ",fileName);
		try {
			String filePath = config.getImportFolder();
			filePath = (filePath.endsWith("/"))?filePath:filePath+"/";
			ClassPathResource resource = new ClassPathResource(fileName);
			InputStream inputStream = resource.getInputStream();

			// Create a workbook and a sheet
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			if (!rowIterator.hasNext()) {
				throw new Exception("XLSX file is empty");
			}

			// Get headers from the first row
			Row headerRow = rowIterator.next();
			List<String> headers = new ArrayList<>();
			for (Cell cell : headerRow) {
				headers.add(cell.getStringCellValue());
			}

			List<Map<String, String>> dataList = new ArrayList<>();

			// Process rows
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Map<String, String> dataMap = new HashMap<>();
				for (int i = 0; i < headers.size(); i++) {
					Cell cell = row.getCell(i);
					dataMap.put(headers.get(i), cell.toString());
				}
				dataList.add(dataMap);
			}

			workbook.close();
			ObjectMapper mapper = new ObjectMapper();
			data =  mapper.writeValueAsString(dataList);

		}catch(FileNotFoundException ex) {
			ex.printStackTrace();
			logger.error("File Not found",ex.getMessage());
			throw ex;
		}catch (IOException e) {
			e.printStackTrace();
			logger.error("Exception while parsing file ",e.getMessage());
			throw new Exception("File does not exists or cannot be opened file name : "+fileName);
		}
		logger.info("Parsed data of xlsx file:{} ",data);
		return CompletableFuture.completedFuture(data);
	}

}
