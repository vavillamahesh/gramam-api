package com.gramam.api.schemes.model;

/**
 * Filters for searching schemes. Every field is optional; blank strings are treated as absent.
 *
 * <p>A {@code state} filter matches that state's schemes <em>and</em> central schemes, because
 * central schemes apply in every state. Combine it with {@code level=STATE} to get only the
 * state's own schemes.
 *
 * @param text free-text search over name, descriptions, ministry and tags
 */
public record SchemeQuery(SchemeLevel level, String state, String category, String text) {

	public SchemeQuery {
		state = normalize(state);
		category = normalize(category);
		text = normalize(text);
	}

	public static SchemeQuery none() {
		return new SchemeQuery(null, null, null, null);
	}

	private static String normalize(String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}
}
