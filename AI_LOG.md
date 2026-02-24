# AI Operation Log

Key actions I performed
- I consulted the AI (Gemini) for storage and geo-index recommendations and chose PostgreSQL (+ PostGIS) for spatial accuracy and scalability; I did not implement Redis/geohash and instead relied on PostGIS for production-grade nearby queries.
- I consulted Gemini about designing the domain model (Person, Location) and mapped it to a DDD-style project layout and OpenAPI specification (openapi.yaml).
- I used Gemini about the working flow of the three endpoints, aligned with AI step by step, ensuring the correct logic implementation.
- I used Gemini to implement the three endpoints as the openai.yaml specification and aligned correct working flow.
- I used Gemini to add input validation and prompt-framing to mitigate prompt-injection risks before calling the external Gemini API.
- I used Gemini to implement asynchronous bio generation: persist Person quickly with a default bio, and update the bio later via a Spring Event worker to avoid blocking the create request.
- I used Gemini to harden DB access: introduced a lower-privileged DB user (`pfdbuser`) and moved away from using the original superuser for normal app runtime.
- I used Gemini to integrate OpenAPI/Swagger for convenient testing and to keep endpoints aligned with the spec.
- I used Gemini to complete unit tests for `GeminiService`, `InputValidator`, and endpoint tests to ensure correctness of changes.
- I used Gemini to help write the `Dockerfile` and `docker-compose.yml`; the initial Docker setup failed, so I adjusted `init.sql` and made it work in my local Docker environment.

Important design decisions and why I chose them
- PostgreSQL + PostGIS as primary datastore: chosen for accurate geographic queries and strong consistency guarantees. This simplifies spatial queries (ST_DWithin, indexes) and reduces complexity when accuracy matters.
  - Trade-off: Slightly higher latency compared to an in-memory index for some nearby queries, but much stronger correctness and easier maintenance.
- Asynchronous bio generation with Spring Events: prevents slow third-party AI calls from blocking write throughput and improves perceived latency for client requests.
  - Performance impact: Create/person endpoint latency stays low; background tasks consume extra CPU/IO and require failure/retry handling.
- Input validation + prompt framing for AI calls: reduces prompt-injection and limits the chance of sensitive data leaking to the external model.
  - Security impact: Lower risk at the cost of rejecting some borderline inputs and extra validation logic.
- Schema design decisions: explicit `geom` field for Location, not-null enforcement, and spatial index to speed nearby queries.
  - Practical impact: Enforces correct geo-data and enables efficient spatial queries, but requires accurate DB migrations and PostGIS availability.

Issues encountered & how they affected design
- Hobbies column type / converter mismatch:
  - Symptom: Hibernate schema-validation reported the `hobbies` column as a PostgreSQL array type (_text / Types#ARRAY) while JPA expected varchar(255).
  - Cause: A custom `StringListConverter` (JPA AttributeConverter) intended to convert List<String> to a DB string column didn't take effect (likely due to mismatched column mapping or conflicting Hibernate Types configuration).
  - Impact: Schema-validation failed at startup and caused runtime exceptions; required adjusting entity mapping (ensure @Convert usage, correct column type) or using a proven library mapping (hibernate-types) consistently.
- ClassCastException when persisting array/collection:
  - Symptom: java.lang.ClassCastException: ArrayList cannot be cast to Object[] originating from hibernate-types array utilities.
  - Cause: Mixing JPA AttributeConverter (which writes a single String) with hibernate-types' custom ARRAY type (which expects Java arrays like String[]) leads to conflicting in-memory representations.
  - Fix direction: Choose one approach consistently: either
    - Use a single VARCHAR column and persist List<String> via a JPA AttributeConverter (CSV/JSON text), or
    - Use PostgreSQL native array column and map it with hibernate-types (and use String[] in the entity), but do not convert to List in-place.
  - Performance impact: Native arrays can be indexed/queried more efficiently for some operations, but complexity rises if conversion is inconsistent.
- equals()/hashCode() changes on `PersonEntity`:
  - Reasoning: Overrode equals/hashCode to rely on a stable business key or identifier rather than Kotlin data-class default behavior to avoid identity issues when entities are proxied by Hibernate, used in Sets, or mutated during persistence.
  - Impact: Prevents subtle bugs when Hibernate returns proxy objects or when collections containing entities are compared/hashed; important for correctness but must be compatible with JPA best practices (avoid using mutable fields).

Decisions that materially affect performance (summary)
- Use of asynchronous events for slow AI calls: reduces create endpoint latency from potentially seconds to milliseconds; increases background processing load and requires a retry/backoff strategy for stability.
- Choosing PostGIS for spatial correctness and scalability: slightly higher per-query cost but avoids incorrect results and supports robust spatial indexing.
- Choice between storing `hobbies` as VARCHAR (converted JSON/CSV) vs PostgreSQL array: affects memory layout, JDBC driver handling, and serialization overhead; prefer a single consistent approach for predictable performance.

Open items & recommended follow-ups
- Resolve `hobbies` mapping: pick one consistent mapping approach (JPA AttributeConverter -> single text column OR hibernate-types -> native array) and migrate DB schema accordingly.
- Add retry and error-handling to the asynchronous bio updater; instrument with metrics to monitor success/fail rates and latency.
- Harden input validation rules and record rejected attempts for analysis.
- Add integration tests for spatial queries (nearby) against a test PostGIS instance and unit tests for the hobbies converter.
- Add DB migration scripts (Flyway/Liquibase) to make schema changes reproducible (including PostGIS setup).
