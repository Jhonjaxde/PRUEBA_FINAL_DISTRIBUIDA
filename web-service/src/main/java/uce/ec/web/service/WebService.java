package uce.ec.web.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import uce.ec.common.dto.TodoDTO;
import uce.ec.web.client.TodoServiceClient;

import java.util.List;

@ApplicationScoped
public class WebService {

    @Inject
    @RestClient
    TodoServiceClient todoServiceClient;

    public List<TodoDTO> getTodosWithUserInfo() {
        return todoServiceClient.getAllTodos();
    }

    public List<TodoDTO> getTodosByUserIdWithUserInfo(Long userId) {
        return todoServiceClient.getTodosByUserId(userId);
    }
}
        


