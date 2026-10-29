package com.gramam.api.schemes.dto;

import com.gramam.api.schemes.model.Scheme;
import com.gramam.api.schemes.model.SchemeLevel;

/** Compact scheme representation used in list responses. */
public record SchemeSummary(
		String id,
		String name,
		String shortDescription,
		SchemeLevel level,
		String state,
		String ministry,
		String category) {

	public static SchemeSummary from(Scheme scheme) {
		return new SchemeSummary(
				scheme.id(),
				scheme.name(),
				scheme.shortDescription(),
				scheme.level(),
				scheme.state(),
				scheme.ministry(),
				scheme.category());
	}
}
