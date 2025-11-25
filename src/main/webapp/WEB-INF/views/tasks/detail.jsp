<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<html>
<head>
    <title>Task Detail</title>

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

        .detail-card {
            border-radius: 12px;
            border: 1px solid #e3e6ea;
        }

        .detail-title {
            font-size: 24px;
            font-weight: 700;
            color: #343a40;
        }

        .info-table th {
            width: 140px;
            background: #f1f3f5;
            font-weight: 600;
            color: #495057;
            vertical-align: middle;
        }

        .info-table td {
            background: #ffffff;
            vertical-align: middle;
        }

        .description-box {
            border: 1px solid #dee2e6;
            background: #ffffff;
            padding: 16px;
            border-radius: 8px;
            white-space: pre-line;
            line-height: 1.6;
        }
    </style>
</head>

<body class="d-flex justify-content-center">

<div style="max-width: 800px; width: 100%;">

    <!-- 페이지 제목 -->
    <div class="mb-4">
        <div class="detail-title">📄 상세보기</div>
    </div>

    <!-- 메인 카드 -->
    <div class="card shadow-sm detail-card">
        <div class="card-body">

        <!-- 2열: 제목 / 생성일 -->
        <div class="row mb-3">
            <div class="col-6">
                <div class="fw-bold text-secondary small">제목</div>
                <div class="mt-1">${task.title}</div>
            </div>
            <div class="col-6">
                <div class="fw-bold text-secondary small">생성일</div>
                <div class="mt-1">${task.createdAt.toLocalDate()}</div>
            </div>
        </div>

        <!-- 상태 (1열) -->
        <div class="mb-3">
            <div class="fw-bold text-secondary small">상태</div>
            <div class="mt-1">${task.status}</div>
        </div>

        <!-- 2열: 목표일 / 완료일 -->
        <div class="row mb-3">
            <div class="col-6">
                <div class="fw-bold text-secondary small">목표일</div>
                <div class="mt-1">${task.targetDate}</div>
            </div>
            <div class="col-6">
                <div class="fw-bold text-secondary small">완료일</div>
                <div class="mt-1">${task.completeDate != null ? task.completeDate : ''}</div>
            </div>
        </div>

        <!-- 설명 -->
        <div class="mt-4">
            <div class="fw-bold text-secondary small mb-2">설명</div>
            <div class="description-box">${fn:trim(task.description)}</div>
        </div>


            <!-- 버튼 영역 -->
            <div class="d-flex justify-content-end gap-3 mt-4">

                <a href="/tasks/${task.id}/edit" class="btn btn-primary px-4">수정</a>

                <a href="/tasks" class="btn btn-secondary px-4">목록으로</a>

            </div>

        </div>
    </div>

</div>

</body>
</html>
