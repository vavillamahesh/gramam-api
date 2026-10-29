package com.gramam.api.schemes.provider;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.gramam.api.schemes.model.Scheme;
import com.gramam.api.schemes.model.SchemeLevel;
import com.gramam.api.schemes.model.SchemeQuery;

class MockSchemeProviderTests {

	private final MockSchemeProvider provider = new MockSchemeProvider();

	@Test
	void sampleDataHasUniqueIdsAndBothLevels() {
		List<Scheme> all = provider.search(SchemeQuery.none());

		assertThat(all).isNotEmpty();
		assertThat(all).extracting(Scheme::id).doesNotHaveDuplicates();
		assertThat(all).extracting(Scheme::level).contains(SchemeLevel.CENTRAL, SchemeLevel.STATE);
	}

	@Test
	void stateSchemesNameTheirStateAndCentralSchemesDoNot() {
		for (Scheme scheme : provider.search(SchemeQuery.none())) {
			if (scheme.level() == SchemeLevel.STATE) {
				assertThat(scheme.state()).as(scheme.id()).isNotBlank();
			}
			else {
				assertThat(scheme.state()).as(scheme.id()).isNull();
			}
		}
	}

	@Test
	void resultsAreSortedByName() {
		List<String> names = provider.search(SchemeQuery.none()).stream().map(Scheme::name).toList();

		assertThat(names).isSortedAccordingTo(String.CASE_INSENSITIVE_ORDER);
	}

	@Test
	void filtersByLevel() {
		List<Scheme> state = provider.search(new SchemeQuery(SchemeLevel.STATE, null, null, null));

		assertThat(state).isNotEmpty().allMatch(s -> s.level() == SchemeLevel.STATE);
	}

	@Test
	void stateFilterReturnsThatStateAndCentralSchemes() {
		List<Scheme> result = provider.search(new SchemeQuery(null, "karnataka", null, null));

		assertThat(result).extracting(Scheme::id).contains("karnataka-gruha-lakshmi", "pm-kisan");
		assertThat(result).extracting(Scheme::id).doesNotContain("telangana-aarogyasri");
	}

	@Test
	void stateFilterWithStateLevelReturnsOnlyThatStatesSchemes() {
		List<Scheme> result = provider.search(new SchemeQuery(SchemeLevel.STATE, "Karnataka", null, null));

		assertThat(result).extracting(Scheme::id).containsExactly("karnataka-gruha-lakshmi");
	}

	@Test
	void filtersByCategoryIgnoringCase() {
		List<Scheme> result = provider.search(new SchemeQuery(null, null, "agriculture", null));

		assertThat(result).extracting(Scheme::id).containsExactlyInAnyOrder("pm-kisan", "pmfby");
	}

	@Test
	void textSearchMatchesNamesAndTags() {
		assertThat(provider.search(new SchemeQuery(null, null, null, "crop insurance")))
				.extracting(Scheme::id).containsExactly("pmfby");
		assertThat(provider.search(new SchemeQuery(null, null, null, "KISAN")))
				.extracting(Scheme::id).contains("pm-kisan");
	}

	@Test
	void blankFiltersAreIgnored() {
		SchemeQuery blank = new SchemeQuery(null, "  ", "", " ");

		assertThat(provider.search(blank)).hasSameSizeAs(provider.search(SchemeQuery.none()));
	}

	@Test
	void findByIdReturnsSchemeOrEmpty() {
		assertThat(provider.findById("mgnrega")).isPresent();
		assertThat(provider.findById("does-not-exist")).isEmpty();
	}
}
