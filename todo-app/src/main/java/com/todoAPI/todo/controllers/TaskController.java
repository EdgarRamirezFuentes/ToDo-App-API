package com.todoAPI.todo.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import java.util.stream.Collectors;


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
    public ResponseEntity<Object> getTasks(
            @RequestParam(name="name", required = false, defaultValue = "") String name,
            @RequestParam(name="priority", required = false, defaultValue = "0") Integer priority,
            @RequestParam(name="isDone", required = false, defaultValue = "false") Boolean isDone,
            @RequestParam(name="sortByDate", required = false, defaultValue = "false") Boolean sortByDate,
            @RequestParam(name="sortByPriority", required = false, defaultValue = "false") Boolean sortByPriority,
            @RequestParam(name="page", required = false, defaultValue = "1") Integer page
        ) {                                
        ArrayList<Task> taskList = new ArrayList<>(tasks.values());

        // Filter the tasks by name, priority and isDone.
        if (!name.equals("")) taskList.removeIf(task -> !task.getName().toLowerCase().contains(name.toLowerCase()));   
        if (priority != 0) taskList.removeIf(task -> task.getPriority() != priority);
        if (isDone != false) taskList.removeIf(task -> task.getIsDone() != isDone);
        //TODO: Sort the tasks by date and priority.
        if (sortByDate != false && taskList.size() > 1) taskList.sort((task1, task2) -> task1.getDueDate().compareTo(task2.getDueDate()));
        if (sortByPriority != false && taskList.size() > 1) taskList.sort((task1, task2) -> task2.getPriority() - task1.getPriority());

        // Paginate the tasks.
        int pageSize = 10;
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, taskList.size());
        if (fromIndex >= taskList.size()) return ResponseEntity.notFound().build();
        ArrayList<Task> paginatedTasks = new ArrayList<>(taskList.subList(fromIndex, toIndex));
        
        // Return the total tasks and the paginated tasks.
        HashMap<String, Object> response = new HashMap<>();
        response.put("totalTasks", taskList.size());
        response.put("tasks", paginatedTasks);
        
        return ResponseEntity.ok(response);
    }

    /**
     * This method register a new task.
     * @param: name - The name of the task.
     * @param: priority - The priority of the task.
     * @param: dueDate - The due date of the task (optional).
     * @version: 1.0
     * @return: task - The task with the given id.
     */
    @PostMapping("/task")
    public ResponseEntity<Object> createTask(@RequestBody Task task) {
        // Name and priority validation.
        if (task.getName().equals("")) return ResponseEntity.badRequest().body("The name of the task is required.");
        if (task.getName().length() > 120) return ResponseEntity.badRequest().body("The name of the task must be less than 121 characters.");
        if (this.priorities.get(task.getPriority()) == null) return ResponseEntity.badRequest().body("The priority of the task is invalid.");
        task.setPriorityName(this.priorities.get(task.getPriority()));

        // Date validation.
        if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now()))
            return ResponseEntity.badRequest().body("The due date must be equals of after today.");

        // Add the task to the tasks list.
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

    /**
     * This method updates a task with the given id.
     * @param: id - The id of the task.
     * @param: name - The name of the task.
     * @param: priority - The priority of the task.
     * @param: dueDate - The due date of the task (optional).
     * @version: 1.0
     * @return: task - The task with the given id.
     */
    @PutMapping("/task/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable("id") int id, @RequestBody Task task) {
        Task taskToUpdate = tasks.get(id);
        if (taskToUpdate == null) return ResponseEntity.notFound().build();

        // Name and priority validation.
        if (task.getName().equals("")) return ResponseEntity.badRequest().body("The name of the task is required.");
        if (task.getName().length() > 120) return ResponseEntity.badRequest().body("The name of the task must be less than 121 characters.");
        if (this.priorities.get(task.getPriority()) == null) return ResponseEntity.badRequest().body("The priority of the task is invalid.");
        task.setPriorityName(this.priorities.get(task.getPriority()));

        // Date validation.
        if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now()))
            return ResponseEntity.badRequest().body("The due date must be equals of after today.");

        // Update the task.
        taskToUpdate.setName(task.getName());
        taskToUpdate.setPriority(task.getPriority());
        taskToUpdate.setPriorityName(task.getPriorityName());
        taskToUpdate.setDueDate(task.getDueDate());
        taskToUpdate.setIsDone(task.getIsDone());

        return ResponseEntity.ok(taskToUpdate);
    }
    
    /**
     * This method deletes a task with the given id.
     * @param: id - The id of the task.
     * @version: 1.0
     * @return: task - The task with the given id.
     */
    @DeleteMapping("/task/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable("id") int id) {
        Task task = tasks.get(id);
        if (task == null) return ResponseEntity.notFound().build();
        tasks.remove(id);
        return ResponseEntity.ok(task);
    }

    /**
     * This method marks a task as done.
     * @param: id - The id of the task.
     * @version: 1.0
     * @return: task - The task with the given id.
     */
    @PostMapping("/task/{id}/done")
    public ResponseEntity<Object> markTaskAsDone(@PathVariable("id") int id) {
        Task task = tasks.get(id);
        if (task == null) return ResponseEntity.notFound().build();
        task.setIsDone(true);
        task.setDoneDate(LocalDateTime.now());
        return ResponseEntity.ok(task);
    }

    /**
     * This method marks a task as undone.
     * @param: id - The id of the task.
     * @version: 1.0
     * @return: task - The task with the given id.
     */
    @PutMapping("/task/{id}/undone")
    public ResponseEntity<Object> markTaskAsUndone(@PathVariable("id") int id) {
        Task task = tasks.get(id);
        if (task == null) return ResponseEntity.notFound().build();
        task.setIsDone(false);
        task.setDoneDate(null);
        return ResponseEntity.ok(task);
    }

    /**
     * This method calculates the average time of doing the tasks 
     * grouped by priority.
     * @version: 1.0
     */
    @GetMapping("/task/average")
    public ResponseEntity<Object> getAverageTime() {
        // Get the tasks grouped by priority.
        Map<String, List<Task>> tasksByPriority = tasks.values()
                                                        .stream()
                                                        .collect(
                                                            Collectors.groupingBy(Task::getPriorityName)
                                                        );

        // Calculate the average time in mintues of doing the tasks grouped by priority.
        HashMap<String, Double> response = new HashMap<>();
        for (Map.Entry<String, List<Task>> entry : tasksByPriority.entrySet()) {
            List<Task> tasks = entry.getValue();
            double averageTime = tasks.stream().mapToDouble(task -> {
                if (task.getDoneDate() == null) return 0;
                return ChronoUnit.MINUTES.between(
                    task.getCreatedDate(), 
                    task.getDoneDate()
                );
            }).average().orElse(0);
            response.put(entry.getKey(), averageTime);
        }
        return ResponseEntity.ok(response);
    }
}
