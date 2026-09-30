# Bootstrap

`SpringMongoApplication` is the only entry point. `@ComponentScan` is an explicit list:

- `com.mongodb.infra.configuration`
- `com.mongodb.infra.adapters.repository`
- `com.mongodb.controller`
- `com.mongodb.domain.adapter.service`

A new package is invisible to Spring until it is added to that list and to any test that declares its own `@ComponentScan`.

Keep `exclude = DataSourceAutoConfiguration.class`. This app uses MongoDB, not JDBC.

Do not move bean or repository enablement into this class. Service beans stay in `BeanConfiguration`. Mongo repository scanning stays in `MongoConfig`.
