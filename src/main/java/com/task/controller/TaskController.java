package com.task.controller;

import com.task.domain.Task;
import com.task.domain.TaskStatus;
import com.task.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // 사용자 식별 토큰(쿠키)
    private String userToken(HttpServletRequest request, HttpServletResponse response) {
        String token = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("user_token".equals(c.getName())) {
                    token = c.getValue();
                    break;
                }
            }
        }

        // 쿠키 발급
        if (token == null) {
            token = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("user_token", token);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 365); // 1년
            response.addCookie(cookie);
        }

        return token;
    }

    // 목록 페이지
    @GetMapping
    public String list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String keyword,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        String token = userToken(request, response);

        model.addAttribute("tasks", taskService.getTasks(status, sort, keyword, token));
        model.addAttribute("keyword", keyword);

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
    public String create(
            @ModelAttribute Task task,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String token = userToken(request, response);
        taskService.createTask(task, token);
        return "redirect:/tasks";
    }

    // 상세 페이지
    @GetMapping("/{id}")
    public String detail(
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        String token = userToken(request, response);

        Task task = taskService.getTask(id, token);
        model.addAttribute("task", task);

        return "tasks/detail";
    }

    // 수정 폼 페이지
    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {
        String token = userToken(request, response);

        Task task = taskService.getTask(id, token);
        model.addAttribute("task", task);
        model.addAttribute("statuses", TaskStatus.values());

        return "tasks/form";
    }

    // 실제 수정 처리
    @PostMapping("/{id}/update")
    public String update(
            @PathVariable Long id,
            @ModelAttribute Task task,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String token = userToken(request, response);
        taskService.updateTask(id, task, token);

        return "redirect:/tasks";
    }

    // 상태 변경
    @PostMapping("/{id}/status")
    public String changeStatus(
            @PathVariable Long id,
            @RequestParam String status,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String token = userToken(request, response);

        taskService.changeStatus(id, TaskStatus.valueOf(status), token);

        return "redirect:/tasks";
    }

    // 삭제 처리
    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String token = userToken(request, response);

        taskService.deleteTask(id, token);

        return "redirect:/tasks";
    }
}
