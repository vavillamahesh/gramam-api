# Changelog

All notable changes are recorded here. Every PR adds an entry under **Unreleased**.

## [Unreleased]

### Added
- **Schemes API (mock data)**: `GET /api/v1/schemes` (filters `level`, `state`, `category`, `q`; paging `page`, `size`), `GET /api/v1/schemes/{id}` and `GET /api/v1/schemes/categories`.
  - Layers: `SchemeController` (HTTP and parameter validation), `SchemeService` (paging, not-found, categories) and `SchemeProvider` (data source interface) under `com.gramam.api.schemes`.
  - `MockSchemeProvider` serves 12 sample schemes (8 central, 4 state: Karnataka, Tamil Nadu, Maharashtra, Telangana). It is selected by `gramam.schemes.provider: mock` (the default). The data is illustrative and not authoritative.
  - A `state` filter returns that state's schemes plus central schemes; combine with `level=STATE` for the state's own schemes only.
  - Models: `Scheme` (detail), `SchemeSummary` (list item), `SchemeQuery` (filters; blank values are ignored), `PagedResponse<T>`.
  - `ApiExceptionHandler` returns RFC 9457 problem details: 404 for an unknown scheme id, 400 for an invalid `level` or out-of-range paging (`page` < 0, `size` outside 1-100).
  - README: endpoint reference and how to add the MyScheme provider.
  - Tests (32 new): provider filters and sorting, service paging edge cases (past the end, integer overflow, empty), controller parameters and error responses, and end-to-end MockMvc tests.
- Initial Spring Boot 4.1.2 service on Java 25 (LTS), built with Maven.
- Dependencies: `spring-boot-starter-webmvc`, `spring-boot-starter-validation`, `spring-boot-starter-actuator`.
- `application.yml`: virtual threads enabled, graceful shutdown, `PORT` env override (default 8080), actuator `health` and `info` exposed with liveness/readiness probes.
- Tests: application context load, and `/actuator/health` plus liveness/readiness probes report `UP`.
- README with run, test and build instructions.
