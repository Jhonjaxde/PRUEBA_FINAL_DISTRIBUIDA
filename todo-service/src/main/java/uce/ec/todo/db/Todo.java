package uce.ec.todo.db;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "todos")
public class Todo extends PanacheEntity {
    
    public Long userId;
    public String title;
    public Boolean completed;

    public Todo() {
    }

    public Todo(Long userId, String title, Boolean completed) {
        this.userId = userId;
        this.title = title;
        this.completed = completed;
    }
}



