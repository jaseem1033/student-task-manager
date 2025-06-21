package com.student.taskmanagement.repository;

import com.student.taskmanagement.model.Task;
import com.student.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(User user);
}
