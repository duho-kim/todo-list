package com.task.repository;

import com.task.domain.Task;
import com.task.domain.TaskStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findAllByOrderByTargetDateAsc();
    List<Task> findAllByStatusOrderByTargetDateAsc(TaskStatus status);
    List<Task> findByTitleContainingIgnoreCase(String keyword);
}
