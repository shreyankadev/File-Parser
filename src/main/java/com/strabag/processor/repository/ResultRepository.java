package com.strabag.processor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.strabag.processor.model.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {


	Optional<Result> findById(Long id);

	List<Result> findByResultContaining(String query);

	@Query("SELECT pr.jobId, COUNT(pr) FROM Result pr GROUP BY pr.jobId")
	List<Object[]> countResultsByJobId();
}