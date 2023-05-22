package com.todoAPI.todo.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.ArrayList;;

import com.todoAPI.todo.models.Task;

@RestController
@RequestMapping("/api/v1/todo")
public class TaskController {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, String> priorities = new HashMap<>();

    public TaskController() {
        priorities.put(1, "Low");
        priorities.put(2, "Medium");
        priorities.put(3, "High");
    }

    @GetMapping("/")
    public String index() {
        return "Hello World";
    }

    /**
     * This method returns the priorities of the tasks.
     * @version: 1.0
     * @return: priorities - The priorities of the tasks.
     */
    @GetMapping("/priorities")
    public ResponseEntity<Object> getPriorities() {
        return ResponseEntity.ok(this.priorities);
    }

    /**
     * This method returns all the tasks.
     * @version: 1.0
     * @return: tasks - The tasks.
     */
    @GetMapping("/task")
    public ResponseEntity<Object> getTasks(@RequestParam(name="name", required = false) String name,
                                            @RequestParam(name="priority", required = false) Integer priority,
                                            @RequestParam(name="isDone", required = false) Boolean isDone,
                                            @RequestParam(name="sortByDate", required = false) Boolean sortByDate,
                                            @RequestParam(name="sortByPriority", required = false) Boolean sortByPriority
                                            ) {                                
        ArrayList<Task> response = new ArrayList<>(tasks.values());

        // Filter the tasks by name, priority and isDone.
        if (name != null) response.removeIf(task -> !task.getName().toLowerCase().contains(name.toLowerCase()));   
        if (priority != 0) response.removeIf(task -> task.getPriority() != priority);
        if (isDone != null) response.removeIf(task -> task.getIsDone() != isDone);
        //TODO: Sort the tasks by date and priority.
        return ResponseEntity.ok(response);
    }

    /**
     * This method register a new task.
     * @param: name - The name of the task.
     * @param: priority - The priority of the task.
     * @param: priorityName - The priority name of the task.
     * @param: dueDate - The due date of the task.
     * @version: 1.0
     * @return: task - The task with the given id.
     */
    @PostMapping("/task")
    public ResponseEntity<Object> createTask(@RequestBody Task task) {
        tasks.put(task.getId(), task);
        return ResponseEntity.ok(task);
    }

    /**
     * This method returns a task with the given id.
     * @param: id - The id of the task.
     * @version: 1.0
     * @return: task - The task with the given id.
     */
    @GetMapping("/task/{id}")
    public ResponseEntity<Object> getTask(@PathVariable("id") int id) {
        Task task = tasks.get(id);
        if (task == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(task);
    }
}
