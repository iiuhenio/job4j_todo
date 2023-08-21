package ru.job4j.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Task;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryTaskRepository implements TaskRepository {

    private AtomicInteger nextId = new AtomicInteger(1);

    private final Map<Integer, Task> tasks = new HashMap<>();

    private MemoryTaskRepository() {
        save(new Task(1, "Description", LocalDateTime.now(), false, true));
        save(new Task(2, "Description2", LocalDateTime.now(), false, true));
        save(new Task(3, "Description3", LocalDateTime.now(), true, true));
    }


    @Override
    public Task save(Task task) {
        task.setId(nextId.getAndIncrement());
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
                        task.isVisible(),
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

    @Override
    public boolean isDone(Task task) {
        return tasks.computeIfPresent(task.getId(), (id, oldTask) ->
                new Task(oldTask.getId(),
                        task.getDescription(),
                        task.getCreated(),
                        task.isVisible(),
                        task.isDone())) != null;
    }

}