package com.gramam.api.schemes.dto;

import java.util.List;

/**
 * One page of results.
 *
 * @param page zero-based page index
 */
public record PagedResponse<T>(List<T> items, int page, int size, long totalItems, int totalPages) {
}
