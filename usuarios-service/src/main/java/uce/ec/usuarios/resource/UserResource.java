package uce.ec.usuarios.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import uce.ec.common.dto.UserDTO;
import uce.ec.usuarios.service.UserService;

@Path("/api/users")
public class UserResource {

    @Inject
    UserService userService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDTO getUserById(@PathParam("id") Long id) {
        return userService.getUserById(id);
    }
}
