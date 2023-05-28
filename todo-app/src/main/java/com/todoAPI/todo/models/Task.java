package com.todoAPI.todo.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.Serializable; 

/**
 * This class represents a task, and it simulates the model of a task.
 * @author: Edgar Ramirez
 * @version: 1.0
 * @param: name - The name of the task.
 * @param: dueDate - The due date of the task.
 */
public class Task implements Serializable {
    public static int id = 0;
    private int taskId = id;
    private String name;
    private LocalDateTime dueDate;
    private int priority;
    private String priorityName;
    private boolean isDone;
    private LocalDateTime doneDate;
    private LocalDateTime createdDate;

    // Constructors
    public Task() {
        this.id += 1;
        this.taskId = id;
        this.createdDate = LocalDateTime.now();
    }

    /**
     * This constructor creates a task with the given name and due date.
     * @param: name - The name of the task.
     * @param: dueDate - The due date of the task.
     * @param: priority - The priority of the task.
     * @param: priorityName - The priority name of the task.
     * @version: 1.0`
    */
    public Task(String name, LocalDateTime dueDate, int priority, String priorityName) {
        this.id += 1;
        this.taskId = id;
        this.name = name;
        this.dueDate = dueDate;
        this.priority = priority;
        this.priorityName = priorityName;
        this.isDone = false;
        this.doneDate = null;
        this.createdDate = LocalDateTime.now();
    }

    // Getters

    /**
     * This method returns the id of the task.
     * @version: 1.0
     * @return: taskId - The id of the task.
     */
    public int getId() {
        return this.taskId;
    }

    /**
     * This method returns the name of the task.
     * @version: 1.0
     * @return: name - The name of the task.
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method returns the due date of the task.
     * @version: 1.0
     * @return: dueDate - The due date of the task.
     */
    public LocalDateTime getDueDate() {
        return this.dueDate;
    }

    /**
     * This method returns the priority id of the task.
     * @version: 1.0
     * @return: priorityId - The priority id of the task.
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * This method returns the priority of the task.
     * @version: 1.0
     * @return: priority - The priority of the task.
     */
    public String getPriorityName() {
        return this.priorityName;
    }

    /**
     * This method returns the status of the task.
     * @version: 1.0
     * @return: isDone - The status of the task.
     */
    public boolean getIsDone() {
        return this.isDone;
    }

    /**
     * This method returns the done date of the task.
     * @version: 1.0
     * @return: doneDate - The done date of the task.
     */
    public LocalDateTime getDoneDate() {
        return this.doneDate;
    }

    /**
     * This method returns the created date of the task.
     * @version: 1.0
     * @return: createdDate - The created date of the task.
     */
    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }


    // Setters
    /**
     * This method sets the name of the task.
     * @param: name - The name of the task.
     * @version: 1.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method sets the due date of the task.
     * @param: dueDate - The due date of the task.
     * @version: 1.0
     */
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * This method sets the priority id of the task.
     * @param: priorityId - The priority id of the task.
     * @version: 1.0
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * This method sets the priority of the task.
     * @param: priority - The priority of the task.
     * @version: 1.0
     */
    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    /**
     * This method sets the status of the task.
     * @param: isDone - The status of the task.
     * @version: 1.0
     */
    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * This method sets the done date of the task.
     * @param: doneDate - The done date of the task.
     * @version: 1.0
     */
    public void setDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
    }


    // Methods
    /**
     * This method marks the task as done.
     * @version: 1.0
     */
    public void markAsDone() {
        this.isDone = true;
        this.doneDate = LocalDateTime.now();
    }

    /**
     * This method marks the task as undone.
     * @version: 1.0
     */
    public void markAsUndone() {
        this.isDone = false;
        this.doneDate = null;
    }
}
