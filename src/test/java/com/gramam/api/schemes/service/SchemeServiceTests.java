package com.gramam.api.schemes.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.gramam.api.schemes.dto.PagedResponse;
import com.gramam.api.schemes.dto.SchemeSummary;
import com.gramam.api.schemes.exception.SchemeNotFoundException;
import com.gramam.api.schemes.model.Scheme;
import com.gramam.api.schemes.model.SchemeLevel;
import com.gramam.api.schemes.model.SchemeQuery;
import com.gramam.api.schemes.provider.SchemeProvider;

class SchemeServiceTests {

	/** Provider with a fixed list of schemes; ignores the query so the tests cover only the service. */
	private static SchemeService serviceWith(List<Scheme> schemes) {
		return new SchemeService(new SchemeProvider() {
			@Override
			public List<Scheme> search(SchemeQuery query) {
				return schemes;
			}

			@Override
			public Optional<Scheme> findById(String id) {
				return schemes.stream().filter(s -> s.id().equals(id)).findFirst();
			}
		});
	}

	private static Scheme scheme(int n, String category) {
		return new Scheme("id-" + n, "Scheme " + n, "short " + n, "long " + n, SchemeLevel.CENTRAL, null,
				"Ministry", category, List.of(), List.of(), List.of(), List.of(), null);
	}

	private static List<Scheme> schemes(int count) {
		return IntStream.range(0, count).mapToObj(n -> scheme(n, "Cat")).toList();
	}

	@Test
	void pagesResultsAndReportsTotals() {
		SchemeService service = serviceWith(schemes(5));

		PagedResponse<SchemeSummary> second = service.search(SchemeQuery.none(), 1, 2);

		assertThat(second.items()).extracting(SchemeSummary::id).containsExactly("id-2", "id-3");
		assertThat(second.page()).isEqualTo(1);
		assertThat(second.size()).isEqualTo(2);
		assertThat(second.totalItems()).isEqualTo(5);
		assertThat(second.totalPages()).isEqualTo(3);
	}

	@Test
	void lastPageMayBeShort() {
		PagedResponse<SchemeSummary> last = serviceWith(schemes(5)).search(SchemeQuery.none(), 2, 2);

		assertThat(last.items()).extracting(SchemeSummary::id).containsExactly("id-4");
	}

	@Test
	void pageBeyondTheEndIsEmptyNotAnError() {
		PagedResponse<SchemeSummary> beyond = serviceWith(schemes(5)).search(SchemeQuery.none(), 10, 2);

		assertThat(beyond.items()).isEmpty();
		assertThat(beyond.totalItems()).isEqualTo(5);
	}

	@Test
	void hugePageNumberDoesNotOverflow() {
		PagedResponse<SchemeSummary> beyond = serviceWith(schemes(3)).search(SchemeQuery.none(), Integer.MAX_VALUE, 100);

		assertThat(beyond.items()).isEmpty();
	}

	@Test
	void noMatchesGivesEmptyPageWithZeroPages() {
		PagedResponse<SchemeSummary> empty = serviceWith(List.of()).search(SchemeQuery.none(), 0, 20);

		assertThat(empty.items()).isEmpty();
		assertThat(empty.totalItems()).isZero();
		assertThat(empty.totalPages()).isZero();
	}

	@Test
	void rejectsInvalidPaging() {
		SchemeService service = serviceWith(schemes(1));

		assertThatThrownBy(() -> service.search(SchemeQuery.none(), -1, 10)).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> service.search(SchemeQuery.none(), 0, 0)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void getByIdReturnsTheScheme() {
		assertThat(serviceWith(schemes(3)).getById("id-1").name()).isEqualTo("Scheme 1");
	}

	@Test
	void getByIdThrowsWhenMissing() {
		assertThatThrownBy(() -> serviceWith(schemes(1)).getById("nope"))
				.isInstanceOf(SchemeNotFoundException.class)
				.hasMessageContaining("nope");
	}

	@Test
	void categoriesAreDistinctAndSorted() {
		SchemeService service = serviceWith(List.of(scheme(1, "Health"), scheme(2, "agriculture"),
				scheme(3, "Health"), scheme(4, null)));

		assertThat(service.getCategories()).containsExactly("agriculture", "Health");
	}
}
