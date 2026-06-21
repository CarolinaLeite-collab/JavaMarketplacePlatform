package MITELOVERS.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OWASP A03:2025 — Software Supply Chain Failures
 *
 * Documents supply chain risks arising from the use of non-pinned dependency
 * version ranges in the frontend. When production dependencies specify a caret (^)
 * prefix, npm resolves to the latest compatible version at install time. A compromised
 * or maliciously published minor or patch version can be automatically pulled into the
 * build without an explicit reviewer decision.
 *
 * <p><strong>Accepted-Risk Documentation:</strong> These tests intentionally pass
 * because the conditions they describe exist in the current codebase. They do not
 * assert that supply-chain controls are in place; rather, they serve as a formal,
 * traceable record of risks acknowledged by the team. Remediation — such as exact
 * version pinning and enabling Subresource Integrity checks — is outside the scope
 * of this sprint and is tracked as a separate backlog item.</p>
 */
@Tag("security")
class SupplyChainSecurityTest {

    @Test
    @DisplayName("OWASP A03 Supply Chain: production dependencies use caret ranges, allowing automatic minor/patch updates without explicit approval")
    void frontendProductionDependencies_useCaretRanges_allowingUnexpectedUpdates() throws Exception {
        Path packageJson = Paths.get("frontend/package.json");
        assertTrue(Files.exists(packageJson), "frontend/package.json must be present");

        String content = Files.readString(packageJson);

        Pattern dependenciesBlock = Pattern.compile("\"dependencies\"\\s*:\\s*\\{([^}]+)\\}");
        Matcher blockMatcher = dependenciesBlock.matcher(content);
        assertTrue(blockMatcher.find(), "package.json must contain a 'dependencies' section");

        long caretCount = blockMatcher.group(1).lines()
                .filter(line -> line.contains("\"^"))
                .count();

        assertTrue(caretCount > 0,
                caretCount + " production dependency/dependencies use a caret (^) range and will " +
                "resolve to the latest compatible version at install time. A compromised minor or " +
                "patch release could be pulled automatically, constituting a supply chain risk.");
    }

    @Test
    @DisplayName("OWASP A03 Supply Chain: npm-shrinkwrap.json is absent — installed versions can drift across environments")
    void npmShrinkwrap_isAbsent_installedVersionsCanDrift() {
        Path shrinkwrap = Paths.get("frontend/npm-shrinkwrap.json");
        assertFalse(shrinkwrap.toFile().exists(),
                "npm-shrinkwrap.json is not present. Unlike package-lock.json, which locks " +
                "versions for the local development environment, npm-shrinkwrap.json enforces " +
                "the same lock for all consumers of the package. Its absence means that CI " +
                "environments running 'npm install' instead of 'npm ci' may silently install " +
                "versions that differ from those tested locally.");
    }
}
