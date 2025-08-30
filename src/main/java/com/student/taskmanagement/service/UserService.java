package com.student.taskmanagement.service;

import com.student.taskmanagement.dto.RegisterRequest;
import com.student.taskmanagement.dto.StudentUsernameResponse;
import com.student.taskmanagement.dto.TaskResponse;
import com.student.taskmanagement.model.Task;
import com.student.taskmanagement.model.User;
import java.util.List;

public interface UserService {
    User registerUser(RegisterRequest request);
    List<TaskResponse> getTasksForUser(String username);
    void unregisterUser(String username);
    List<StudentUsernameResponse> getAllStudentUsers();
}
