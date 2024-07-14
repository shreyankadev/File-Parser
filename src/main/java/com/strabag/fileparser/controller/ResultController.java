package com.strabag.fileparser.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.strabag.fileparser.model.Job;
import com.strabag.fileparser.model.Result;
import com.strabag.fileparser.service.ResultService;

@RestController
@RequestMapping("/results")
public class ResultController {

	private static final Logger logger = LoggerFactory.getLogger(ResultController.class);
	
	@Autowired
	ResultService service;

	public ResultController(ResultService service) {
		this.service =service;
	}

	@GetMapping
	public List<Result> findAll(){
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Result> findById(@PathVariable Long id){
		logger.info("find Result by id: {}", id);
		return service.findById(id);
	}

	@PostMapping("/parse")
	public ResponseEntity parseFile(@RequestParam String file) {
		logger.info("Going to parse file: {}", file);
		return service.findJobAndParseFile(file);
	}
	@GetMapping("/job/{jobId}")
	public ResponseEntity<List<Result>> getResultsByJobId(@PathVariable Long jobId){
		logger.info("find Job by Job id: {}", jobId);
		return service.getResultsByJobId(jobId);
	}

	@GetMapping("/search")
	public ResponseEntity<List<Result>> searchResults(@RequestParam String query) {
		logger.info("Searching results with query: {}", query);
		return ResponseEntity.ok(service.searchResults(query));
	}

}
