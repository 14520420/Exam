<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
  <c:param name="title">教員管理</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">教員管理</h2>

      <!-- 新規登録リンク -->
      <div class="my-2 text-end px-4">
        <a href="TeacherCreate.action" class="btn btn-primary btn-sm">新規登録</a>
      </div>

      <!-- 教員一覧表示 -->
      <c:choose>
        <c:when test="${not empty teachers && teachers.size() > 0}">
          <table class="table table-hover">
            <thead>
              <tr>
                <th>ID</th>
                <th>氏名</th>
                <th>学校</th>
                <th>管理者</th>
                <th></th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="teacher" items="${teachers}">
                <tr>
                  <td>${teacher.id}</td>
                  <td>${teacher.name}</td>
                  <td>${teacher.school.name}</td>
                  <td>
                    <c:choose>
                      <c:when test="${teacher.isAdmin()}">○</c:when>
                      <c:otherwise>-</c:otherwise>
                    </c:choose>
                  </td>
                  <td><a href="TeacherUpdate.action?id=${teacher.id}">編集</a></td>
                  <td><a href="TeacherDelete.action?id=${teacher.id}" class="text-danger" onclick="return confirm('本当に削除しますか？');">削除</a></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:when>
        <c:otherwise>
          <div class="alert alert-info">
            教員情報がありません。
          </div>
        </c:otherwise>
      </c:choose>

      <div class="mt-3">
        <a href="Menu.action" class="btn btn-secondary">メニューに戻る</a>
      </div>
    </section>
  </c:param>
</c:import> 