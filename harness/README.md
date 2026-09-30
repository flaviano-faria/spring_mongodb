# Harness

Layer guidance for this repository. There are no per-layer Cursor rules. `AGENTS.md` tells the agent to read the matching file here before editing that layer.

```
controller → UserServicePort → UserService → UserRepositoryPort → UserRepository → IUserRepository → MongoDB
```

Controllers and infrastructure depend inward on ports and the domain. `UserService` depends on ports, never on Spring Data or HTTP types.

| Layer | Package | Read |
|-------|---------|------|
| Bootstrap | `com.mongodb.app` | `layers/bootstrap.md` |
| HTTP | `com.mongodb.controller` | `layers/http.md` |
| Domain model | `com.mongodb.domain.User` | `layers/domain.md` |
| Ports | `com.mongodb.domain.ports` | `layers/ports.md` |
| Service | `com.mongodb.domain.adapter.service` | `layers/service.md` |
| Persistence entity | `com.mongodb.infra.adapters.entity` | `layers/persistence-entity.md` |
| Persistence adapter | `com.mongodb.infra.adapters.repository` | `layers/persistence-repository.md` |
| Configuration | `com.mongodb.infra.configuration` | `layers/configuration.md` |
| Tests | `src/test/java` | `layers/testing.md` |

Project-wide behavior (API status codes, test policy, checklists) stays in `AGENTS.md`.
