package com.gramam.api.schemes.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.gramam.api.schemes.dto.PagedResponse;
import com.gramam.api.schemes.dto.SchemeSummary;
import com.gramam.api.schemes.exception.SchemeNotFoundException;
import com.gramam.api.schemes.model.Scheme;
import com.gramam.api.schemes.model.SchemeQuery;
import com.gramam.api.schemes.provider.SchemeProvider;

@Service
public class SchemeService {

	private final SchemeProvider provider;

	public SchemeService(SchemeProvider provider) {
		this.provider = provider;
	}

	public PagedResponse<SchemeSummary> search(SchemeQuery query, int page, int size) {
		if (page < 0 || size < 1) {
			throw new IllegalArgumentException("page must be >= 0 and size must be >= 1");
		}
		List<Scheme> matches = provider.search(query);
		long total = matches.size();
		int totalPages = (int) ((total + size - 1) / size);
		long from = Math.min((long) page * size, total);
		long to = Math.min(from + size, total);
		List<SchemeSummary> items = matches.subList((int) from, (int) to).stream()
				.map(SchemeSummary::from)
				.toList();
		return new PagedResponse<>(items, page, size, total, totalPages);
	}

	public Scheme getById(String id) {
		return provider.findById(id).orElseThrow(() -> new SchemeNotFoundException(id));
	}

	/** Distinct categories across all schemes, for building filter dropdowns. */
	public List<String> getCategories() {
		return provider.search(SchemeQuery.none()).stream()
				.map(Scheme::category)
				.filter(Objects::nonNull)
				.distinct()
				.sorted(String.CASE_INSENSITIVE_ORDER)
				.toList();
	}
}
