package com.gramam.api.schemes.provider;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.gramam.api.schemes.model.Scheme;
import com.gramam.api.schemes.model.SchemeLevel;
import com.gramam.api.schemes.model.SchemeQuery;

/**
 * In-memory sample data used until the MyScheme integration lands. Active when
 * {@code gramam.schemes.provider=mock} (the default).
 *
 * <p>The content is illustrative for development and UI work. It is not authoritative: amounts,
 * eligibility and links must come from the official source once the real provider exists.
 */
@Component
@ConditionalOnProperty(prefix = "gramam.schemes", name = "provider", havingValue = "mock", matchIfMissing = true)
public class MockSchemeProvider implements SchemeProvider {

	private static final String MYSCHEME = "https://www.myscheme.gov.in";

	private static final List<Scheme> SCHEMES = List.of(
			new Scheme(
					"pm-kisan",
					"PM-KISAN (Pradhan Mantri Kisan Samman Nidhi)",
					"Income support of Rs 6,000 a year for farmer families.",
					"Direct income support to landholding farmer families, paid in three equal instalments "
							+ "of Rs 2,000 every four months into the beneficiary's bank account.",
					SchemeLevel.CENTRAL, null,
					"Ministry of Agriculture and Farmers Welfare",
					"Agriculture",
					List.of("farmer", "income support", "dbt"),
					List.of("Farmer family owning cultivable land",
							"Land records in the applicant's name",
							"Bank account linked with Aadhaar"),
					List.of("Rs 6,000 per year in three instalments of Rs 2,000"),
					List.of("Aadhaar card", "Land ownership records", "Bank account details"),
					"https://pmkisan.gov.in"),
			new Scheme(
					"ayushman-bharat-pmjay",
					"Ayushman Bharat PM-JAY",
					"Health cover of Rs 5 lakh per family per year for hospital care.",
					"Cashless secondary and tertiary hospital treatment at empanelled public and private "
							+ "hospitals for eligible families.",
					SchemeLevel.CENTRAL, null,
					"Ministry of Health and Family Welfare",
					"Health",
					List.of("health insurance", "hospital", "cashless"),
					List.of("Family identified in the SECC database or covered by a state expansion",
							"No age limit or family size cap"),
					List.of("Cover of Rs 5 lakh per family per year", "Cashless treatment at empanelled hospitals"),
					List.of("Aadhaar card", "Ration card", "Family identification proof"),
					"https://pmjay.gov.in"),
			new Scheme(
					"pmay-gramin",
					"Pradhan Mantri Awaas Yojana - Gramin (PMAY-G)",
					"Financial help to build a pucca house with basic amenities in rural areas.",
					"Assistance for rural households that are houseless or live in kutcha or dilapidated "
							+ "houses to build a pucca house, with support for a toilet and a LPG connection.",
					SchemeLevel.CENTRAL, null,
					"Ministry of Rural Development",
					"Housing",
					List.of("housing", "rural", "pucca house"),
					List.of("Houseless or living in a kutcha or dilapidated house",
							"Selected through the Awaas+ / SECC priority list",
							"No existing pucca house in the family"),
					List.of("Unit assistance for house construction", "Convergence with MGNREGA wages and toilet support"),
					List.of("Aadhaar card", "Job card (MGNREGA)", "Bank account details"),
					"https://pmayg.nic.in"),
			new Scheme(
					"mgnrega",
					"MGNREGA (Mahatma Gandhi National Rural Employment Guarantee Act)",
					"Guaranteed 100 days of wage employment a year for rural households.",
					"Legal guarantee of at least 100 days of unskilled manual work per financial year to every "
							+ "rural household whose adult members volunteer for it.",
					SchemeLevel.CENTRAL, null,
					"Ministry of Rural Development",
					"Employment",
					List.of("employment", "wages", "rural", "job card"),
					List.of("Adult member of a rural household",
							"Willing to do unskilled manual work",
							"Registered with the Gram Panchayat for a job card"),
					List.of("Up to 100 days of wage employment per household per year", "Wages paid to the bank account"),
					List.of("Passport photo", "Aadhaar card", "Bank account details"),
					"https://nrega.dord.gov.in"),
			new Scheme(
					"pm-ujjwala-yojana",
					"Pradhan Mantri Ujjwala Yojana (PMUY)",
					"Free LPG connection for women from poor households.",
					"Provides a deposit-free LPG connection to adult women from eligible poor households to "
							+ "replace firewood and other polluting cooking fuels.",
					SchemeLevel.CENTRAL, null,
					"Ministry of Petroleum and Natural Gas",
					"Women & Child",
					List.of("lpg", "women", "clean fuel"),
					List.of("Adult woman", "Member of a BPL or other eligible household",
							"No existing LPG connection in the household"),
					List.of("Deposit-free LPG connection", "Support for the first refill and stove in some cases"),
					List.of("BPL / ration card", "Aadhaar card", "Bank account details"),
					"https://www.pmuy.gov.in"),
			new Scheme(
					"pmfby",
					"Pradhan Mantri Fasal Bima Yojana (PMFBY)",
					"Crop insurance against yield loss from natural calamities, pests and disease.",
					"Insurance cover for notified crops at low farmer-paid premiums, with the balance "
							+ "shared between the Centre and the state.",
					SchemeLevel.CENTRAL, null,
					"Ministry of Agriculture and Farmers Welfare",
					"Agriculture",
					List.of("crop insurance", "farmer", "kharif", "rabi"),
					List.of("Farmers growing notified crops in notified areas",
							"Loanee and non-loanee farmers, including sharecroppers and tenants"),
					List.of("Insurance cover for crop loss", "Low fixed premium for food and oilseed crops"),
					List.of("Aadhaar card", "Land records or tenancy proof", "Bank account details", "Sowing declaration"),
					"https://pmfby.gov.in"),
			new Scheme(
					"sukanya-samriddhi-yojana",
					"Sukanya Samriddhi Yojana",
					"Small savings scheme with a government-set interest rate for a girl child's future.",
					"A savings account for the education and marriage expenses of a girl child, opened by a "
							+ "parent or guardian.",
					SchemeLevel.CENTRAL, null,
					"Ministry of Finance",
					"Women & Child",
					List.of("savings", "girl child", "post office"),
					List.of("Girl child below 10 years of age", "Opened by a parent or legal guardian",
							"One account per girl child"),
					List.of("Government-notified interest rate", "Tax benefits under the applicable rules"),
					List.of("Girl child's birth certificate", "Guardian's ID and address proof"),
					MYSCHEME),
			new Scheme(
					"atal-pension-yojana",
					"Atal Pension Yojana (APY)",
					"Guaranteed monthly pension from age 60 for workers in the unorganised sector.",
					"A pension scheme that pays a fixed monthly amount from age 60, depending on the "
							+ "contribution chosen at enrolment.",
					SchemeLevel.CENTRAL, null,
					"Ministry of Finance",
					"Social Security",
					List.of("pension", "unorganised sector", "savings"),
					List.of("Indian citizen aged 18 to 40", "Has a savings bank account",
							"Not an income-tax payer"),
					List.of("Monthly pension of Rs 1,000 to Rs 5,000 from age 60", "Pension continues to the spouse"),
					List.of("Aadhaar card", "Bank account details", "Mobile number"),
					MYSCHEME),
			new Scheme(
					"karnataka-gruha-lakshmi",
					"Gruha Lakshmi",
					"Monthly assistance of Rs 2,000 to the woman head of the family.",
					"Karnataka government scheme that supports the woman head of an eligible household with "
							+ "a monthly cash transfer.",
					SchemeLevel.STATE, "Karnataka",
					"Department of Women and Child Development, Karnataka",
					"Women & Child",
					List.of("women", "monthly assistance", "dbt"),
					List.of("Woman head of an eligible family", "Resident of Karnataka",
							"Holds a ration card (BPL, APL or Antyodaya)"),
					List.of("Rs 2,000 per month"),
					List.of("Ration card", "Aadhaar card", "Bank account details"),
					"https://sevasindhu.karnataka.gov.in"),
			new Scheme(
					"tn-kalaignar-magalir-urimai-thogai",
					"Kalaignar Magalir Urimai Thogai",
					"Monthly assistance of Rs 1,000 for women heads of eligible families.",
					"Tamil Nadu government scheme providing a monthly rights grant to women heads of "
							+ "eligible families.",
					SchemeLevel.STATE, "Tamil Nadu",
					"Department of Social Welfare, Tamil Nadu",
					"Women & Child",
					List.of("women", "monthly assistance", "dbt"),
					List.of("Woman head of an eligible family", "Resident of Tamil Nadu",
							"Family income within the prescribed limit"),
					List.of("Rs 1,000 per month"),
					List.of("Ration card", "Aadhaar card", "Bank account details"),
					"https://www.tn.gov.in"),
			new Scheme(
					"maharashtra-mazi-ladki-bahin",
					"Mazi Ladki Bahin Yojana",
					"Monthly assistance of Rs 1,500 for eligible women in Maharashtra.",
					"Maharashtra government scheme giving eligible women a monthly cash transfer to improve "
							+ "their financial independence and health.",
					SchemeLevel.STATE, "Maharashtra",
					"Women and Child Development Department, Maharashtra",
					"Women & Child",
					List.of("women", "monthly assistance", "dbt"),
					List.of("Woman resident of Maharashtra within the notified age band",
							"Family income within the prescribed limit",
							"Bank account linked with Aadhaar"),
					List.of("Rs 1,500 per month"),
					List.of("Aadhaar card", "Domicile or ration card", "Bank account details", "Income proof"),
					"https://www.maharashtra.gov.in"),
			new Scheme(
					"telangana-aarogyasri",
					"Aarogyasri",
					"Cashless hospital treatment for eligible families in Telangana.",
					"Telangana government health scheme giving eligible families cashless treatment for "
							+ "listed procedures at network hospitals.",
					SchemeLevel.STATE, "Telangana",
					"Health, Medical and Family Welfare Department, Telangana",
					"Health",
					List.of("health insurance", "hospital", "cashless"),
					List.of("Resident of Telangana", "Holds a white ration card"),
					List.of("Cashless treatment up to Rs 10 lakh per family per year"),
					List.of("White ration card", "Aadhaar card"),
					"https://www.telangana.gov.in"));

