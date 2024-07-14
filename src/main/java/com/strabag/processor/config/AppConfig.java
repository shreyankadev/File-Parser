package com.strabag.processor.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.strabag.processor.model.Job;
import com.strabag.processor.model.Result;

@Configuration
public class AppConfig {
 @Value("${import.folder}")
 private String importFolder;

 public String getImportFolder() {
     return importFolder;
 }
 
 @Bean
 public Job job() {
     return new Job();
 }
 
 @Bean
 public Result parsedResult() {
     return new Result();
 }
}
