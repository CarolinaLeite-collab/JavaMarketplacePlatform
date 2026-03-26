---
name: Default Issue
about: Default Template Issue
title: "Refactor <ClassName> to DDD-compliant Aggregate"
labels: refactor, create, add
---

### sub-issues:
Define `<ClassName>` identity
Refactor `<ClassName>` into an Aggregate Root
Refactor `I<name>Repo`

### Acceptance Criteria
- [ ] `<ClassName>` uses a dedicated `<ClassName>Id` instead of primitive identity
- [ ] `<ClassName>` is a valid Aggregate Root
- [ ] All invariants are enforced inside the aggregate root
- [ ] No external class directly modifies internal entities
- [ ] Repositories operate only on the aggregate root
- [ ] Tests reflect and validate the new domain model behavior 