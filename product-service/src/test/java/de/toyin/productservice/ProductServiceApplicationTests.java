package de.toyin.productservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.toyin.productservice.dto.ProductRequest;
import de.toyin.productservice.dto.ProductResponse;
import de.toyin.productservice.repository.ProductRepository;
import de.toyin.productservice.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)

class ProductServiceApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	@Autowired
	private MockMvc mockMvc;
	@Mock
	private ProductService productService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry ){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri",mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception{
		ProductRequest productRequest = getProductRequest();
		String productRequeststring = objectMapper.writeValueAsString(productRequest);
	mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
					.contentType(MediaType.APPLICATION_JSON)
					.content(productRequeststring))
			.andExpect(status().isCreated());
		Assertions.assertEquals(1, productRepository.findAll().size());
	}

		private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Iphone 13")
				.description("iphone 13")
				.price(BigDecimal.valueOf(12))
				.build();
	}

	@Test
	void shouldGetProducts() throws Exception {
		// Mock the service layer response
		List<ProductResponse> expectedResponse = new ArrayList<>();
		expectedResponse.add(getResponse());
		when(productService.getAllProducts()).thenReturn(expectedResponse);

		// Make the request to "/api/product"
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/product"));

		// Assert the response status is OK (HTTP 200)
		resultActions.andExpect(status().isOk());

		// Assert the response content matches the expected JSON representation
		resultActions.andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
	}

	private ProductResponse getResponse() {
		return ProductResponse.builder()
				.price(BigDecimal.valueOf(12))
				.description("iphone 13")
				.name("Iphone 13")
				.build();
	}
}