package ru.job4j.repository;

import ru.job4j.model.Task;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryTaskRepository implements TaskRepository {

    private static final MemoryTaskRepository INSTANCE = new MemoryTaskRepository();

    private int nextId = 1;

    private final Map<Integer, Task> tasks = new HashMap<>();

    private MemoryTaskRepository() {
        save(new Task(1, "Description", LocalDateTime.now(), false));
        save(new Task(2, "Description2", LocalDateTime.now(), false));
        save(new Task(3, "Description3", LocalDateTime.now(), true));

}

    public static MemoryTaskRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Task save(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public boolean deleteById(int id) {
        return tasks.remove(id) != null;
    }

    @Override
    public boolean update(Task task) {
        return tasks.computeIfPresent(task.getId(), (id, oldTask) ->
                new Task(oldTask.getId(),
                        task.getDescription(),
                        task.getCreated(),
                        task.isDone())) != null;
    }

    @Override
    public Optional<Task> findById(int id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public Collection<Task> findAll() {
        return tasks.values();
    }

}