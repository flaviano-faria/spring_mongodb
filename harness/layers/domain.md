# Domain model

Observed in `com.mongodb.domain`. `User` is the only type in this package. Ports and `UserService` live in subpackages; their conventions are `layers/ports.md` and `layers/service.md`.

## Type

- Public class. No superclass, no interfaces, and no methods of its own.
- Lombok, in this order: `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`. No `@RequiredArgsConstructor`. Import each annotation by name.
- Fields are private, in this order: `String id`, `String document`, `String name`, `int age`.
- `age` is a primitive `int`. Use `Integer` only when a missing value must stay null.

## Persistence annotations on the model

`@Document(collection = "users")` on the class and `@Id` on `id` are a known leak. `UserEntity` carries the same annotations. Leave both in place unless the task is to remove them from the domain. The collection name is `"users"`. See "Known architecture leaks" in `AGENTS.md`.

## Boundary

Do not add repository, controller, Spring Web, or validation types to `User`. No behavior, invariants, or mapping methods on this class. Mapping stays on `UserEntity.fromUser` / `toUser`.

A field change follows the field checklist in `AGENTS.md`: `User`, `UserEntity`, `fromUser`, `toUser`, and every test that builds or reads a user.
