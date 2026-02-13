package uce.ec.todo.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uce.ec.common.dto.TodoDTO;
import uce.ec.todo.service.TodoService;

import java.util.List;

@Path("/api/todos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoResource {

    @Inject
    TodoService todoService;

    @GET
    public List<TodoDTO> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GET
    @Path("/user/{userId}")
    public List<TodoDTO> getTodosByUserId(@PathParam("userId") Long userId) {
        return todoService.getTodosByUserId(userId);
    }

    @GET
    @Path("/{id}")
    public Response getTodoById(@PathParam("id") Long id) {
        TodoDTO todo = todoService.getTodoById(id);
        if (todo != null) {
            return Response.ok(todo).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response createTodo(TodoDTO todoDTO) {
        TodoDTO created = todoService.createTodo(todoDTO);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTodo(@PathParam("id") Long id, TodoDTO todoDTO) {
        TodoDTO updated = todoService.updateTodo(id, todoDTO);
        if (updated != null) {
            return Response.ok(updated).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTodo(@PathParam("id") Long id) {
        boolean deleted = todoService.deleteTodo(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
