# Security Findings – CVE Resolution Log

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