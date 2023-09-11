package ru.job4j.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Task;

import javax.persistence.criteria.CriteriaBuilder;
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
    private final Map<Integer, Task> tasksIsDone = new HashMap<>();
    private final Map<Integer, Task> tasksIsNotDone = new HashMap<>();

    private MemoryTaskRepository() {
        save(new Task(1, "Приготовить еду", "Для всей семьи", LocalDateTime.now(), false, true));
        save(new Task(2, "Выполнить работу", "Сделать сайт", LocalDateTime.now(), false, true));
        save(new Task(3, "Погулять с собакой", "Не забыть поводок", LocalDateTime.now(), true, true));
    }


    @Override
    public Task save(Task task) {
        task.setId(nextId.getAndIncrement());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public boolean deleteById(int id) {
        tasksIsDone.remove(id);
        tasksIsNotDone.remove(id);
        tasks.remove(id);
        return true;
    }

    @Override
    public boolean update(Task task) {
        return tasks.computeIfPresent(task.getId(), (id, oldTask) ->
                new Task(oldTask.getId(),
                        task.getName(),
                        task.getDescription(),
                        task.getCreated(),
                        task.isVisible(),
                        task.isDone())) != null;
    }

    @Override
    public boolean done(int id) {
        tasks.get(id).setDone(true);
        tasksIsNotDone.remove(id);
        return true;
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
    public Collection<Task> findIsDone() {
        Integer count = 1;
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            Task value = entry.getValue();
            if (value.isDone()) {
                tasksIsDone.put(count, value);
                count++;
            }
        }
        return tasksIsDone.values();
    }

    @Override
    public Collection<Task> findIsNotDone() {
        Integer count = 1;
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            Task value = entry.getValue();
            if (!value.isDone()) {
                tasksIsNotDone.put(count, value);
                count++;
            }
        }
        return tasksIsNotDone.values();
    }
}