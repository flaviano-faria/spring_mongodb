# Domain model

`User` is the only aggregate: `id`, `document`, `name`, `age`. Collection name is `users`.

`@Document` and `@Id` on this class are a known leak. Leave them unless the task is to remove Mongo annotations from the domain.

A field change is incomplete until all of these match:

1. `User`
2. `UserEntity`
3. `UserEntity.fromUser` and `toUser`
4. Builders and assertions in every test that constructs or reads a user

Do not add repository, controller, or Spring Web types to this class. Use a wrapper (`Integer`) instead of primitive `int` when a missing value must stay null; existing `age` is a primitive.
