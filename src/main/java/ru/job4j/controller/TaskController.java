package ru.job4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Task;
import ru.job4j.service.TaskService;

@Controller
@RequestMapping("/tasks") /* Работать с задачами будем по URI /tasks/** */
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task) {
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model) {
        var isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Задача с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }
}