# Tests

Name the class `<ClassName>Test`. Surefire includes only `**/*Test.java`. Mirror the production package under `src/test/java`.

Integration tests follow `UserServiceTest`: `@SpringBootTest(classes = SpringMongoApplication.class)`, `@Import(TestConfig.class)`, `@DynamicPropertySource` set from `TestConfig.getMongoUri()`, and `deleteAll()` in `@BeforeEach`. The container image is `mongo:6.0.2`. Use `TestConfig`. `MongoTestConfig` is unused; do not extend it.

Controller tests use `MockMvcBuilders.standaloneSetup` and a Mockito mock of `UserServicePort`. Entity mapping tests are plain JUnit, with no Spring context.

Do not make a run pass by deleting assertions, loosening expected values, adding `@Disabled`, or skipping tests. Docker must be running for Testcontainers. If it is not, say the suite was not verified.

The `stop` hook runs `.\mvnw.cmd test` after an agent edits `src/main`, `src/test`, or `pom.xml`.
