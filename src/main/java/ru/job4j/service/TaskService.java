package ru.job4j.service;

import ru.job4j.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {

    Task save(Task task);

    boolean deleteById(int id);

    boolean update(Task task);

    Optional<Task> findById(int id);

    Collection<Task> findAll();

    Collection<Task> findIsDone();

    Collection<Task> findIsNotDone();

    boolean done(int id);

}