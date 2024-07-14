package com.strabag.processor.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.strabag.processor.model.Job;
import com.strabag.processor.service.JobService;

@RestController
@RequestMapping("/jobs")
public class JobController {

	private static final Logger logger = LoggerFactory.getLogger(JobController.class);
	
	@Autowired
	private JobService service;

	@GetMapping
	public List<Job> findAll(){
		logger.info("Fetching all jobs" );
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Job> findById(@PathVariable Long id) {
		logger.info("Fetching all job by id:{}",id);
		return service.findById(id);
	}
	
	@PostMapping("/register-job")
	public ResponseEntity<Object> createJob(@RequestBody Job job){
		logger.info("create job with details:{} ",job);
		return	service.create(job); 
	}
	 
}
