package com.KMSDemo.KMSDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.KMSDemo.KMSDemo.Controllers.KmsController;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(KmsController.class)
public class KmsControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	
	
	
	

	@Autowired
	private ObjectMapper objectMapper;

}
