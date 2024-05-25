package com.m1kellz.todoservice.service;

import com.m1kellz.todoservice.entity.Todo;
import com.m1kellz.todoservice.entity.TodoDetails;
import com.m1kellz.todoservice.model.TodoRequest;
import com.m1kellz.todoservice.model.TodoResponse;
import com.m1kellz.todoservice.repository.TodoDetailsRepository;
import com.m1kellz.todoservice.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final TodoDetailsRepository todoDetailsRepository;
    @Override
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @Override
    public List<Todo> getAllTodosByUserId(Long userId) {
        return todoRepository.findAllByUserId(userId);
    }


    @Override
    public List<TodoResponse> getAllTodosWithDetailsByUserId(Long userId) {
        List<Todo> todos = todoRepository.findAllByUserId(userId);
        // Return ResponseEntity with the list of TodoResponses
        return mapToTodoResponses(todos);
    }

    @Override
    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id) ;
    }

    @Override
    public void saveTodo(TodoRequest todoDto) {
        Todo todo = new Todo();
        todo.setTitle(todoDto.title());
        todo.setUserId(todoDto.userId());

        TodoDetails todoDetails = new TodoDetails();
        todoDetails.setDescription(todoDto.description());
        todoDetails.setPriority(todoDto.priority());
        todoDetails.setStatus(todoDto.status());

        // Associate Todo with TodoDetails
        todo.setTodoDetails(todoDetails);

        todoDetailsRepository.save(todoDetails);
        todoRepository.save(todo);

    }

    @Override
    public void updateTodo(long id ,TodoRequest todoDto) {

        Optional<Todo> todoOptional = todoRepository.findById(id);
        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            Optional<TodoDetails> todoDetailsOptional = todoDetailsRepository.findById(todo.getTodoDetails().getId());

            if (todoDetailsOptional.isPresent()) {
                TodoDetails todoDetails = todoDetailsOptional.get();
                todoDetails.setDescription(todoDto.description());
                todoDetails.setPriority(todoDto.priority());
                todoDetails.setStatus(todoDto.status());

                // Create a new Todo instance with updated values
                Todo updatedTodo = new Todo();
                updatedTodo.setId(todo.getId());
                updatedTodo.setTitle(todoDto.title());
                updatedTodo.setUserId(todoDto.userId());

                // Associate Todo with TodoDetails
                updatedTodo.setTodoDetails(todoDetails);
                todoDetailsRepository.save(todoDetails);
                todoRepository.save(updatedTodo);
            }
        }

    }

    @Override
    public void deleteTodo(Long id) {
    todoRepository.deleteById(id);
    }


    public List<TodoResponse> mapToTodoResponses(List<Todo> todos) {
        return todos.stream()
                .map(this::mapToTodoResponse)
                .collect(Collectors.toList());
    }


    public TodoResponse mapToTodoResponse(Todo todo) {
        TodoDetails todoDetails = todo.getTodoDetails();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todoDetails.getDescription(),
                todoDetails.getPriority(),
                todoDetails.getStatus(),
                todoDetails.getCreatedAt(),
                todoDetails.getLastUpdated());
    }

}
