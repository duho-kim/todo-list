<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Todo List</title>

    <!-- Bootstrap 5 -->
    <link
        rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
    />

    <style>
        body {
            background: #f5f7fa;
            padding: 30px;
        }

        /* 상태 색상 */
        .status-TODO  { color: #0d6efd !important; font-weight: 600; }
        .status-DOING { color: #fd7e14 !important; font-weight: 600; }
        .status-DONE  { color: #198754 !important; font-weight: 600; }

        /* 탭 스타일 */
        .task-tabs {
            display: flex;
            gap: 15px;
        }
        .task-tab-btn {
            padding: 8px 18px;
            border: 1px solid #ddd;
            background: white;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: 0.2s;
        }
        .task-tab-btn:hover {
            background: #e9ecef;
        }
        .task-tab-active {
            background: #0d6efd;
            color: white;
            border-color: #0d6efd;
        }

        /* 열 비율 */
        .col-no         { width: 6%; }
        .col-title      { width: 38%; }
        .col-target     { width: 14%; }
        .col-complete   { width: 14%; }
        .col-status     { width: 9%; }
        .col-statuschg  { width: 16%; white-space: nowrap; }
        .col-delete     { width: 5%; white-space: nowrap; }

        .table thead th button {
            color: white !important;
            text-decoration: none;
        }

        /* 테이블 안 폼 아래쪽 기본 마진 제거 (세로정렬 깔끔하게) */
        td form {
            margin-bottom: 0 !important;
        }

        /* 상단 바 정렬용 */
        .top-bar {
            min-height: 48px;
        }
        .top-bar form {
            margin-bottom: 0;
        }
    </style>
</head>

<body class="container d-flex justify-content-center">
    <div style="max-width: 800px; width: 100%;">

        <!-- 상단 타이틀 -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1 class="m-0">📌 Todo 리스트</h1>
        </div>

        <!-- ✅ 탭 + 검색 + 추가 버튼 (상하 가운데 정렬 버전) -->
        <div class="top-bar d-flex align-items-center mb-3">

            <!-- 왼쪽: 탭들 -->
            <div class="task-tabs d-flex align-items-center" style="gap: 8px;">

                <!-- ALL -->
                <form action="/tasks" method="get">
                    <button class="task-tab-btn ${empty param.status ? 'task-tab-active' : ''}">
                        ALL
                    </button>
                </form>

                <!-- TODO -->
                <form action="/tasks" method="get">
                    <input type="hidden" name="status" value="TODO"/>
                    <button class="task-tab-btn ${param.status == 'TODO' ? 'task-tab-active' : ''}">
                        TODO
                    </button>
                </form>

                <!-- DOING -->
                <form action="/tasks" method="get">
                    <input type="hidden" name="status" value="DOING"/>
                    <button class="task-tab-btn ${param.status == 'DOING' ? 'task-tab-active' : ''}">
                        DOING
                    </button>
                </form>

                <!-- DONE -->
                <form action="/tasks" method="get">
                    <input type="hidden" name="status" value="DONE"/>
                    <button class="task-tab-btn ${param.status == 'DONE' ? 'task-tab-active' : ''}">
                        DONE
                    </button>
                </form>

            </div>

            <!-- 가운데: 제목 검색 -->
            <div class="flex-grow-1 d-flex justify-content-center">
                <form action="/tasks" method="get"
                      class="d-flex align-items-center" style="gap: 6px;">

                    <!-- 검색어 유지 (필터 유지한 상태에서도 검색 가능) -->
                    <c:if test="${not empty param.status}">
                        <input type="hidden" name="status" value="${param.status}">
                    </c:if>

                    <input type="text" name="keyword"
                           value="${keyword}"
                           class="form-control"
                           placeholder="제목 검색"
                           style="width: 240px;">

                    <button class="btn btn-outline-secondary">검색</button>
                </form>
            </div>

            <!-- 오른쪽: 추가 -->
            <div class="d-flex align-items-center">
                <a href="/tasks/new" class="btn btn-primary">+ 추가</a>
            </div>

        </div>


        <!-- TABLE -->
        <table class="table table-hover table-bordered shadow-sm bg-white text-center align-middle">
            <thead class="table-dark">
                <tr>
                    <th class="col-no">No</th>
                    <th class="text-start col-title">제목</th>

                    <th class="col-target">
                        <form action="/tasks" method="get" style="display:inline;">
                            <input type="hidden" name="sort" value="targetDate"/>

                            <c:if test="${not empty param.status}">
                                <input type="hidden" name="status" value="${param.status}" />
                            </c:if>

                            <button type="submit" class="btn btn-link p-0 m-0 text-white">
                                목표일 ▲
                            </button>
                        </form>
                    </th>

                    <th class="col-complete">완료일</th>
                    <th class="col-status">상태</th>
                    <th class="col-statuschg">상태변경</th>
                    <th class="col-delete">삭제</th>
                </tr>
            </thead>

            <tbody>
            <c:forEach var="task" items="${tasks}" varStatus="st">
                <tr>
                    <td>${st.index + 1}</td>

                    <td class="text-start">
                        <form action="/tasks/${task.id}" method="get" style="display:inline;">
                            <button type="submit"
                                    class="btn btn-link p-0"
                                    style="text-decoration: underline;">
                                ${task.title}
                            </button>
                        </form>
                    </td>

                    <td>${task.targetDate}</td>
                    <td>${task.completeDate}</td>

                    <!-- 상태 색상 적용 -->
                    <td class="status-${task.status}">${task.status}</td>

                    <!-- 상태 변경 -->
                    <td class="col-statuschg">
                        <form action="/tasks/${task.id}/status" method="post"
                              class="d-flex justify-content-center align-items-center gap-2">

                            <select name="status" class="form-select form-select-sm w-auto">
                                <option value="TODO"  ${task.status == 'TODO' ? 'selected' : ''}>TODO</option>
                                <option value="DOING" ${task.status == 'DOING' ? 'selected' : ''}>DOING</option>
                                <option value="DONE"  ${task.status == 'DONE' ? 'selected' : ''}>DONE</option>
                            </select>

                            <button class="btn btn-sm btn-outline-secondary">변경</button>
                        </form>
                    </td>

                    <!-- 삭제 -->
                    <td class="col-delete">
                        <form action="/tasks/${task.id}/delete" method="post"
                              class="d-flex justify-content-center align-items-center">

                            <button type="submit" class="btn btn-sm btn-danger"
                                    onclick="return confirm('삭제할까요?');">삭제</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>

        </table>
    </div>
</body>
</html>
