package com.gramam.api.schemes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/** Exercises controller, service and the mock provider together. */
@SpringBootTest
@AutoConfigureMockMvc
class SchemesApiIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void listsSchemesFromTheMockProvider() throws Exception {
		mockMvc.perform(get("/api/v1/schemes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalItems").value(12))
				.andExpect(jsonPath("$.items.length()").value(12))
				.andExpect(jsonPath("$.items[0].id").exists());
	}

	@Test
	void filtersByStateAndLevel() throws Exception {
		mockMvc.perform(get("/api/v1/schemes").param("level", "STATE").param("state", "Telangana"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalItems").value(1))
				.andExpect(jsonPath("$.items[0].id").value("telangana-aarogyasri"));
	}

	@Test
	void pagesTheList() throws Exception {
		mockMvc.perform(get("/api/v1/schemes").param("size", "5").param("page", "2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.items.length()").value(2))
				.andExpect(jsonPath("$.totalPages").value(3));
	}

	@Test
	void returnsSchemeDetail() throws Exception {
		mockMvc.perform(get("/api/v1/schemes/pm-kisan"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("PM-KISAN (Pradhan Mantri Kisan Samman Nidhi)"))
				.andExpect(jsonPath("$.eligibility").isNotEmpty())
				.andExpect(jsonPath("$.documentsRequired").isNotEmpty());
	}

	@Test
	void unknownSchemeIs404() throws Exception {
		mockMvc.perform(get("/api/v1/schemes/nope")).andExpect(status().isNotFound());
	}

	@Test
	void listsCategories() throws Exception {
		mockMvc.perform(get("/api/v1/schemes/categories"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0]").value("Agriculture"));
	}
}
