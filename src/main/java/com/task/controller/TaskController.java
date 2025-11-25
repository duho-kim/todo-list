package com.task.controller;

import com.task.domain.Task;
import com.task.domain.TaskStatus;
import com.task.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // 목록 페이지
    @GetMapping
    public String list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        model.addAttribute("tasks", taskService.getTasks(status, sort, keyword));
        model.addAttribute("keyword", keyword); // 검색창 유지용
        return "tasks/list";
    }
    

    // 생성 폼 페이지
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("statuses", TaskStatus.values());
        return "tasks/form";
    }

    // 실제 생성 처리
    @PostMapping
    public String create(@ModelAttribute Task task) {
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    // 상세 페이지
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Task task = taskService.getTask(id);
        model.addAttribute("task", task);
        return "tasks/detail";
    }
    
    // 수정 폼 페이지
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Task task = taskService.getTask(id);
        model.addAttribute("task", task);
        model.addAttribute("statuses", TaskStatus.values());
        return "tasks/form";
    }

    // 실제 수정 처리
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute Task task) {
        taskService.updateTask(id, task);
        return "redirect:/tasks";
    }

    // 상태 변경
    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam String status) {
        taskService.changeStatus(id, TaskStatus.valueOf(status));
        return "redirect:/tasks";
    }
    
    // 삭제 처리
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
}
