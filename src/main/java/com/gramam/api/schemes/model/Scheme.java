package com.gramam.api.schemes.model;

import java.util.List;

/**
 * A government scheme. This is Gramam's own model: providers (mock today, MyScheme later)
 * map whatever they receive into this shape, so the API contract does not depend on the source.
 *
 * @param state the state that runs the scheme; {@code null} for central schemes
 */
public record Scheme(
		String id,
		String name,
		String shortDescription,
		String description,
		SchemeLevel level,
		String state,
		String ministry,
		String category,
		List<String> tags,
		List<String> eligibility,
		List<String> benefits,
		List<String> documentsRequired,
		String applicationUrl) {
}
