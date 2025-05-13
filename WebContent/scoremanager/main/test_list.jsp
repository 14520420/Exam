<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
	<c:param name="title">得点一覧</c:param>
	<c:param name="content">
		<section class="me-4">

			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">得点一覧（科目）</h2>

			<!-- ======== 検索フォーム ======== -->
			<form method="get">
				<!-- 1 段目 -->
				<div class="row border mx-3 py-2 rounded mb-2 align-items-center">
					<!-- 入学年度 -->
					<div class="col-3">
						<label class="form-label">入学年度</label> <select name="f1"
							class="form-select">
							<option value="">--</option>
							<c:forEach var="y" items="${ent_year_set}">
								<option value="${y}"
									<c:if test="${conditions['entYear']==y.toString()}">selected</c:if>>${y}</option>
							</c:forEach>
						</select>
					</div>

					<!-- クラス -->
					<div class="col-3">
						<label class="form-label">クラス</label> <select name="f2"
							class="form-select">
							<option value="">--</option>
							<c:forEach var="c" items="${class_num_set}">
								<option value="${c}"
									<c:if test="${conditions['classNum']==c}">selected</c:if>>${c}</option>
							</c:forEach>
						</select>
					</div>

					<!-- 科目 -->
					<div class="col-3">
						<label class="form-label">科目</label> <select name="f3"
							class="form-select">
							<option value="">--</option>
							<c:forEach var="s" items="${subjects}">
								<option value="${s.cd}"
									<c:if test="${conditions['subjectCd']==s.cd}">selected</c:if>>${s.name}</option>
							</c:forEach>
						</select>
					</div>

					<!-- ボタン -->
					<div class="col-3 text-center">
						<label class="form-label d-block">&nbsp;</label>
						<button class="btn btn-secondary">絞込</button>
					</div>
				</div>

				<!-- 2 段目（学生情報）-->
				<div class="row border mx-3 py-2 rounded align-items-center">
					<div class="col-9">
						<label class="form-label">学生番号 / 氏名</label> <input type="text"
							name="studentInfo" class="form-control"
							value="${conditions['studentInfo']}">
					</div>
					<div class="col-3 text-center">
						<label class="form-label d-block">&nbsp;</label>
						<button class="btn btn-primary">検索</button>
					</div>
				</div>
			</form>
			<!-- ======== /検索フォーム ======== -->

			<!-- ======== 検索結果 ======== -->
			<c:choose>
				<c:when test="${tests.size()>0}">
					<c:if test="${not empty selectedSubject}">
						<div class="mt-2">科目: ${selectedSubject.name}</div>
					</c:if>
					<table class="table table-hover">
						<thead>
							<tr>
								<th>入学年度</th>
								<th>クラス</th>
								<th>学生番号</th>
								<th>氏名</th>
								<th>得点</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="t" items="${tests}">
								<tr>
									<td>${t.student.entYear}</td>
									<td>${t.classNum}</td>
									<td>${t.student.no}</td>
									<td>${empty t.student.name ? '－' : t.student.name}</td>
									<td>${t.point}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<div class="text-muted mt-2">該当するデータはありません。</div>
				</c:otherwise>
			</c:choose>
			<!-- ======== /検索結果 ======== -->

		</section>
	</c:param>
</c:import>