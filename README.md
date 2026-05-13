# MiteLovers

A second-hand book and magazine marketplace built with Java and Spring Boot, developed as part of the SwitchDev LABPROJ 25/26 programme.

---

## Prerequisites

- Java 21
- Maven 3.8+

---

## Table of contents

<!-- TOC -->
* [MiteLovers](#mitelovers)
  * [Prerequisites](#prerequisites)
  * [Table of contents](#table-of-contents)
  * [Build, Test and Run](#build-test-and-run)
  * [DevSecOps Workflow](#devsecops-workflow)
    * [Branching Strategy](#branching-strategy)
    * [Development Flow](#development-flow)
    * [Quality Gates](#quality-gates)
  * [Test Coverage](#test-coverage)
  * [CI Pipeline](#ci-pipeline)
    * [Notify Discord on PR Creation](#notify-discord-on-pr-creation)
    * [Notify Discord on PR Merge](#notify-discord-on-pr-merge)
    * [Run Tests on Pull Request](#run-tests-on-pull-request)
  * [SpringBoot Active Profile](#springboot-active-profile)
  * [application.properties](#applicationproperties)
<!-- TOC -->

___


## Build, Test and Run

To compile, build and run all tests of the application:

```bash
mvn clean verify
```

To run tests only:

```bash
mvn clean test
```

To run the application locally, there are two options:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mem
```

- starts the app using an in-memory profile, meaning the database lives only in RAM and is wiped clean every time the app stops.

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=jpa,bootstrap
```

- starts the app with two profiles: jpa for file/persistent database configuration, and bootstrap to seed initial data on startup. Data survives restarts.

The application will start on `http://localhost:8081`.

The H2 console is available at `http://localhost:8081/h2-console` with the following settings:
- JDBC URL: `jdbc:h2:mem:miteloversdb`
- Username: `sa`
- Password: *(leave blank)*

---

## DevSecOps Workflow

### Branching Strategy
- `b3` and `b4` are the main integration branches;
- Feature branches are created from `b3` or `b4` depending on the work assigned to each team member;
- All changes are integrated via Pull Requests;
- Direct pushes to `b3` and `b4` are reserved for exceptional cases;
- Direct pushes to `main` are not allowed.


### Development Flow
1. Create a feature branch from `b3` or `b4`;
2. Develop and commit following the convention: `[feat|fix|refactor|docs|config]: description closes #issue`;
3. Open a Pull Request targeting `b3` or `b4`;
4. CI pipeline runs automatically — all checks must pass;
5. Code is reviewed and merged.

### Quality Gates
- All tests must pass;
- Build must succeed with `mvn clean verify`.
- JaCoCo instruction coverage must be ≥ 95%;

---

## Test Coverage

It was decided to link the coverage threshold to the `mvn verify` stage, with JaCoCo configured to enforce a minimum instruction coverage of **95%**.

For that we updated the JaCoCo plugin in the pom.xml:

```xml
<plugins>
    <!-- JaCoCo (coverage) -->
    <plugin>
        <groupId>org.jacoco</groupId>
        <artifactId>jacoco-maven-plugin</artifactId>
        <version>${jacoco.version}</version>
        <executions>
            <execution>
                <goals>
                    <goal>prepare-agent</goal>
                </goals>
            </execution>
            <execution>
                <id>report</id>
                <goals>
                    <goal>report</goal>
                </goals>
            </execution>
            <!-- Verifies that coverage is higher than 95% -->
            <execution>
                <id>check-coverage</id>
                <phase>verify</phase>
                <goals><goal>check</goal></goals>
                <configuration>
                    <rules>
                        <rule>
                            <element>BUNDLE</element>
                            <limits>
                                <limit>
                                    <counter>LINE</counter>
                                    <value>COVEREDRATIO</value>
                                    <minimum>0.95</minimum>
                                </limit>
                            </limits>
                        </rule>
                    </rules>
                </configuration>
            </execution>
        </executions>
    </plugin>
</plugins>
```

This sets several goals:

- `<phase>verify</phase>` binds this execution to the `verify` phase, so it runs and enforces the coverage whenever `mvn verify` is run;
- `<goal>check</goal>` tells JaCoCo to check coverage against the rules;
- `<counter>LINE</counter>` measures line coverage;
- `<value>COVEREDRATIO</value>` uses ratio (0.0 to 1.0);
- `<minimum>0.95</minimum>` enforces 95% minimum, failing the build if coverage drops below that.

The build will fail automatically if coverage drops below this threshold, blocking any non-compliant code from being integrated.

To generate the coverage report locally:

```bash
mvn clean verify
```

The HTML report is available at `target/site/jacoco/index.html`.

___

## CI Pipeline

The project uses GitHub Actions for continuous integration.

The pipeline triggers automatically on:
- Every push to `main`, `b3`, or `b4`
- Every pull request targeting `main`, `b3`, or `b4`

For that, we defined three different GitHub worflows:

___

### Notify Discord on PR Creation

This workflow sends an automated message triggered by any pull request to the teams Discord channel. 

The objective is to notify the existence of a new PR, so that the team can assingn reviewers to it:

```yaml
name: Notify Discord on PR Creation

on:
  pull_request:
    types: [opened, reopened]

jobs:
  notify:
    runs-on: ubuntu-latest

    steps:
      - name: Send message to Discord
        run: |
          curl -H "Content-Type: application/json" \
          -d "$(jq -n \
            --arg title "${{ github.event.pull_request.title }}" \
            --arg user "${{ github.event.pull_request.user.login }}" \
            --arg url "${{ github.event.pull_request.html_url }}" \
            --arg number "${{ github.event.pull_request.number }}" \
            --arg base "${{ github.event.pull_request.base.ref }}" \
            '{
              content: (
                "📢 New Pull Request #" + $number + "to merge into " + $base  "\n" +
                "🏷️ " + $title + "\n" +
                "👤 " + $user + "\n" +
                "🌐 " + $url
              )
            }')" \
          ${{ secrets.DISCORD_INCOMING_PR_WEBHOOK }}
```

### Notify Discord on PR Merge

This GitHub workflow has a complementary action to the first one. It is triggered whenever a Pull Request is merged on a certain branch and notifies the team to integrate these new changes in their local working branches:

```yaml
name: Notify Discord on PR Merge

on:
  pull_request:
    types: [closed]

jobs:
  notify:
    if: github.event.pull_request.merged == true
    runs-on: ubuntu-latest


    steps:
      - name: Send message to Discord
        run: |
          curl -H "Content-Type: application/json" \
          -d "$(jq -n \
            --arg title "${{ github.event.pull_request.title }}" \
            --arg user "${{ github.event.pull_request.user.login }}" \
            --arg url "${{ github.event.pull_request.html_url }}" \
            --arg number "${{ github.event.pull_request.number }}" \
            --arg base "${{ github.event.pull_request.base.ref }}" \
            '{
              content: (
                "🚀 PR #" + $number + " merged to " + $base + "\n" +
                "🏷️ " + $title + "\n" +
                "👤 " + $user + "\n" +
                "🌐 " + $url + "\n\n" +
                "🚨 Please update your branches!"
              )
            }')" \
          ${{ secrets.DISCORD_WEBHOOK }}
```

### Run Tests on Pull Request

On each Pull Request trigger, the pipeline runs `mvn clean verify` which compiles the code, executes all tests and enforces the test line coverage threshold.

To follow good DevOps practices, it also archives the JaCoCo coverage report in HTML format. 

To fishing, another step was established, to post a coverage comment on the Pull Request itself, with invaluable data such as the line coverage per code class and the impact the worked-on classes had on the overall project's line coverage:

![post-coverage-comment.png](docs/readme-printscreens/post-coverage-comment.png)

The project's GitHub workflow:

```yaml
name: Run Tests on Pull Request

on:
  pull_request:
    branches: [ main, b3, b4 ]

env:
  FORCE_JAVASCRIPT_ACTIONS_TO_NODE24: true

permissions:
  contents: read
  pull-requests: write

jobs:
  build-and-test-with-coverage:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v4.2.2
      - name: Set up JDK 21
        uses: actions/setup-java@v4.7.0
        with:
          distribution: temurin
          java-version: '21'
          cache: maven
      - name: Build and run unit tests with coverage
        run: mvn clean verify
      - name: Upload coverage report
        if: always()
        uses: actions/upload-artifact@v4.6.2
        with:
          name: jacoco-report
          path: target/site/jacoco/
      - name: Post coverage comment
        if: always()
        uses: madrapps/jacoco-report@v1.7.1
        with:
          paths: ${{ github.workspace }}/target/site/jacoco/jacoco.xml
          token: ${{ secrets.GITHUB_TOKEN }}
          min-coverage-overall: 0
          min-coverage-changed-files: 0
```

Besides the steps already covered, we created an environment variable `FORCE_JAVASCRIPT_ACTIONS_TO_NODE24`, that forces all JavaScript-based actions (checkout, setup-java, upload-artifact) to run on Node.js 24 instead of the deprecated Node.js 20, removing the deprecation warning.

Additionally, a permissions block was added:

- `contents`: read — allows the workflow to clone/read the repository contents;
- `pull-requests`: write — allows the workflow to post comments on the PR (needed for the JaCoCo coverage comment).

---

## SpringBoot application.properties

The `application.properties` file is the central configuration file for a Spring Boot application, where settings like database connections, server port, and framework behavior can be defined:

```bash
# H2
spring.datasource.url=jdbc:h2:file:./data/miteloversdb;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false

# Active Profile
spring.profiles.active=bootstrap,jpa

# Server
server.port=8081
```
This Spring Boot configuration sets up an H2 file-based database stored at `./data/miteloversdb`, accessible via the built-in H2 console at /h2-console using the default credentials. 

Hibernate manages the schema automatically with `ddl-auto=update`, keeping it in sync with the entity classes without ever dropping data, while SQL logging is enabled for debugging purposes. 

The `open-in-view=false` setting ensures database sessions are properly scoped to the service layer. 

Two profiles are active: `bootstrap` and `jpa`, handling data seeding (through a `DataInitializer` class) and JPA configuration (All the Java Persistence API repos, that were established as the active profile) respectively. 

The application runs on port 8081.