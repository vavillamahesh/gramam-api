# gramam-api

Backend service for Gramam (companion to [gramam-ui](https://github.com/Pradhyumna18/gramam-ui)).

| | |
|---|---|
| Language | Java 25 (LTS) |
| Framework | Spring Boot 4.1.2 (Spring Framework 7) |
| Build | Maven 3.9+ |
| Package | `com.gramam.api` |

## Prerequisites

JDK 25 (e.g. [Temurin 25](https://adoptium.net/temurin/releases/?version=25)) and Maven 3.9+. Check with `java -version` and `mvn -v`.

## Run

```bash
mvn spring-boot:run          # starts on http://localhost:8080 (override with PORT=9000)
```

Health checks:

- `GET /actuator/health`
- `GET /actuator/health/liveness`
- `GET /actuator/health/readiness`

## Test and build

```bash
mvn verify                   # compiles, runs unit tests, builds target/gramam-api-*.jar
java -jar target/gramam-api-0.0.1-SNAPSHOT.jar
```

## Schemes API

Government schemes (central and state). Backed by built-in **mock data** for now; the MyScheme
integration will replace it without changing the endpoints.

| Endpoint | Description |
|---|---|
| `GET /api/v1/schemes` | List schemes, paged. Filters: `level` (`CENTRAL` or `STATE`, upper case), `state`, `category`, `q` (free text). Paging: `page` (from 0, default 0), `size` (1-100, default 20). |
| `GET /api/v1/schemes/{id}` | Full scheme detail: description, eligibility, benefits, documents required, application link. |
| `GET /api/v1/schemes/categories` | Distinct categories, for filter dropdowns. |

Notes:

- `state=Karnataka` returns Karnataka's schemes **and** central schemes (they apply everywhere). Add `level=STATE` to get only the state's own schemes.
- Errors are RFC 9457 problem details: 404 for an unknown id, 400 for a bad `level` or out-of-range paging.
- The sample data is illustrative, not authoritative. Do not show it to end users as official information.

```bash
curl 'localhost:8080/api/v1/schemes?state=Karnataka&size=5'
curl  localhost:8080/api/v1/schemes/pm-kisan
```

### Layers and swapping in MyScheme

```
SchemeController  ->  SchemeService  ->  SchemeProvider (interface)
  HTTP, validation      paging, 404,        MockSchemeProvider   (gramam.schemes.provider=mock)
                        categories          MySchemeProvider     (future)
```

To add MyScheme: implement `SchemeProvider`, annotate it with
`@ConditionalOnProperty(prefix = "gramam.schemes", name = "provider", havingValue = "myscheme")`,
map the API response into `Scheme`, and set `gramam.schemes.provider: myscheme`.

## Layout

```
src/main/java/com/gramam/api/
  schemes/{controller,service,provider,model,dto,exception}   schemes feature
  common/web/                                                  shared error handling
src/main/resources/application.yml
src/test/java/com/gramam/api/                                  tests (mirrors main)
```

## Conventions

- Every PR adds a detailed entry to [CHANGELOG.md](CHANGELOG.md).
- New behaviour ships with unit tests.
