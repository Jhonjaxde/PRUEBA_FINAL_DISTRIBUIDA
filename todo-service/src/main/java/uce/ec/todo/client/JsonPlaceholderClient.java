package uce.ec.todo.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import uce.ec.common.dto.UserDTO;

@RegisterRestClient(configKey = "jsonplaceholder-api")
public interface JsonPlaceholderClient {

    @GET
    @Path("/users/{id}")
    UserDTO getUserById(@PathParam("id") Long id);
}
