---
name: Apply IRepository Interface (DDD)
about: Refactor a repository to conform to the generic IRepository interface
title: "Apply IRepository interface to I<name>Repo"
labels: refactor, ddd
---

### Tasks
- [ ] Refactor `I<name>Repo` to extend `IRepository`
    - [ ] Ensure it implements `save(T entity)`
    - [ ] Ensure it implements `findAll()`
    - [ ] Ensure it implements `ofIdentity(ID id)`
    - [ ] Ensure it implements `containsOfIdentity(ID id)`
    - [ ] Make sure you delete existing methods doing the same job
- [ ] Update repository implementations to comply with `IRepository`
- [ ] Refactor dependent classes to use the updated interface
- [ ] Create/adjust unit tests for `I<name>Repo`
- [ ] Adjust unit tests in other classes  