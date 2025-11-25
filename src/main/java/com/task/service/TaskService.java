package com.task.service;

import com.task.domain.Task;
import com.task.domain.TaskStatus;
import com.task.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // 전체 목록 조회
    public List<Task> getTasks(String status, String sort, String keyword) {

        boolean sortByTargetDate = "targetDate".equals(sort);

        // 1) 🔍 keyword가 있을 때 → 무조건 제목 검색 우선
        if (keyword != null && !keyword.isBlank()) {
            List<Task> result = taskRepository.findByTitleContainingIgnoreCase(keyword);

            // 정렬 옵션도 가능하면 적용
            if (sortByTargetDate) {
                result.sort(Comparator.comparing(Task::getTargetDate,
                        Comparator.nullsLast(Comparator.naturalOrder())));
            }

            return result;
        }

        // 2) 🔥 keyword 없으면 기존 로직 그대로

        // 상태 필터 없을 때
        if (status == null || status.isEmpty()) {
            return sortByTargetDate
                    ? taskRepository.findAllByOrderByTargetDateAsc()
                    : taskRepository.findAll();
        }

        // 상태 필터 있을 때
        TaskStatus s = TaskStatus.valueOf(status);

        return sortByTargetDate
                ? taskRepository.findAllByStatusOrderByTargetDateAsc(s)
                : taskRepository.findByStatus(s);
    }   

    // 상태별 목록 조회
    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(TaskStatus.valueOf(status));
    }

    // 단건 조회
    public Task getTask(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) {
            throw new IllegalArgumentException("Task not found: " + id);
        }
        return optionalTask.get();
    }

    // 생성
    public Task createTask(Task data) {
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
    public Task updateTask(Long id, Task data) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));
    
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
    
    
    
    public void changeStatus(Long id, TaskStatus newStatus) {
        Task task = getTask(id);
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
    public void deleteTask(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) {
            throw new IllegalArgumentException("Task not found: " + id);
        }

        taskRepository.deleteById(id);
    }
}
