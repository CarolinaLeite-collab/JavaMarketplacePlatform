---
name: Create Class ID (DDD)
about: Creates a new ClassID
title: "Create <ClassName>Id"
labels: refactor, ddd
---

### Tasks
- [ ] Create `<ClassName>Id` class
    - [ ] Ensure it implements `DomainId` interface
    - [ ] Implement `equals(Object object)` method
    - [ ] Implement `toString()` method
    - [ ] make sure you delete existing methods doing the same job
- [ ] Guarantee immutability of the ID
- [ ] Add validation if applicable (e.g., non-null, format)
- [ ] Update related entities to use `<ClassName>Id` instead of primitive types
- [ ] Create unit tests for `<ClassName>Id`
- [ ]  Adjust unit tests in other classes