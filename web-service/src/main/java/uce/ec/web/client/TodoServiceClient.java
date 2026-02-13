package uce.ec.web.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import uce.ec.common.dto.TodoDTO;

import java.util.List;

@RegisterRestClient(configKey = "todo-api")
public interface TodoServiceClient {

    @GET
    @Path("/api/todos")
    List<TodoDTO> getAllTodos();

    @GET
    @Path("/api/todos/user/{userId}")
    List<TodoDTO> getTodosByUserId(@PathParam("userId") Long userId);
}
