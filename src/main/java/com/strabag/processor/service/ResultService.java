package com.strabag.processor.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.stereotype.Service;

import com.strabag.processor.controller.ResultController;
import com.strabag.processor.model.Job;
import com.strabag.processor.model.JobStatus;
import com.strabag.processor.model.Result;
import com.strabag.processor.repository.JobRepository;
import com.strabag.processor.repository.ResultRepository;

@Service
public class ResultService {

	private static final Logger logger = LoggerFactory.getLogger(ResultService.class);
	
	@Autowired
	private Job job;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private ResultRepository resultRepository;

	private final FileParserService csvFileParser;
	private final FileParserService xlsxFileParser;

	@Autowired
	public ResultService(@Qualifier("csvFileParser") FileParserService csvFileParser,
			@Qualifier("xlsxFileParser") FileParserService xlsxFileParser) {
		this.csvFileParser = csvFileParser;
		this.xlsxFileParser = xlsxFileParser;
	}

	public List<Result> findAll(){
		return resultRepository.findAll();
	}

	public Optional<Result> findById(Long id) {
		return resultRepository.findById(id);
	}

	public ResponseEntity findJobAndParseFile(String fileName) {
		String data ="";
		ResponseEntity response = ResponseEntity.ok("");
		Result result;
		try {
			job = getJob(fileName);
			data = getParsedData(fileName);
			result = createResult(data,job);
			response= response.ok("File SuccessFully Parsed\n"+result);
			logger.info("findJobAndParseFile() parsed response ", response );
		}catch(Exception e) {
			logger.error("Exception ",e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		return response;

	}

	public Map<Long, Long> countResultsByJob() {
        List<Object[]> results = resultRepository.countResultsByJobId();
        return results.stream().collect(Collectors.toMap(result -> (Long) result[0], result -> (Long) result[1]));
    }
	
	private String getParsedData(String fileName) throws Exception {
		String data = null ;
		try {
			if(fileName.endsWith(".xlsx")) {
				data = xlsxFileParser.parseFile(fileName).get();
			}else if(fileName.endsWith(".csv")) {
				data = csvFileParser.parseFile(fileName).get();
			}else {
				throw new Exception("Unsupported File Type");
			}
			logger.info("getParsedData parsed data ", data );
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return data;
	}

	public Job getJob(String fileName) throws Exception {
		Job tempJob = new Job();
		tempJob.setFileName(fileName);
		Example example = Example.of(tempJob);

		try {
			List<Job> jobs = jobRepository.findAll(example);
			if(jobs.isEmpty()) {
				job = createJob(fileName);
			}else {
				job = jobs.get(0);
			}
			logger.info("getJob Job", job );
			return job;
		}catch(Exception e) {
			throw new Exception(e.getMessage());
		}
	}


	private Job createJob(String fileName) throws Exception {
		try {

			job = new Job();
			job.setFileName(fileName);
			job.setCreatedAt(LocalDateTime.now());
			job.setStatus(JobStatus.PENDING);
			job.setFailureReason("");
			jobRepository.save(job);
			logger.info("createJob Job", job );
		}catch(Exception e) {
			e.printStackTrace();
			throw new Exception("problem creating Job: "+job);
		}
		return job;
	}

	private Result createResult(String data, Job job) {
		Result result = new Result();
		result.setJobId(job.getId());
		result.setResult(data);
		result.setFileName(job.getFileName());
		result.setCreatedAt(LocalDateTime.now());
		resultRepository.save(result);
		job.setStatus(JobStatus.SUCCESS);
		jobRepository.save(job);
		logger.info("createResult Job", job +" Result "+result);
		return result;
	}

	public List<Result> searchResults(String query) {
		query = Optional.ofNullable(query).orElse("");
		return resultRepository.findByResultContaining(query);
	}

	public ResponseEntity<List<Result>> getResultsByJobId(Long jobId) {

		Result result = new Result();
		result.setJobId(jobId);
		Example<Result> example = Example.of(result);
		return ResponseEntity.ok(resultRepository.findAll(example));

	}

}
