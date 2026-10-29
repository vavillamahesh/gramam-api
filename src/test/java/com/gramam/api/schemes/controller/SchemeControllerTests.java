package com.gramam.api.schemes.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.gramam.api.schemes.dto.PagedResponse;
import com.gramam.api.schemes.dto.SchemeSummary;
import com.gramam.api.schemes.exception.SchemeNotFoundException;
import com.gramam.api.schemes.model.Scheme;
import com.gramam.api.schemes.model.SchemeLevel;
import com.gramam.api.schemes.model.SchemeQuery;
import com.gramam.api.schemes.service.SchemeService;

import org.assertj.core.api.Assertions;

@WebMvcTest(SchemeController.class)
class SchemeControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private SchemeService schemeService;

	@Test
	void listPassesFiltersAndPagingToTheService() throws Exception {
		SchemeSummary summary = new SchemeSummary("pm-kisan", "PM-KISAN", "short", SchemeLevel.CENTRAL, null,
				"Agriculture Ministry", "Agriculture");
		given(schemeService.search(any(SchemeQuery.class), eq(1), eq(5)))
				.willReturn(new PagedResponse<>(List.of(summary), 1, 5, 6, 2));

		mockMvc.perform(get("/api/v1/schemes")
						.param("level", "CENTRAL")
						.param("state", "Karnataka")
						.param("category", "Agriculture")
						.param("q", "kisan")
						.param("page", "1")
						.param("size", "5"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.items[0].id").value("pm-kisan"))
				.andExpect(jsonPath("$.page").value(1))
				.andExpect(jsonPath("$.size").value(5))
				.andExpect(jsonPath("$.totalItems").value(6))
				.andExpect(jsonPath("$.totalPages").value(2));

		ArgumentCaptor<SchemeQuery> query = ArgumentCaptor.forClass(SchemeQuery.class);
		verify(schemeService).search(query.capture(), eq(1), eq(5));
		Assertions.assertThat(query.getValue())
				.isEqualTo(new SchemeQuery(SchemeLevel.CENTRAL, "Karnataka", "Agriculture", "kisan"));
	}

	@Test
	void listUsesDefaultPaging() throws Exception {
		given(schemeService.search(any(SchemeQuery.class), eq(0), eq(20)))
				.willReturn(new PagedResponse<>(List.of(), 0, 20, 0, 0));

		mockMvc.perform(get("/api/v1/schemes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.items").isEmpty());
	}

	@Test
	void listRejectsUnknownLevel() throws Exception {
		mockMvc.perform(get("/api/v1/schemes").param("level", "GALACTIC"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
	}

	@Test
	void listRejectsOutOfRangePaging() throws Exception {
		mockMvc.perform(get("/api/v1/schemes").param("size", "101")).andExpect(status().isBadRequest());
		mockMvc.perform(get("/api/v1/schemes").param("size", "0")).andExpect(status().isBadRequest());
		mockMvc.perform(get("/api/v1/schemes").param("page", "-1")).andExpect(status().isBadRequest());
	}

	@Test
	void getReturnsTheScheme() throws Exception {
		Scheme scheme = new Scheme("mgnrega", "MGNREGA", "short", "long", SchemeLevel.CENTRAL, null, "MoRD",
				"Employment", List.of("jobs"), List.of("adult"), List.of("100 days"), List.of("Aadhaar"), null);
		given(schemeService.getById("mgnrega")).willReturn(scheme);

		mockMvc.perform(get("/api/v1/schemes/mgnrega"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value("mgnrega"))
				.andExpect(jsonPath("$.level").value("CENTRAL"))
				.andExpect(jsonPath("$.benefits[0]").value("100 days"));
	}

	@Test
	void getReturnsProblemDetailWhenMissing() throws Exception {
		given(schemeService.getById("nope")).willThrow(new SchemeNotFoundException("nope"));

		mockMvc.perform(get("/api/v1/schemes/nope"))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
				.andExpect(jsonPath("$.title").value("Scheme not found"))
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.detail").value("No scheme found with id 'nope'"));
	}

	@Test
	void categoriesEndpointIsNotShadowedByTheIdRoute() throws Exception {
		given(schemeService.getCategories()).willReturn(List.of("Agriculture", "Health"));

		mockMvc.perform(get("/api/v1/schemes/categories"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0]").value("Agriculture"))
				.andExpect(jsonPath("$[1]").value("Health"));
	}
}
