package uce.ec.usuarios.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import uce.ec.common.dto.UserDTO;
import uce.ec.usuarios.client.JsonPlaceholderClient;

@ApplicationScoped
public class UserService {

    @Inject
    @RestClient
    JsonPlaceholderClient jsonPlaceholderClient;

    public UserDTO getUserById(Long id) {
        return jsonPlaceholderClient.getUserById(id);
    }
}
