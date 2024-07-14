package com.strabag.processor.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.strabag.processor.service.ResultService;

@RestController
@RequestMapping("/chart")
@CrossOrigin(origins = "http://localhost:8080")
public class ChartController {


	@Autowired
	private ResultService service;

	@GetMapping("/{maxJobId}")
	public Map<Long, Long> getResultsByJob(@PathVariable String maxJobId) {
		Integer maxId = Integer.parseInt(maxJobId);
		Map<Long,Long> chartValues =  service.countResultsByJob();
		if(maxJobId != null)
			chartValues.entrySet().removeIf(entry -> entry.getKey() > maxId);
		return chartValues;
	}
	
	@GetMapping
	public Map<Long, Long> getResultsByJob() {
		
		return  service.countResultsByJob();
	}

}
