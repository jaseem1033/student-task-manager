package com.student.taskmanagement.service;

import com.student.taskmanagement.dto.TaskRequest;
import com.student.taskmanagement.dto.TaskResponse;
import com.student.taskmanagement.model.Task;

public interface TaskService {
    TaskResponse assignTask(TaskRequest request);
    void updateTaskStatus(Long taskId, String username, String status);
}
