package com.m1kellz.todoservice.controller;

import com.m1kellz.todoservice.entity.Todo;
import com.m1kellz.todoservice.model.TodoRequest;
import com.m1kellz.todoservice.model.TodoRequestForService;
import com.m1kellz.todoservice.model.TodoResponse;
import com.m1kellz.todoservice.service.TodoService;
import com.m1kellz.todoservice.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/todos")
@Tag(name = "Todos", description = "Create and manage todos (JWT required for production endpoints)")
@SecurityRequirement(name = "bearerAuth")
public class TodoController {

    private final TodoService todoService;
    private final JwtUtils jwtUtils;

    @Autowired
    public TodoController(TodoService todoService, JwtUtils jwtUtils) {
        this.todoService = todoService;
        this.jwtUtils = jwtUtils;
    }

    @Operation(summary = "[Dev] Create todo without auth", description = "Test endpoint — hardcoded userId=20. Do not use in production.", security = {})
    @PostMapping("/test")
    public ResponseEntity<Void> saveTodoTest(@RequestBody TodoRequest todoDto) {
        int extractedUserId = 20;
        TodoRequestForService todoRequestForService = new TodoRequestForService(
                todoDto.title(), extractedUserId, todoDto.description(), todoDto.priority(), todoDto.status());
        todoService.saveTodo(todoRequestForService);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "[Dev] List all todos without auth", security = {})
    @GetMapping("/test")
    public ResponseEntity<List<Todo>> getAllTodosTest() {
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    @Operation(summary = "[Dev] List todos by userId without auth", security = {})
    @GetMapping("/test/{userId}")
    public ResponseEntity<List<TodoResponse>> getAllTodosByUserIdTest(@PathVariable int userId) {
        return ResponseEntity.ok(todoService.getAllTodosWithDetailsByUserId(userId));
    }

    @Operation(summary = "[Dev] Update todo without auth", security = {})
    @PutMapping("/test/{id}")
    public ResponseEntity<Void> updateTodoTest(@PathVariable int id, @RequestBody TodoRequest todoDto) {
        todoService.updateTodo(id, todoDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Create todo", description = "Creates a todo for the authenticated user (userId from JWT).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Todo created"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Token valid but user id missing from JWT")
    })
    @PostMapping
    public ResponseEntity<Void> saveTodo(
            @RequestBody TodoRequest todoDto,
            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        if (!jwtUtils.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer extractedUserId = jwtUtils.extractUserIdFromToken(token);
        if (extractedUserId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        TodoRequestForService todoRequestForService = new TodoRequestForService(
                todoDto.title(), extractedUserId, todoDto.description(), todoDto.priority(), todoDto.status());
        todoService.saveTodo(todoRequestForService);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "List all todos", description = "Returns every todo in the system (authenticated).")
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos(@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        if (!jwtUtils.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    @Operation(summary = "List my todos", description = "Returns todos belonging to the authenticated user.")
    @GetMapping("/user")
    public ResponseEntity<List<Todo>> getAllTodosForUserId(@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        Integer extractedUserId = requireUserId(token);
        if (extractedUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(todoService.getAllTodosByUserId(extractedUserId));
    }

    @Operation(summary = "List my todos with details", description = "Returns todos with priority, status, and timestamps.")
    @GetMapping("/user/details/")
    public ResponseEntity<List<TodoResponse>> getAllTodosWithDetailsForUser(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        Integer extractedUserId = requireUserId(token);
        if (extractedUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(todoService.getAllTodosWithDetailsByUserId(extractedUserId));
    }

    @Operation(summary = "List todos by user ID", description = "Only allowed when path userId matches JWT user id.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Todo>> getAllTodosByUserId(
            @PathVariable int userId,
            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        Integer extractedUserId = requireMatchingUserId(token, userId);
        if (extractedUserId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(todoService.getAllTodosByUserId(userId));
    }

    @Operation(summary = "List todos with details by user ID")
    @GetMapping("/user/details/{userId}/")
    public ResponseEntity<List<TodoResponse>> getAllTodosWithDetailsByUserId(
            @PathVariable int userId,
            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        Integer extractedUserId = requireMatchingUserId(token, userId);
        if (extractedUserId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(todoService.getAllTodosWithDetailsByUserId(userId));
    }

    @Operation(summary = "Get todo by ID", description = "Only returns the todo if it belongs to the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Todo found"),
            @ApiResponse(responseCode = "403", description = "Todo belongs to another user"),
            @ApiResponse(responseCode = "404", description = "Todo not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(
            @PathVariable int id,
            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        Integer extractedUserId = requireUserId(token);
        if (extractedUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Todo> todo = todoService.getTodoById(id);
        if (todo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!extractedUserId.equals(todo.get().getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(todo.get());
    }

    @Operation(summary = "Update todo")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTodo(
            @PathVariable int id,
            @RequestBody TodoRequest todoDto,
            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        Integer extractedUserId = requireUserId(token);
        if (extractedUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Todo> todo = todoService.getTodoById(id);
        if (todo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!extractedUserId.equals(todo.get().getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        todoService.updateTodo(id, todoDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete todo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable int id,
            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        Integer extractedUserId = requireUserId(token);
        if (extractedUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Todo> todo = todoService.getTodoById(id);
        if (todo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!extractedUserId.equals(todo.get().getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        todoService.deleteTodo(id);
        return ResponseEntity.ok().build();
    }

    private Integer requireUserId(String token) {
        if (!jwtUtils.isTokenValid(token)) {
            return null;
        }
        return jwtUtils.extractUserIdFromToken(token);
    }

    private Integer requireMatchingUserId(String token, int userId) {
        Integer extractedUserId = requireUserId(token);
        if (extractedUserId == null || extractedUserId != userId) {
            return null;
        }
        return extractedUserId;
    }
}
