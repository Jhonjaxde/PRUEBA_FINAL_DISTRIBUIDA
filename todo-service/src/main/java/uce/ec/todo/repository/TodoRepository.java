package uce.ec.todo.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import uce.ec.todo.db.Todo;

import java.util.List;

@ApplicationScoped
public class TodoRepository implements PanacheRepository<Todo> {

    public List<Todo> findByUserId(Long userId) {
        return list("userId", userId);
    }
}
