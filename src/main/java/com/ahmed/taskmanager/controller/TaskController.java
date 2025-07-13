package com.ahmed.taskmanager.controller;

import com.ahmed.taskmanager.exception.TaskNotFoundException;
import com.ahmed.taskmanager.model.Task;
import com.ahmed.taskmanager.repository.TaskRepository;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.List;

@RestController
@RequestMapping("/tasks")


public class TaskController {
    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(TaskNotFoundException.class)
        public ResponseEntity<String> handleTaskNotFound(TaskNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.deleteById(id);
    }

    @PutMapping("/{id}/complete")
    public Task markTaskComplete(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));


        task.setCompleted(true);
        return taskRepository.save(task);
    }

    @PutMapping("/{id}/update")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));


        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());

        return taskRepository.save(task);
    }

    @PutMapping("/{id}/toggle")
    public Task toggleTask(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));


        task.setCompleted(!task.isCompleted());
        return taskRepository.save(task);
    }

    @GetMapping("/completed")
    public List<Task> getCompletedTasks() {
        return taskRepository.findByCompleted(true);
    }

    @GetMapping("/incomplete")
    public List<Task> getIncompleteTasks() {
        return taskRepository.findByCompleted(false);
    }

    @GetMapping("/search")
    public List<Task> searchTasks(@RequestParam String keyword) {
        return taskRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

    @GetMapping("/sorted")
    public List<Task> getTasksSortedByDueDate() {
        return taskRepository.findAll(Sort.by("dueDate").ascending());
    }





}
