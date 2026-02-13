package uce.ec.web.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import uce.ec.common.dto.UserDTO;

@RegisterRestClient(configKey = "user-api")
public interface UserServiceClient {

    @GET
    @Path("/api/users/{id}")
    UserDTO getUserById(@PathParam("id") Long id);
}
