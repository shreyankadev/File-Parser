package com.strabag.fileparser;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strabag.fileparser.model.Job;
import com.strabag.fileparser.model.JobStatus;
import com.strabag.fileparser.repository.JobRepository;
import com.strabag.fileparser.service.JobService;

@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private JobService service;
    
    @Mock
    private JobRepository repository;

    
    @Test
    public void resgisterJobFile() throws Exception {
    	 String jsonRequest = "{\"fileName\":\"File2.csv\"}";
    	 String jsonResponse = mockMvc.perform(MockMvcRequestBuilders.post("/jobs/register-job")
        		.contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        
        Job response = objectMapper.readValue(jsonResponse, Job.class);

  
        assert response.getId() != null;
        assert response.getStatus().equals(JobStatus.PENDING);
        assert response.getFileName().equals("File2.csv");
        assert response.getFailureReason() == null;
    }

    
    @Test
    public void testFindAll() throws Exception {
       
        Mockito.when(service.findAll()).thenReturn(Arrays.asList(
                new Job(1L, "File 1", JobStatus.PENDING,"",LocalDateTime.now()),
                new Job(2L, "File 2", JobStatus.SUCCESS,"",LocalDateTime.now())
        ));
        
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/jobs"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("Response: " + response);

        mockMvc.perform(MockMvcRequestBuilders.get("/jobs"))
                .andExpect(MockMvcResultMatchers.status().isOk());
                
    }

    @Test
    public void testFindById() throws Exception {
        Long jobId = 1L;
        Mockito.when(service.findById(Mockito.any())).thenReturn(Optional.of(new Job(1L, "File 1", JobStatus.PENDING,"",LocalDateTime.now())));

        mockMvc.perform(MockMvcRequestBuilders.get("/jobs/{id}", jobId))
                .andExpect(MockMvcResultMatchers.status().isOk());
                
    }

}

