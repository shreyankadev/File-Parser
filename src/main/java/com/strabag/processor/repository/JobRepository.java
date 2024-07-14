package com.strabag.processor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strabag.processor.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
	
	Job findByFileName(String fileName);

	Job save(Job job);
	
	Optional<Job> findById(Long id);

}


