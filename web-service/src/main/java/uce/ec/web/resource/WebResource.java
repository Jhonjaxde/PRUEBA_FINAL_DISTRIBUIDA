package uce.ec.web.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import uce.ec.common.dto.TodoDTO;
import uce.ec.web.service.WebService;

import java.util.List;

@Path("/api/web")
@Produces(MediaType.APPLICATION_JSON)
public class WebResource {

    @Inject
    WebService webService;

    @GET
    @Path("/todos")
    public List<TodoDTO> getAllTodosWithUserInfo() {
        return webService.getTodosWithUserInfo();
    }

    @GET
    @Path("/todos/user/{userId}")
    public List<TodoDTO> getTodosByUserIdWithUserInfo(@PathParam("userId") Long userId) {
        return webService.getTodosByUserIdWithUserInfo(userId);
    }
}
