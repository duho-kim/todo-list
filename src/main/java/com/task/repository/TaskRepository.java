package com.task.repository;

import com.task.domain.Task;
import com.task.domain.TaskStatus;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // List<Task> findByStatus(TaskStatus status);
    // List<Task> findAllByOrderByTargetDateAsc();
    // List<Task> findAllByStatusOrderByTargetDateAsc(TaskStatus status);
    // List<Task> findByTitleContainingIgnoreCase(String keyword);

    List<Task> findByUserToken(String userToken);
    List<Task> findByUserTokenAndStatus(String userToken, TaskStatus status);
    List<Task> findByUserTokenAndStatusOrderByTargetDateAsc(String userToken, TaskStatus status);
    List<Task> findByUserTokenAndTitleContainingIgnoreCase(String userToken, String keyword);
    Optional<Task> findByIdAndUserToken(Long id, String userToken);
    List<Task> findByUserTokenOrderByTargetDateAsc(String userToken);
}
