package com.gramam.api.schemes.provider;

import java.util.List;
import java.util.Optional;

import com.gramam.api.schemes.model.Scheme;
import com.gramam.api.schemes.model.SchemeQuery;

/**
 * Source of scheme data. Implement this to plug in a real data source (for example the MyScheme
 * APIs) and select it with {@code gramam.schemes.provider}; the service and controller do not change.
 */
public interface SchemeProvider {

	/** Returns all schemes matching the query, sorted by name. Never {@code null}. */
	List<Scheme> search(SchemeQuery query);

	Optional<Scheme> findById(String id);
}
