# Controller conventions

Observed in `com.mongodb.controller`. `UserController` is the only class. Match it when adding a handler or a new controller.

## Type

- Package `com.mongodb.controller`. One public `@RestController` per resource, named `<Aggregate>Controller`.
- Class-level `@RequestMapping` holds the resource path (`/api/users`). Method mappings are relative to that path: bare `@PostMapping` / `@GetMapping` for the collection, `@GetMapping("/{id}")` and `@DeleteMapping("/{id}")` for one document.
- `@RestController` is the stereotype that registers the bean. This package is already on `SpringMongoApplication`'s `@ComponentScan`, so the controller has no `@Bean`.
- No Lombok. Declare `private final UserServicePort userServicePort` and assign it in an explicit public constructor. No `@Autowired`.

## Boundary

- Inject the inbound port only (`UserServicePort`). Do not inject `UserService`, `UserRepositoryPort`, `UserRepository`, or `IUserRepository`.
- Bind and return the domain type. `@RequestBody User` on create. `@PathVariable String id` on by-id routes. No DTO, no `UserEntity`.
- A handler calls one port method and builds the HTTP response. No mapping, branching, validation, or persistence in this class.
- Handler names match the port: `createUser`, `getAllUsers`, `getUserById`, `deleteUser`.

## Response

Every handler returns `ResponseEntity`. Keep the status and body each method already uses:

| Handler | Signature | Body |
|---------|-----------|------|
| `createUser` | `ResponseEntity<?>` | `new ResponseEntity<>("", HttpStatus.CREATED)` |
| `getAllUsers` | `ResponseEntity<List<User>>` | `ResponseEntity.ok(userServicePort.getAllUsers())` |
| `getUserById` | `ResponseEntity<User>` | `new ResponseEntity<>(userServicePort.getUserById(id), HttpStatus.OK)` — a null user is still 200 |
| `deleteUser` | `ResponseEntity<Void>` | `ResponseEntity.ok().build()` after `deleteUser(id)` |

Create and delete are `void` on the port. The controller supplies the response; it does not read a saved id back from `createUser`.

Imports: wildcard `org.springframework.web.bind.annotation.*`. Import `HttpStatus` and `ResponseEntity` by name.

## Left out of this class

No `@Valid`, `@ControllerAdvice`, PUT, or PATCH. `spring-boot-starter-validation` is unused. Do not add those unless the task asks for them.
