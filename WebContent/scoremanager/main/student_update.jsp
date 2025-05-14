<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
	<c:param name="title">学生情報変更</c:param>
	<c:param name="scripts"></c:param>
	<c:param name="content">
		<h2>学生情報変更</h2>

		<form action="StudentUpdateExecute.action" method="post">
			<table>
				<tr>
					<th>入学年度：</th>
					<td><select name="ent_year">
							<c:forEach var="year" items="${ent_year_set}">
								<option value="${year}"
									<c:if test="${year == student.entYear}">selected</c:if>>${year}</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<th>学生番号</th>
					<td><input type="text" name="no" value="${student.no}"
						readonly /></td>
				</tr>
				<tr>
					<th>氏名</th>
					<td><input type="text" name="name" value="${student.name}"
						maxlength="30" required /></td>
				</tr>
				<tr>
					<th>クラス</th>
					<td><select name="class_num">
							<c:forEach var="cls" items="${sclassList}">
								<option value="${cls}"
									<c:if test="${cls == student.classNum}">selected</c:if>>
									${cls}</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<th>在学中</th>
					<td><input type="checkbox" name="is_attend"
						<c:if test="${student.attend}">checked</c:if> /></td>
				</tr>
			</table>

			<div style="margin-top: 20px;">
				<input type="submit" value="更新" /> <a href="StudentList.action"
					style="margin-left: 20px;">戻る</a>
			</div>
		</form>
	</c:param>
</c:import> 