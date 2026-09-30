# HTTP adapter

Controllers are driving adapters. Inject `UserServicePort` through the constructor. Do not inject `UserService`, `UserRepository`, or `IUserRepository`.

Keep the current HTTP contract unless the task is to change it:

- `POST /api/users` → `201` and an empty string body
- `GET /api/users` → `200` and the list
- `GET /api/users/{id}` → `200` and the user, or an empty body when missing (not `404`)
- `DELETE /api/users/{id}` → `200` and an empty body

Bind and return `User` directly. There is no DTO layer; do not add one unless the task asks for it.

A new endpoint needs a method on `UserServicePort` and `UserService` first, then the mapping here. On the first controller change, add `UserControllerTest` with `MockMvcBuilders.standaloneSetup(new UserController(mockPort))`. Do not use `@WebMvcTest`; the explicit component scan would pull Mongo beans into the slice.
