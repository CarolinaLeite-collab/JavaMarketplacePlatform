## Description
<!-- What does this PR do? Why is it needed? -->
- 

---

## Related Issue / Ticket
<!-- Link GitHub issue -->
- Closes #
- Related to #

---

## Type of Change
<!-- Mark with an 'x' -->
- [ ] New feature
- [ ] Bug fix
- [ ] Refactor
- [ ] Chore / Maintenance
- [ ] Documentation
- [ ] Performance improvement

---

## Changes Description
<!-- Link GitHub issue -->

1.
2.
3.

---

## How Has This Been Tested?
<!-- Describe testing steps -->
- [ ] Unit tests
- [ ] Integration tests
- [ ] Manual testing

---

## Checklist
<!-- Mark with an 'x' -->
- [ ] Code follows project conventions
- [ ] Self-review completed
- [ ] Tests added/updated where necessary
- [ ] Documentation updated (if needed)
- [ ] PR is ready for review

---

## Security Code Review Checklist
<!-- Required for PR review. Mark N/A only when the item does not apply. -->

### Input Validation
- [ ] All new external inputs are validated before use
- [ ] Request DTOs include validation rules where applicable such as `@NotBlank`, `@NotNull`, `@Min`, `@Max`, `@Size`, or `@Pattern`
- [ ] Domain/value object validation is preserved and covered by tests
- [ ] User-provided values are not used unsafely in queries, paths, redirects, or logs
- 
### Authorization and Access Control
- [ ] New or changed actions are reflected in `AuthorizationPolicy` when role-based access is needed
- [ ] Link providers expose only actions allowed for the current user role
- [ ] Access control tests cover allowed and denied cases where applicable
- [ ] User IDs from requests or headers are checked before granting access

### Secrets and Configuration
- [ ] No real secrets or credentials are committed
- [ ] Environment-specific values are not hardcoded
- [ ] Development-only settings, such as H2 console or debug logging, are not enabled unintentionally for production
- [ ] Gitleaks findings from CI are resolved before merge

### Logging and Error Handling
- [ ] Error responses do not expose internal details or sensitive data
- [ ] Exception messages returned to clients do not reveal sensitive data
- [ ] Logs do not include secrets, personal data, or unnecessary request payloads
- [ ] Security-related errors follow the existing REST exception handling

### Dependencies and CI Security Gates
- [ ] Dependency changes are justified and lock files are updated
- [ ] Build, tests, and security scans pass in CI
- [ ] Unresolved security findings are documented

## Additional Notes
<!-- Anything reviewers should know -->
-