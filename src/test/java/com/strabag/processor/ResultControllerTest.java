package com.strabag.processor;

import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strabag.processor.model.Result;
import com.strabag.processor.repository.JobRepository;
import com.strabag.processor.repository.ResultRepository;
import com.strabag.processor.service.ResultService;

@SpringBootTest
@AutoConfigureMockMvc
public class ResultControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ResultService resultService;

	@Mock
	private ResultRepository resultRepository;

	@InjectMocks
	private ResultService serviceUnderTest;

	@Test
	public void testReadFile() throws Exception {
		Matcher matcher = anything("File SuccessFully Parsed");
		mockMvc.perform(MockMvcRequestBuilders.post("/results/parse")
				.param("file", "File1.xlsx"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().string(matcher));
	}


	@Test
	public void testFindAll() throws Exception {
		Mockito.when(resultService.findAll()).thenReturn(Arrays.asList( new Result(1L,
				1L,LocalDateTime.now(),"Result1", "File 1"), new Result(2L,
						2L,LocalDateTime.now(),"Result2", "File 2") ));

		String response = mockMvc.perform(MockMvcRequestBuilders.get("/results"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		System.out.println("Response Content: " + response);
		mockMvc.perform(MockMvcRequestBuilders.get("/results"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].jobId").value(1))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].result").value("Result1"))
		.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));
	}

	@Test
	public void testFindById() throws Exception {
		Long resultId = 1L;

		Mockito.when(resultService.findById(resultId)).thenReturn(Optional.of(new
				Result(1L, 1L,LocalDateTime.now(),"Result1", "File 1")));

		mockMvc.perform(MockMvcRequestBuilders.get("/results/{id}", resultId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(resultId));
	}

	@Test
	public void testGetResultsByJobId() throws Exception {
		Long jobId = 1L;

		List<Result> results = Arrays.asList( new Result(1L,
				1L,LocalDateTime.now(),"Result1", "File 1"), new Result(2L,
						2L,LocalDateTime.now(),"Result2", "File 2") );



		Mockito.when(resultService.getResultsByJobId(jobId)).thenReturn(ResponseEntity.ok(
				results));


		mockMvc.perform(MockMvcRequestBuilders.get("/results/job/{jobId}", jobId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].jobId").value(jobId));
	}

	@Test
	public void testSearchResults() throws Exception {
		String query = "Result";
		List<Result> results = Arrays.asList(
				new Result(1L, 1L,LocalDateTime.now(),"Result1", "File 1"),
				new Result(2L, 2L,LocalDateTime.now(),"Result2", "File 2")
				);

		when(resultService.searchResults(query)).thenReturn(results); 
		String responseContent = mockMvc.perform(get("/results/search").param("query", query))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		System.out.println("Response Content: " + responseContent);
		mockMvc.perform(MockMvcRequestBuilders.get("/results/search")
				.param("query", query))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
		.andExpect(MockMvcResultMatchers.jsonPath("$[1].result").value("Result2"));
	}

}
