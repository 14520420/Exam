<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
	<c:param name="title">科目編集</c:param>
	<c:param name="scripts"></c:param>
	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目編集</h2>
			<form action="SubjectUpdateExecute.action" method="post" class="px-4">
				<input type="hidden" name="cd" value="${subject.cd}" />
				<div class="mb-3">
					<label class="form-label">科目コード</label>
					<input type="text" class="form-control" value="${subject.cd}" disabled />
				</div>
				<div class="mb-3">
					<label class="form-label">科目名</label>
					<input type="text" name="name" class="form-control" value="${subject.name}" required />
				</div>
				<button type="submit" class="btn btn-primary">更新</button>
				<a href="SubjectList.action" "
					class="btn btn-link">戻る</a>

			</form>
		</section>
	</c:param>
</c:import> 