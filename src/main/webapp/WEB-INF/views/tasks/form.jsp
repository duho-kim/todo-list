<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<html>
<head>
    <title>Task Form</title>

    <!-- Bootstrap -->
    <link 
        rel="stylesheet" 
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
    />

    <style>
        body {
            background: #f5f7fa;
            padding: 40px;
        }
    </style>
</head>

<body class="container d-flex justify-content-center">
    <div style="max-width: 800px; width: 100%;">
        <!-- 제목 -->
        <h2 class="mb-4">
            📝 ${task.id == null ? '생성' : '수정'}
        </h2>

        <!-- 카드 형태 -->
        <div class="card shadow-sm">
            <div class="card-body">
        
                <c:choose>
                    <c:when test="${task.id == null}">
                        <form action="/tasks" method="post">
                    </c:when>
                    <c:otherwise>
                        <form action="/tasks/${task.id}/update" method="post">
                    </c:otherwise>
                </c:choose>
        
                    <!-- 제목 -->
                    <div class="mb-3">
                        <label class="form-label">제목</label>
                        <input type="text" name="title" value="${task.title}"
                               class="form-control" required />
                    </div>
        
                    <!-- 설명 -->
                    <div class="mb-3">
                        <label class="form-label">설명</label>
                        <textarea name="description" rows="5"
                                  class="form-control">${task.description}</textarea>
                    </div>
        
                    <!-- 🔥 목표일 + 상태 한 줄로 정렬 -->
                    <div class="row">
                        <!-- 목표일 -->
                        <div class="col-md-6 mb-3">
                            <label class="form-label">목표일</label>
                            <input type="date" name="targetDate"
                                   value="${task.targetDate != null ? task.targetDate : ''}"
                                   class="form-control">
                        </div>

                        <!-- 상태 -->
                        <div class="col-md-6 mb-3">
                            <label class="form-label">상태</label>
                            <select name="status" class="form-select">
                                <c:forEach var="st" items="${statuses}">
                                    <option value="${st}" ${st == task.status ? 'selected' : ''}>
                                        ${st}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <!-- 완료일 (DONE일 때만 표시) -->
                    <div class="mb-3" id="completeDateRow" style="display:none;">
                        <label class="form-label">완료일</label>
                        <input type="date" name="completeDate"
                            value="${task.completeDate != null ? task.completeDate : ''}"
                            class="form-control">
                    </div>
                    
                    <!-- 버튼 -->
                    <div class="d-flex gap-3 mt-3">
                        <button type="submit" class="btn btn-primary px-4">저장</button>
                        <a href="/tasks" class="btn btn-secondary px-4">목록으로</a>
                    </div>
        
                </form>
            </div>
        </div>        
    </div>

    <script>
        function completeDate() {
            const status = document.querySelector("select[name='status']").value;
            const completeRow = document.getElementById("completeDateRow");
    
            if (status === "DONE") {
                completeRow.style.display = "block";
            } else {
                completeRow.style.display = "none";
            }
        }
    
        // 페이지 로딩 시 상태가 DONE이면 자동 표시
        window.onload = completeDate;
    
        // 상태 변경될 때 실행
        document.querySelector("select[name='status']")
                .addEventListener("change", completeDate);
    </script>
    
</body>
</html>
