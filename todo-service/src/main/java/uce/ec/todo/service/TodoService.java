package uce.ec.todo.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import uce.ec.common.dto.TodoDTO;
import uce.ec.common.dto.UserDTO;
import uce.ec.todo.client.JsonPlaceholderClient;
import uce.ec.todo.db.Todo;
import uce.ec.todo.repository.TodoRepository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TodoService {

    @Inject
    TodoRepository todoRepository;

    @Inject
    @RestClient
    JsonPlaceholderClient jsonPlaceholderClient;

    public List<TodoDTO> getAllTodos() {
        return todoRepository.listAll().stream()
                .map(this::toDTOWithUserInfo)
                .collect(Collectors.toList());
    }

    public List<TodoDTO> getTodosByUserId(Long userId) {
        return todoRepository.findByUserId(userId).stream()
                .map(this::toDTOWithUserInfo)
                .collect(Collectors.toList());
    }

    public TodoDTO getTodoById(Long id) {
        Todo todo = todoRepository.findById(id);
        return todo != null ? toDTOWithUserInfo(todo) : null;
    }

    @Transactional
    public TodoDTO createTodo(TodoDTO todoDTO) {
        Todo todo = new Todo(todoDTO.getUserId(), todoDTO.getTitle(), todoDTO.getCompleted());
        todoRepository.persist(todo);
        return toDTO(todo);
    }

    @Transactional
    public TodoDTO updateTodo(Long id, TodoDTO todoDTO) {
        Todo todo = todoRepository.findById(id);
        if (todo != null) {
            todo.userId = todoDTO.getUserId();
            todo.title = todoDTO.getTitle();
            todo.completed = todoDTO.getCompleted();
            todoRepository.persist(todo);
            return toDTO(todo);
        }
        return null;
    }

    @Transactional
    public boolean deleteTodo(Long id) {
        return todoRepository.deleteById(id);
    }

    private TodoDTO toDTO(Todo todo) {
        TodoDTO dto = new TodoDTO();
        dto.setId(todo.id);
        dto.setUserId(todo.userId);
        dto.setTitle(todo.title);
        dto.setCompleted(todo.completed);
        return dto;
    }

    private TodoDTO toDTOWithUserInfo(Todo todo) {
        TodoDTO dto = toDTO(todo);
        try {
            UserDTO user = jsonPlaceholderClient.getUserById(todo.userId);
            dto.setUsername(user.getUsername());
            dto.setName(user.getName());
        } catch (Exception e) {
            // En caso de error, usar valores por defecto
            dto.setUsername("Unknown");
            dto.setName("Unknown User");
        }
        return dto;
    }
}
