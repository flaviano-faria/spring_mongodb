# Domain model

`User` is the only aggregate: `id`, `document`, `name`, `age`.

`User` and `UserEntity` both have `@Document` and `@Id`. That is a known leak on `User`. Leave the annotations on `User` unless the task is to remove them from the domain. Do not drop them from `UserEntity` either. The collection name `users` is the persistence mapping. See "Known architecture leaks" in `AGENTS.md`.

A field change follows the field checklist in `AGENTS.md`: `User`, `UserEntity`, `fromUser`, `toUser`, and every test that builds or reads a user. `age` is a primitive `int`. Use `Integer` only when a missing value must stay null.

Do not add repository, controller, or Spring Web types to this class.
