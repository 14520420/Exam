<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="../../common/base.jsp" >
<c:param name="title">学生追加</c:param>
<c:param name="scripts"></c:param>
<c:param name="content">

	<h2>学生情報登録</h2>

	<div class="success-message"
		style="background-color: #cce5cc; color: #2d572d; padding: 10px; margin-top: 10px; border: 1px solid #a6d8a8;">
		登録が完了しました</div>

	<div class="link-group" style="margin-top: 30px;">
		<a href="menu.jsp"
			style="margin-right: 20px; text-decoration: none; color: blue;">戻る</a>
		<a href="StudentList.action"
			style="text-decoration: none; color: blue;">学生一覧</a>
	</div>
</c:param>
</c:import> 