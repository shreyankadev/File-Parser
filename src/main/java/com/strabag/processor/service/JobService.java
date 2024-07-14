package com.strabag.processor.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.strabag.processor.controller.ResultController;
import com.strabag.processor.model.Job;
import com.strabag.processor.model.JobStatus;
import com.strabag.processor.repository.JobRepository;

@Service
public class JobService {

	private static final Logger logger = LoggerFactory.getLogger(JobService.class);
	
	@Autowired
	private JobRepository repo;

	public List<Job> findAll() {
		return repo.findAll();
	}

	public Optional<Job> findById(Long id) {
		return repo.findById(id);
	}

	public ResponseEntity create(Job job) {
		job.setCreatedAt(LocalDateTime.now());
		job.setStatus(JobStatus.PENDING);
		ResponseEntity response = ResponseEntity.ok("");
		try {
			return response.ok(repo.save(job));
		}catch(Exception e) {
			e.printStackTrace();
			response.ok(e.getMessage());
		}
		
		return response;
		}
	}
	

