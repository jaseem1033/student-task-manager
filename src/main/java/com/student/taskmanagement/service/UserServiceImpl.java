package com.student.taskmanagement.service;

import com.student.taskmanagement.dto.RegisterRequest;
import com.student.taskmanagement.dto.StudentUsernameResponse;
import com.student.taskmanagement.dto.TaskResponse;
import com.student.taskmanagement.model.Role;
import com.student.taskmanagement.model.Task;
import com.student.taskmanagement.model.User;
import com.student.taskmanagement.repository.TaskRepository;
import com.student.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    //Get Users
    @Override
    public List<StudentUsernameResponse> getAllStudentUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole().name().equalsIgnoreCase("STUDENT"))
                .map(user -> new StudentUsernameResponse(user.getUsername()))
                .collect(Collectors.toList());
    }

    //Register User
    @Override
    public User registerUser(RegisterRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        return userRepository.save(user);
    }

    //Delete User
    @Override
    public void unregisterUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
        List<Task> tasks = taskRepository.findByAssignedTo(user);
        taskRepository.deleteAll();
        userRepository.delete(user);
    }

    @Override
    public List<TaskResponse> getTasksForUser(String username) {
            User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
            List<Task> taskList = taskRepository.findByAssignedTo(user);
            return taskList.stream()
                    .map(this::mapToTaskResponse)
                    .collect(Collectors.toList());
    }

    private TaskResponse mapToTaskResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription((task.getDescription()));
        response.setStatus(task.getStatus().name());
        response.setAssignedToUsername(task.getAssignedTo().getUsername());
        return response;
    }
}

