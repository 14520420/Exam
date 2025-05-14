<%-- 学生一覧JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生情報登録</h2>
			<form action="StudentCreateExecute.action" method="get">
				<%--<div class="row border mx-3 mb-3 py-2 align-items-center rounded "id="filter"> --%>
				<div>
					<div class="col-12">
						<%--forでHTMLのform要素を紐づけられたIDを指定 --%>
						<label class="form-label" for="studen^f1-select">入学年度</label> <select
							class="form-select" id="student-f1-select" name="ent_year">
							<option value="0">--------</option>
							<c:forEach var="year" items="${ent_year_set}">
								<option value="${year}" <c:if test="${year==f1}">selected</c:if>>${year}</option>
							</c:forEach>
						</select>
						<%-- エラー出力 --%>
						<c:if test="${not empty ageerror}">
							<p class="mt-2 fw-bold text-warning">${ageerror}</p>
						</c:if>
					</div>
					<div class="row mb-3">
						<div class="col-12">
							<label class="form-label" for="student-no">学生番号</label> <input
								type="text" class="form-control" id="student-no"
								placeholder="学生番号を入力してください" name="no" maxlength="10" required>
						</div>
						<%-- エラー出力 --%>
						<c:if test="${not empty cderror}">
							<p class="mt-2 fw-bold text-warning">${cderror}</p>
						</c:if>
					</div>
					<div class="row mb-3">
						<div class="col-12">
							<label class="form-label" for="student-name">氏名</label> <input
								type="text" class="form-control" id="student-name"
								placeholder="氏名を入力してください" name="name" maxlength="10" required>
						</div>
					</div>
					<div class="col-12">
						<label class="form-label" for="student-f2-select">クラス</label> <select
							class="form-select" id="student-f2-select" name="class_num">
							<c:forEach var="num" items="${class_num_set}">
								<option value="${num}" <c:if test="${num==f2}">selected</c:if>>${num}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-2 text-center">
						<button class="btn btn-secondary" id="end">登録して終了</button>
					</div>
					<div class="mt-2 text-warning">${errors.get("f1")}</div>
					<div class="row px-4">
						<a href="StudentList.action">戻る</a>
					</div>
				</div>
			</form>
		</section>
	</c:param>
</c:import> 