	private static final Comparator<Scheme> BY_NAME = Comparator.comparing(Scheme::name, String.CASE_INSENSITIVE_ORDER);

	@Override
	public List<Scheme> search(SchemeQuery query) {
		return SCHEMES.stream()
				.filter(scheme -> matchesLevel(scheme, query))
				.filter(scheme -> matchesState(scheme, query))
				.filter(scheme -> matchesCategory(scheme, query))
				.filter(scheme -> matchesText(scheme, query))
				.sorted(BY_NAME)
				.toList();
	}

	@Override
	public Optional<Scheme> findById(String id) {
		return SCHEMES.stream().filter(scheme -> scheme.id().equals(id)).findFirst();
	}

	private static boolean matchesLevel(Scheme scheme, SchemeQuery query) {
		return query.level() == null || scheme.level() == query.level();
	}

	/** Central schemes apply in every state, so they pass any state filter. */
	private static boolean matchesState(Scheme scheme, SchemeQuery query) {
		return query.state() == null
				|| scheme.level() == SchemeLevel.CENTRAL
				|| query.state().equalsIgnoreCase(scheme.state());
	}

	private static boolean matchesCategory(Scheme scheme, SchemeQuery query) {
		return query.category() == null || query.category().equalsIgnoreCase(scheme.category());
	}

	private static boolean matchesText(Scheme scheme, SchemeQuery query) {
		if (query.text() == null) {
			return true;
		}
		String needle = query.text().toLowerCase(Locale.ROOT);
		return contains(scheme.name(), needle)
				|| contains(scheme.shortDescription(), needle)
				|| contains(scheme.description(), needle)
				|| contains(scheme.ministry(), needle)
				|| scheme.tags().stream().anyMatch(tag -> contains(tag, needle));
	}

	private static boolean contains(String haystack, String lowerCaseNeedle) {
		return haystack != null && haystack.toLowerCase(Locale.ROOT).contains(lowerCaseNeedle);
	}
}
