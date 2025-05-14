<%-- サイドバー --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<ul class="nav nav-pills flex-column mb-auto px-4">
	<li class="nav-item my-3"><a href="Menu.action">メニュー</a></li>
	<li class="nav-item mb-3"><a href="StudentList.action">学生管理</a></li>
	<li class="nav-item">成績管理</li>
	<li class="nav-item mx-3 mb-3"><a href="TestRegist.action">成績登録</a></li>
	<li class="nav-item mx-3 mb-3"><a href="TestList.action">成績参照</a></li>
	<li class="nav-item mb-3"><a href="SubjectList.action">科目管理</a></li>
	<li class="nav-item mb-3"><a href="ClassList.action">クラス管理</a></li>

	<%-- 管理者権限がある場合のみ教員管理を表示 --%>
	<c:if test="${user.isAdmin()}">
		<li class="nav-item mb-3"><a href="TeacherList.action">教員管理</a></li>
	</c:if>
</ul> 