package com.task.service;

import com.task.domain.Task;
import com.task.domain.TaskStatus;
import com.task.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // 전체 목록 조회
    public List<Task> getTasks(String status, String sort, String keyword, String userToken) {

        boolean sortByTargetDate = "targetDate".equals(sort);

        if (keyword != null && !keyword.isBlank()) {
            List<Task> result = taskRepository.findByUserTokenAndTitleContainingIgnoreCase(userToken, keyword);

            if (sortByTargetDate) {
                result.sort(Comparator.comparing(Task::getTargetDate,
                        Comparator.nullsLast(Comparator.naturalOrder())));
            }

            return result;
        }

        // 상태 필터 없을 때
        if (status == null || status.isEmpty()) {
            return sortByTargetDate
                    ? taskRepository.findByUserTokenOrderByTargetDateAsc(userToken)
                    : taskRepository.findByUserToken(userToken);
        }

        // 상태 필터 있을 때
        TaskStatus s = TaskStatus.valueOf(status);

        return sortByTargetDate
                ? taskRepository.findByUserTokenAndStatusOrderByTargetDateAsc(userToken, s)
                : taskRepository.findByUserTokenAndStatus(userToken, s);
    }   

    // 상태별 목록 조회
    public List<Task> getTasksByStatus(String status, String userToken) {
        TaskStatus s = TaskStatus.valueOf(status);
        return taskRepository.findByUserTokenAndStatus(userToken, s);
    }
    

    // 단건 조회
    public Task getTask(Long id, String userToken) {
        return taskRepository.findByIdAndUserToken(id, userToken)
        .orElseThrow(() -> new IllegalArgumentException("Task not found or unauthorized: " + id));
    }

    // 생성
    public Task createTask(Task data, String userToken) {
        data.setUserToken(userToken);
        // DONE 처리
        if (data.getStatus() == TaskStatus.DONE) {
            if (data.getCompleteDate() != null) {
            }
            else {
                data.setCompleteDate(LocalDate.now());
            }
        }
    
        return taskRepository.save(data);
    }
    
    

    // 수정
    public Task updateTask(Long id, Task data, String userToken) {
        Task task = getTask(id, userToken);

        // 이전 상태 저장
        TaskStatus oldStatus = task.getStatus();
        TaskStatus newStatus = data.getStatus();
    
        // 필드 업데이트
        task.setTitle(data.getTitle());
        task.setDescription(data.getDescription());
        task.setStatus(newStatus);
        task.setTargetDate(data.getTargetDate());
    
        // DONE 처리
        if (newStatus == TaskStatus.DONE) {
            if (data.getCompleteDate() != null) {
                task.setCompleteDate(data.getCompleteDate());
            }

            else if (oldStatus != TaskStatus.DONE || task.getCompleteDate() == null) {
                task.setCompleteDate(LocalDate.now());
            }
        } else {
            task.setCompleteDate(null);
        }
    
        return taskRepository.save(task);
    }
    
    
    // 상태 변경
    public void changeStatus(Long id, TaskStatus newStatus, String userToken) {
        Task task = getTask(id, userToken);
        TaskStatus oldStatus = task.getStatus();

        // 상태 변경 적용
        task.setStatus(newStatus);

        // DONE으로 변경되면 완료일 자동 생성
        if (newStatus == TaskStatus.DONE && oldStatus != TaskStatus.DONE) {
            task.setCompleteDate(LocalDate.now());
        }

        // DONE에서 벗어나면 완료일 제거
        if (oldStatus == TaskStatus.DONE && newStatus != TaskStatus.DONE) {
            task.setCompleteDate(null);
        }

        taskRepository.save(task);
    }

    // 삭제
    public void deleteTask(Long id, String userToken) {
        Task task = getTask(id, userToken);
        taskRepository.delete(task);
    }
}
