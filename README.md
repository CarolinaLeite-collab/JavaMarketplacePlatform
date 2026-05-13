# MiteLovers

A second-hand book and magazine marketplace built with Java and Spring Boot, developed as part of the SwitchDev LABPROJ 25/26 programme.

---

## Prerequisites

- Java 21
- Maven 3.8+

---

## Build and Test

To compile, run all tests and package the application:

```bash
mvn clean install
```

To run tests only:

```bash
mvn clean test
```

To run the application locally:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8081`.

The H2 console is available at `http://localhost:8081/h2-console` with the following settings:
- JDBC URL: `jdbc:h2:mem:miteloversdb`
- Username: `sa`
- Password: *(leave blank)*

---

## CI Pipeline

The project uses GitHub Actions for continuous integration.

The pipeline triggers automatically on:
- Every push to `main`, `b3`, or `b4`
- Every pull request targeting `main`, `b3`, or `b4`

On each trigger, the pipeline runs `mvn clean install` which compiles the code, executes all tests, enforces the coverage threshold, and packages the application.

---

## Test Coverage

JaCoCo is configured to enforce a minimum instruction coverage of **95%**.

The build will fail automatically if coverage drops below this threshold, blocking any non-compliant code from being integrated.

To generate the coverage report locally:

```bash
mvn clean install
```

The HTML report is available at `target/site/jacoco/index.html`.

---

## Active Profile

The application uses the `jpa` Spring profile by default, which activates the JPA repository implementations backed by an H2 in-memory database.

---

## DevSecOps Workflow

### Branching Strategy
- `b3` and `b4` are the main integration branches
- Feature branches are created from `b3` or `b4` depending on the work assigned to each team member
- All changes are integrated via Pull Requests
- Direct pushes to `b3` and `b4` are reserved for exceptional cases
- Direct pushes to `main` are not allowed


### Development Flow
1. Create a feature branch from `b3` or `b4`
2. Develop and commit following the convention: `[feat|fix|refactor|docs|config]: description closes #issue`
3. Open a Pull Request targeting `b3` or `b4`
4. CI pipeline runs automatically — all checks must pass
5. Code is reviewed and merged

### Quality Gates
- All tests must pass
- JaCoCo instruction coverage must be ≥ 95%
- Build must succeed with `mvn clean install`
