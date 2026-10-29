package com.gramam.api.schemes.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gramam.api.schemes.dto.PagedResponse;
import com.gramam.api.schemes.dto.SchemeSummary;
import com.gramam.api.schemes.model.Scheme;
import com.gramam.api.schemes.model.SchemeLevel;
import com.gramam.api.schemes.model.SchemeQuery;
import com.gramam.api.schemes.service.SchemeService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/v1/schemes")
public class SchemeController {

	private final SchemeService schemeService;

	public SchemeController(SchemeService schemeService) {
		this.schemeService = schemeService;
	}

	/**
	 * Lists schemes with optional filters and paging.
	 *
	 * @param level {@code CENTRAL} or {@code STATE} (upper case)
	 * @param state state name; also returns central schemes unless {@code level=STATE}
	 * @param q free-text search
	 * @param page zero-based page index
	 */
	@GetMapping
	public PagedResponse<SchemeSummary> list(
			@RequestParam(name = "level", required = false) SchemeLevel level,
			@RequestParam(name = "state", required = false) String state,
			@RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "q", required = false) String q,
			@RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
			@RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(100) int size) {
		return schemeService.search(new SchemeQuery(level, state, category, q), page, size);
	}

	@GetMapping("/categories")
	public List<String> categories() {
		return schemeService.getCategories();
	}

	@GetMapping("/{id}")
	public Scheme get(@PathVariable("id") String id) {
		return schemeService.getById(id);
	}
}
