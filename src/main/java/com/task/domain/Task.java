package com.task.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false)
    private String title; // 제목

    @Column(columnDefinition = "TEXT")
    private String description; // 설명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status; // TODO / DOING / DONE

    @Column(nullable = false)
    private LocalDateTime createdAt; // 생성 시간

    @Column
    private LocalDate targetDate;   // 목표일

    @Column
    private LocalDate completeDate; // 완료일

    public Task() {
        // JPA 기본 생성자
    }
    
    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = TaskStatus.TODO;
        }
    }

    @Column(name = "user_token")
    private String userToken; // 토큰

    // Getter / Setter

    // public Long getId() {
    //     return id;
    // }

    // public void setId(Long id) {
    //     this.id = id;
    // }

    // public String getTitle() {
    //     return title;
    // }

    // public void setTitle(String title) {
    //     this.title = title;
    // }

    // public String getDescription() {
    //     return description;
    // }

    // public void setDescription(String description) {
    //     this.description = description;
    // }

    // public TaskStatus getStatus() {
    //     return status;
    // }

    // public void setStatus(TaskStatus status) {
    //     this.status = status;
    // }

    // public LocalDateTime getCreatedAt() {
    //     return createdAt;
    // }

    // public void setCreatedAt(LocalDateTime createdAt) {
    //     this.createdAt = createdAt;
    // }

    // public LocalDate getTargetDate() {
    //     return targetDate;
    // }

    // public void setTargetDate(LocalDate targetDate) {
    //     this.targetDate = targetDate;
    // }

    // public LocalDate getCompleteDate() {
    //     return completeDate;
    // }

    // public void setCompleteDate(LocalDate completeDate) {
    //     this.completeDate = completeDate;
    // }
}
