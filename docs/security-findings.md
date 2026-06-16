# Security Findings – CVE Resolution Log

This log tracks security findings detected during the project, including both
resolved issues (with their fixes) and unresolved issues that are being carried
as accepted risks together with their mitigation decisions.

## SF-001 · Spring Framework DoS vulnerabilities in `spring-core`

| Field | Detail |
|---|---|
| **Date detected** | 2026-06-09 |
| **Detected by** | OWASP Dependency-Check 12.2.2 (CI pipeline) |
| **Affected component** | `org.springframework:spring-core:7.0.7` |
| **Introduced via** | `spring-boot-starter-parent:4.0.6` |
| **Status** | ✅ Resolved |

---

### CVEs

| CVE | CVSS | Description |
|---|---|---|
| `CVE-2026-41842` | 7.5 | Denial of Service via versioned static resources in Spring MVC / WebFlux |
| `CVE-2026-41850` | 7.5 | Algorithmic DoS via user-supplied SpEL expressions |
| `CVE-2026-41851` | 7.5 | DoS via unbounded cache growth triggered by SpEL expressions |

All three are remotely exploitable **Denial of Service** vulnerabilities. CVE-2026-41842 affects
static resource resolution; CVE-2026-41850 and CVE-2026-41851 affect applications that evaluate
user-supplied Spring Expression Language (SpEL) expressions.

---

### Fix applied

Overrode the Spring Framework version in `pom.xml` `<properties>` to pull in the patched release
while keeping the Spring Boot parent at `4.0.6`:

```xml
<!-- pom.xml – <properties> -->
<spring-framework.version>7.0.8</spring-framework.version>
```

`spring-framework 7.0.8` was released on 2026-06-08 and resolves all three CVEs.  
Reference: https://spring.io/blog/2026/06/08/spring-framework-7-0-8-and-6-2-19-available-now/

**Preferred long-term fix:** upgrade `spring-boot-starter-parent` to `4.0.7` (or whichever version
manages `spring-framework:7.0.8` natively) and remove the manual override.

---

### Verification

After applying the fix, confirm the resolved version with:

```bash
mvn dependency:tree | grep spring-core
# expected: org.springframework:spring-core:jar:7.0.8
mvn clean verify
# expected: BUILD SUCCESS – no CVSS ≥ 7 findings
```

---

### Timeline

| Date | Event |
|---|---|
| 2026-06-08 | Spring Framework 7.0.8 released, CVEs published |
| 2026-06-09 | CI pipeline blocked by OWASP Dependency-Check |
| 2026-06-09 | Fix applied via `<spring-framework.version>7.0.8</spring-framework.version>` |


## SF-002 · Absence of application-level authentication and authorization

| Field | Detail |
|---|---|
| **Date recorded** | 2026-06-16 |
| **Detected by** | Manual review (architecture / requirements) |
| **Affected area** | Entire Web UI and HTTP API |
| **Status** | ⚠ Accepted risk (unresolved) |

---

### Description

The current version of the application does not implement application-level:

- User login or authentication
- Authorization / role-based access control
- Access control lists (ACL) on protected operations or resources

Any user who can reach the Web UI or API endpoints can access functionalities
that, in a production system, would typically require an authenticated identity
and specific permissions.

### Impact

- The system cannot distinguish between different users or roles.
- Operations that should be restricted (for example, creating or modifying
  domain data) can be triggered by any party with network access to the
  application.
- In a production context, this would represent a high-severity weakness.

### Decision

- **Decision type:** Accepted risk
- **Reason:** Implementing full authentication and authorization is outside the
  current delivery scope and cannot be completed within the available time and
  constraints.
- **Status:** The limitation is explicitly acknowledged and tracked as an
  unresolved security issue, not ignored or considered fixed.
- **Owner:** Project team / product owner for this delivery.

### Compensating controls

To reduce the impact of this limitation in the current environment:

- The application is deployed only in a controlled environment, not as a
  publicly exposed Internet service.
- Access to the deployment may be further restricted at infrastructure level
  (for example, reverse proxy rules, HTTP Basic Authentication, or network/IP
  restrictions), as documented in the deployment configuration.
- The system is used with non-production data for demonstration and evaluation
  purposes.

These measures reduce exposure but do **not** replace proper, in-application
authentication and authorization.

### Review conditions

This accepted risk must be revisited if:

- The application is exposed to a broader audience or less controlled network,
- Real user accounts or personal/sensitive data are introduced, or
- The project scope is extended to include identity and access management.

At that point, designing and implementing authentication, authorization, and
access control mechanisms becomes a required remediation step rather than an
accepted limitation.