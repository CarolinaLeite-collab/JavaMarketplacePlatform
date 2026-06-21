package MITELOVERS.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OWASP A04:2025 — Cryptographic Failures
 *
 * Documents the absence of cryptographic protections for data in transit and at rest.
 * The application runs with an H2 database that has no password and operates over
 * plain HTTP with no TLS or SSL keystore configured. Sensitive data — including user
 * email addresses, sale prices, and item descriptions — is transmitted and stored
 * without any encryption layer.
 *
 * <p><strong>Accepted-Risk Documentation:</strong> These tests intentionally pass
 * because the vulnerabilities they describe are present in the current codebase.
 * They do not verify that encryption controls are in place; rather, they serve as a
 * formal, traceable record of risks acknowledged by the team. Introducing TLS and
 * securing the datastore are outside the scope of this sprint and are tracked as
 * separate backlog items.</p>
 */
@Tag("security")
class CryptographicFailuresSecurityTest {

    private Properties loadProperties(Path file) throws Exception {
        Properties props = new Properties();
        try (var reader = Files.newBufferedReader(file)) {
            props.load(reader);
        }
        return props;
    }

    @Test
    @DisplayName("OWASP A04 Cryptographic Failure: H2 database password is empty — datastore is unprotected at rest")
    void datasource_hasNoPassword_datastoreUnprotectedAtRest() throws Exception {
        Path propsFile = Paths.get("src/main/resources/application.properties");
        assertTrue(Files.exists(propsFile), "application.properties must be present");

        Properties props = loadProperties(propsFile);
        String password = props.getProperty("spring.datasource.password");

        assertEquals("", password,
                "The H2 datasource password is an empty string. Any process with network " +
                "access to the database port can connect without credentials, exposing all " +
                "stored data — including user emails, item details, and sale records.");
    }

    @Test
    @DisplayName("OWASP A04 Cryptographic Failure: no TLS/SSL keystore is configured in any profile — all traffic transmitted over plain HTTP")
    void server_hasNoTlsKeyStore_trafficTransmittedInCleartext() throws Exception {
        Properties base = loadProperties(Paths.get("src/main/resources/application.properties"));
        Properties dev  = loadProperties(Paths.get("src/main/resources/application-dev.properties"));

        assertNull(base.getProperty("server.ssl.key-store"),
                "server.ssl.key-store is not configured in application.properties. " +
                "All HTTP traffic — including the X-User-Id identity header, sale prices, " +
                "and personal data — is transmitted in cleartext, susceptible to interception.");
        assertNull(dev.getProperty("server.ssl.key-store"),
                "server.ssl.key-store is not configured in application-dev.properties either. " +
                "No profile enables TLS, confirming the application has no encryption in transit.");
    }
}
