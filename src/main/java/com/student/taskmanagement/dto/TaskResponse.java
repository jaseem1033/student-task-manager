package com.student.taskmanagement.dto;

import lombok.Data;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String assignedToUsername;
}
