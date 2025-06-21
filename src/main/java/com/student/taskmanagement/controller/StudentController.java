package com.student.taskmanagement.controller;

import com.student.taskmanagement.dto.RegisterRequest;
import com.student.taskmanagement.dto.TaskResponse;
import com.student.taskmanagement.model.User;
import com.student.taskmanagement.service.TaskService;
import com.student.taskmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final UserService userService;
    private final TaskService taskService;

    //Register new Student...
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody @Valid RegisterRequest request) {
        request.setRole("STUDENT");
        if(!request.getRole().equalsIgnoreCase("STUDENT"))
            return ResponseEntity.badRequest().body(null);
        else
            return ResponseEntity.ok(userService.registerUser(request));
    }

    //Student views their own tasks...
    @GetMapping("/tasks")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<TaskResponse>> getTasksForUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getTasksForUser(authentication.getName()));
    }

    //Student Updates Task Status
    @PostMapping("/tasks/{task_id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> updateTaskStatus(@PathVariable Long task_id, @RequestParam String status, Authentication authentication) {
        taskService.updateTaskStatus(task_id, authentication.getName(), status);
        return new ResponseEntity<>("Task status updated successfully", HttpStatus.OK);
    }
}
