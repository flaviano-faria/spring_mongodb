# Persistence entity

`UserEntity` is the Mongo document. `@Document(collection = "users")` belongs on the entity. Mirror every `User` field.

`fromUser` and `toUser` copy every field, including `id`. When a field is added, update both methods in the same change.

```java
return UserEntity.builder()
        .id(user.getId())
        .document(user.getDocument())
        .name(user.getName())
        .age(user.getAge())
        .build();
```

On the first change, add `UserEntityTest`: a plain JUnit round-trip of `fromUser` / `toUser` that asserts every field. No Spring context and no Docker.
