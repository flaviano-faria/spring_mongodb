# Tests

Follow the unit test policy and the class-to-test map in `AGENTS.md`. This file only adds what is easy to get wrong in this repository.

`UserServiceTest` repeats `@ComponentScan`, and Spring does not process it. The test loads `SpringMongoApplication` and `@Import(TestConfig.class)`. See `layers/bootstrap.md`.

Integration tests use `TestConfig` and the image `mongo:6.0.2`. `MongoTestConfig` is unused and its `@ComponentScan` omits `com.mongodb.controller`. Do not import it, extend it, or edit that list.

Controller tests use `MockMvcBuilders.standaloneSetup` and a Mockito mock of `UserServicePort`. They call `/api/users`, without the `/springmongodb` context path. Entity mapping tests are plain JUnit, with no Spring context.

The `stop` hook behavior is rule 6 of the unit test policy in `AGENTS.md`.
