package com.student.taskmanagement.service;

import com.student.taskmanagement.dto.TaskRequest;
import com.student.taskmanagement.dto.TaskResponse;
import com.student.taskmanagement.model.Task;
import com.student.taskmanagement.model.TaskStatus;
import com.student.taskmanagement.model.User;
import com.student.taskmanagement.repository.TaskRepository;
import com.student.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public TaskResponse assignTask(TaskRequest request) {

        User assignedUser = userRepository.findByUsername(request.getAssignedToUsername()).orElseThrow(()-> new RuntimeException("Assigned user not found"));
        if(assignedUser.getRole().name().equalsIgnoreCase("ADMIN"))
            throw new RuntimeException("You are not authorized to assign task to Admin");

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setAssignedTo(assignedUser);
        task.setStatus(TaskStatus.PENDING);
        Task savedTask = taskRepository.save(task);
        return mapToTaskResponse(savedTask);
    }

    @Override
    public void updateTaskStatus(Long taskId, String username, String status) {
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new RuntimeException("Task not found"));
        if(!task.getAssignedTo().getUsername().equals(username)){
            throw new RuntimeException("You are not authorized to update this task");
        }
        try {
            task.setStatus(Enum.valueOf(TaskStatus.class,status.toUpperCase()));
            taskRepository.save(task);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Status : "+ status);
        }
    }
    public TaskResponse mapToTaskResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription((task.getDescription()));
        response.setStatus(task.getStatus().name());
        response.setAssignedToUsername(task.getAssignedTo().getUsername());
        return response;
    }

}
