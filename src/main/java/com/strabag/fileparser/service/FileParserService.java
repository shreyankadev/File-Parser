package com.strabag.fileparser.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.strabag.fileparser.config.AppConfig;

@Service
public abstract class FileParserService {
	
	@Autowired
	protected AppConfig config;
	@Async
	public abstract CompletableFuture<String> parseFile(String fileName) throws Exception;

}