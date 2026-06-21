package MITELOVERS.security;

import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OWASP A09:2025 — Security Logging and Alerting Failures
 *
 * Documents the absence of structured security event logging. The application
 * does not configure a dedicated security or audit logger, and no property in
 * {@code application.properties} directs security-relevant events to a separate
 * log sink. As a result, events such as unauthenticated access attempts, invalid
 * identity claims, and resource enumeration are either silently swallowed or
 * mixed into the general application log without structured context. This makes
 * post-incident forensics impractical and real-time alerting impossible.
 *
 * <p><strong>Accepted-Risk Documentation:</strong> These tests intentionally pass
 * because the conditions they describe exist in the current codebase. They do not
 * assert that security logging controls are in place; rather, they serve as a formal,
 * traceable record of risks acknowledged by the team. Implementing a structured
 * security audit logger — for example, a dedicated Logback appender writing to a
 * SIEM-ready JSON sink — is outside the scope of this sprint and is tracked as a
 * separate backlog item.</p>
 */
@Tag("security")
class SecurityLoggingSecurityTest {

    private Properties loadProperties(Path file) throws Exception {
        Properties props = new Properties();
        try (var reader = Files.newBufferedReader(file)) {
            props.load(reader);
        }
        return props;
    }

    @Test
    @DisplayName("OWASP A09 Security Logging: no application-level security log level is configured — security events are not separately monitored")
    void noSecuritySpecificLogLevel_isConfigured_inApplicationProperties() throws Exception {
        Properties props = loadProperties(Paths.get("src/main/resources/application.properties"));

        assertNull(props.getProperty("logging.level.MITELOVERS.security"),
                "logging.level.MITELOVERS.security is not configured — security-relevant " +
                "events in the application's own security package generate no dedicated log output.");
        assertNull(props.getProperty("logging.level.org.springframework.security"),
                "logging.level.org.springframework.security is not configured — Spring Security " +
                "events such as access-denied decisions and filter-chain activity are not surfaced.");
    }

    @Test
    @DisplayName("OWASP A09 Security Logging: no dedicated security audit logger is registered in the logging context")
    void noSecurityAuditLogger_isRegistered_inLogbackContext() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // context.exists() returns non-null only when the named logger has been explicitly
        // accessed or configured; null confirms it was never set up
        ch.qos.logback.classic.Logger auditLogger =
                context.exists("MITELOVERS.SECURITY_AUDIT");

        assertNull(auditLogger,
                "No MITELOVERS.SECURITY_AUDIT logger is registered in the Logback context. " +
                "Security-relevant events — such as authentication failures, access control " +
                "violations, and suspicious inputs — are not captured in a dedicated, " +
                "structured audit trail.");
    }
}
