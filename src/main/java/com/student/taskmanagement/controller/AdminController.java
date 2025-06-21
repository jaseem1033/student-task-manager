package com.student.taskmanagement.controller;

import com.student.taskmanagement.dto.RegisterRequest;
import com.student.taskmanagement.dto.StudentUsernameResponse;
import com.student.taskmanagement.dto.TaskRequest;
import com.student.taskmanagement.dto.TaskResponse;
import com.student.taskmanagement.model.User;
import com.student.taskmanagement.service.TaskService;
import com.student.taskmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final TaskService taskService;

    //Register new Admin...
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> registerUser(@RequestBody @Valid RegisterRequest request) {
        request.setRole("ADMIN");
        if (!"ADMIN".equalsIgnoreCase(request.getRole()))
            throw new RuntimeException("Only ADMIN can register");
        else
            return ResponseEntity.ok(userService.registerUser(request));
    }

    //Delete Student User...
    @DeleteMapping("/register/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> unregisterUser(@PathVariable String username) {
        userService.unregisterUser(username);
        return ResponseEntity.ok("User deleted successfully");
    }

    //Assign new Task to Student...
    @PostMapping("/tasks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> assignTask(@RequestBody @Valid TaskRequest request) {
        return new ResponseEntity<>(taskService.assignTask(request), HttpStatus.OK);
    }

    //Get Tasks of a Student...
    @GetMapping("/tasks/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskResponse>> getTasksForUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getTasksForUser(username));
    }

    //Get All Users
    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StudentUsernameResponse>> getAllStudentUsers() {
        return ResponseEntity.ok(userService.getAllStudentUsers());
    }
}
