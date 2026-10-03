# HTTP adapter

Controllers are driving adapters. Inject `UserServicePort` through the constructor. Do not inject `UserService`, `UserRepository`, or `IUserRepository`.

Bind and return `User` directly. There is no DTO layer. Do not add one unless the task asks for it.

Status codes, the empty `201` body, and the missing-user `200` are the API surface in `AGENTS.md`. Controller mappings such as `/api/users` are relative to `server.servlet.context-path=/springmongodb`. The public URL is `http://localhost:8080/springmongodb/api/users`. `standaloneSetup` tests call the controller path, without the context path.

A new endpoint follows the "New endpoint / use case" checklist in `AGENTS.md`. On the first controller change, add `UserControllerTest` with `MockMvcBuilders.standaloneSetup(new UserController(mockPort))`. Do not use `@WebMvcTest`. The explicit component scan would pull repository beans into a web slice and require MongoDB.
