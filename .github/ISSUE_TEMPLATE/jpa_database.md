---
name: Database
about: Persist an aggregate in a relational database
title: "Persist <AggregateName> Aggregate in database"
labels: api
---
### Description
Implements persistence for the <AggregateName> aggregate using JPA, replacing the in-memory repository with a database-backed implementation. This includes the creation of a Data Model, repository adapter, mapper/assembler, and Spring Data interface, ensuring a clear separation between domain and persistence layers while preserving existing system behavior.

### Tasks

- [ ] Annotate MemGenreRepo as Spring repository with mem profile

- [ ] Create **Jpa<Class>Repo**
    - create `Jpa<Class>Repo` in `persistence/repository/jpa` and tests
    - implements `I<Class>Repo`
    - injects `I<Class>SpringDataRepo` and `<Class>Assembler`
    - delegates all persistence to `I<Class>SpringDataRepo`
    - delegates all mapping to `<Class>Assembler`

- [ ] Create **<Class>DataModel**
    - create `<Class>DataModel` in `persistence/jpa/dataModel`
    - `@Entity`, `@Table(name = "<Class>s")`
    - add Lombok annotations: `@Getter`, `@NoArgsConstructor`, `@AllArgsConstructor`
    - `@Id` on the identity field

- [ ] Create **<Class>Assembler**
    - create `<Class>Assembler` in `ersistence/jpa/assembler`
    - method `DM2Domain()` and `domain2DM()`
    - delegates reconstruction to `<Class>Factory`

- [ ] Create **I<Class>SpringDataRepo**
    - create `I<Class>SpringDataRepo` in `persistence/springdata` 
    - extends `JpaRepository<<Class>DataModel, String>`
  
## Acceptance
- [ ] `Mem<Class>Repo` can be replaced by `Jpa<Class>RepoImpl` with zero changes outside `persistence`
- [ ] no domain class has any JPA or Lombok annotation
- [ ] `mvn verify` passes