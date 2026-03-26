---
name: Reengineer Aggregate Root (DDD)
about: Refactor a class into a proper DDD aggregate root
title: "Reengineer <ClassName> into Aggregate Root"
labels: refactor, ddd
---

### Tasks
- [ ] Refactor `<ClassName>` into an Aggregate Root
- [ ] Ensure it implements `AggregateRoot` interface
    - [ ] make sure it implements identity() method
    - [ ] make sure it implements sameAs(Object object) method
- [ ] Prevent external access to internal entities
- [ ] Refactor related entities/value objects to be managed only through the root
- [ ] Refactor repositories to reference only the aggregate root
- [ ] Update unit tests accordingly
- [ ] Ensure tests validate invariants

### Acceptance Criteria
- [ ] `<ClassName>` is the single entry point for its aggregate
- [ ] All invariants are enforced inside the aggregate root
- [ ] No external class directly modifies internal entities
- [ ] Tests reflect and validate the new aggregate behavior 