<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp">

	<c:param name="title">得点管理システム</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section class="m-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">クラス管理</h2>
			<div class="my-2 text-end px-4">
				<a href="ClassCreate.action">新規登録</a>
			</div>
			<c:choose>
				<c:when test="${classes.size()>0}">
					<div>表示数：${classes.size()}件</div>
					<table class="table table-hover">
						<tr>
							<th>クラス番号</th>
							<th>在籍数</th>
							<th></th>
						</tr>
						<c:forEach var="clazz" items="${classes}">
							<tr>
								<td>
									<a href="ClassStudentList.action?class_num=${clazz.class_num}"
									     class="text-decoration-none text-dark">
									     ${clazz.class_num}
									</a>
								</td>
								<td>${clazz.c_count }</td>
								<td>
									<a href="ClassUpdate.action?class_num=${clazz.class_num}&c_count=${clazz.c_count}">変更</a>
								</td>
								<td>
									<%-- 削除確認付き --%>
									<a href="ClassDelete.action?class_num=${clazz.class_num}&c_count=${clazz.c_count}"
									onclick="return confirm('本当に削除してもよろしいですか？');">削除</a>
								</td>
							</tr>
						</c:forEach>
					</table>
				</c:when>
			</c:choose>
		</section>
	</c:param>
</c:import>