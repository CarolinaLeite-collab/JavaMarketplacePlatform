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
 * OWASP A05:2021 — Security Misconfiguration
 *
 * Documents insecure configuration risks present in the dev profile.
 * The H2 database web console is enabled without any authentication layer,
 * allowing any user with network access to execute arbitrary SQL against the
 * embedded datastore. Additionally, no Spring Security configuration exists
 * in any profile, leaving all endpoints completely unprotected.
 *
 * <p><strong>Accepted-Risk Documentation:</strong> These tests intentionally pass
 * because the conditions they describe exist in the current codebase. They do not
 * assert that security controls are in place; rather, they serve as a formal,
 * traceable record of risks acknowledged by the team. Disabling the H2 console in
 * non-development profiles and adding Spring Security are outside the scope of this
 * sprint and are tracked as separate backlog items.</p>
 */
@Tag("security")
class InsecureConfigurationSecurityTest {

    private Properties loadProperties(Path file) throws Exception {
        Properties props = new Properties();
        try (var reader = Files.newBufferedReader(file)) {
            props.load(reader);
        }
        return props;
    }

    @Test
    @DisplayName("OWASP A05 Insecure Config: H2 console is enabled in dev profile")
    void h2Console_isEnabledInDevProfile() throws Exception {
        Properties dev = loadProperties(Paths.get("src/main/resources/application-dev.properties"));
        assertEquals("true", dev.getProperty("spring.h2.console.enabled"),
                "H2 console is enabled in the dev profile — accessible at /h2-console without " +
                "authentication, allowing any network-reachable client to execute arbitrary SQL.");
    }

    @Test
    @DisplayName("OWASP A05 Insecure Config: H2 console path is exposed at /h2-console")
    void h2Console_isServedAtDefaultPath() throws Exception {
        Properties dev = loadProperties(Paths.get("src/main/resources/application-dev.properties"));
        assertEquals("/h2-console", dev.getProperty("spring.h2.console.path"),
                "H2 console path is set to /h2-console — reachable without authentication when " +
                "the dev profile is active, exposing full database access to unauthenticated users.");
    }

    @Test
    @DisplayName("OWASP A05 Insecure Config: no Spring Security configuration is present in the application")
    void springSecurityAutoConfiguration_isNotPresent() throws Exception {
        Properties base = loadProperties(Paths.get("src/main/resources/application.properties"));
        Properties dev  = loadProperties(Paths.get("src/main/resources/application-dev.properties"));

        assertNull(base.getProperty("spring.security.enabled"),
                "spring.security.enabled is not set in application.properties — " +
                "Spring Security is not explicitly configured, leaving all endpoints unprotected.");
        assertNull(dev.getProperty("spring.security.enabled"),
                "spring.security.enabled is not set in application-dev.properties either — " +
                "no profile activates authentication or authorisation controls.");
    }
}
