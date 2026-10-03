# Persistence entity

`UserEntity` is the Mongo document. Keep `@Document(collection = "users")` and `@Id` on this class.

`User` has the same annotations. That is a known leak. Do not remove them from `User` while editing the entity. See "Known architecture leaks" in `AGENTS.md`.

Mirror every `User` field. `fromUser` and `toUser` copy every field, including `id`. The field checklist is in `AGENTS.md`.

```java
return UserEntity.builder()
        .id(user.getId())
        .document(user.getDocument())
        .name(user.getName())
        .age(user.getAge())
        .build();
```

On the first change, add `UserEntityTest`: a plain JUnit round-trip of `fromUser` / `toUser` that asserts every field. No Spring context and no Docker.
