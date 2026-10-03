# Bootstrap

`SpringMongoApplication` is the only entry point. Keep `exclude = DataSourceAutoConfiguration.class`. This app uses MongoDB, not JDBC.

`@ComponentScan` on this class is the list Spring loads:

- `com.mongodb.infra.configuration`
- `com.mongodb.infra.adapters.repository`
- `com.mongodb.controller`
- `com.mongodb.domain.adapter.service`

A new package is invisible until it is added here. `@SpringBootTest(classes = SpringMongoApplication.class)` uses this same list.

`UserServiceTest` repeats those four packages on its own `@ComponentScan`. Spring does not process that annotation. The test context is the application class plus `@Import(TestConfig.class)`. Do not treat the test annotation as a second scan.

`MongoTestConfig` declares another `@ComponentScan` and omits `com.mongodb.controller`. The class is unused. Do not import it, extend it, or edit that list to match.

The application scan does not register `UserService`. The class has no stereotype. See `layers/service.md`.

Do not move bean or repository enablement into this class. Service beans stay in `BeanConfiguration`. Mongo repository scanning stays in `MongoConfig`.
