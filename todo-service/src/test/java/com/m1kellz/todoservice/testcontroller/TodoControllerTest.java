package com.m1kellz.todoservice.testcontroller;

import com.m1kellz.todoservice.controller.TodoController;
import com.m1kellz.todoservice.entity.Priority;
import com.m1kellz.todoservice.entity.Status;
import com.m1kellz.todoservice.entity.Todo;
import com.m1kellz.todoservice.model.TodoRequest;
import com.m1kellz.todoservice.service.TodoService;
import com.m1kellz.todoservice.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/*@SpringBootTest
public class TodoControllerTest {
    @InjectMocks
    private TodoController todoController;

    @Mock
    private TodoService todoService;

    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTodo_ValidToken_ReturnsOk() {
        // Mock token validation
        when(jwtUtils.isTokenValid(anyString())).thenReturn(true);

        // Mock extracted user ID
        when(jwtUtils.extractUserIdFromToken(anyString())).thenReturn(1L);

        // Mock service call
        doNothing().when(todoService).saveTodo(any());

        // Call the controller method
        ResponseEntity<Void> response = todoController.saveTodo(new TodoRequest("finish the task","complete user service", Priority.CRITICAL, Status.IN_PROGRESS), "valid-token");

        // Verify
        verify(todoService).saveTodo(any());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllTodos_ValidToken_ReturnsTodos() {
        // Mock token validation
        when(jwtUtils.isTokenValid(anyString())).thenReturn(true);

        // Mock service call
        List<Todo> mockTodos = Arrays.asList(new Todo(), new Todo());
        when(todoService.getAllTodos()).thenReturn(mockTodos);

        // Call the controller method
        ResponseEntity<List<Todo>> response = todoController.getAllTodos("valid-token");

        // Verify
        verify(todoService).getAllTodos();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTodos, response.getBody());
    }

    @Test
    void saveTodo_InvalidToken_ReturnsUnauthorized() {
        // Mock token validation
        when(jwtUtils.isTokenValid(anyString())).thenReturn(false);

        // Call the controller method
        ResponseEntity<Void> response = todoController.saveTodo(
                new TodoRequest("finish the task","complete user service", Priority.CRITICAL, Status.IN_PROGRESS), "invalid-token");

        // Verify
        verify(todoService, never()).saveTodo(any());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }


}*/

