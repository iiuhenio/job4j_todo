package ru.job4j.model;

import lombok.Data;
import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private LocalDateTime created = LocalDateTime.now();
    private boolean done;
    private boolean visible;
    public Task() {
    }

    public Task(Integer id, String description, LocalDateTime created, boolean done, boolean visible) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.done = done;
        this.visible = visible;
    }
}
