<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
  <c:param name="title">得点管理システム</c:param>
  <c:param name="scripts"></c:param>

  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>

      <!-- 検索条件表示 -->
      <div class="mb-3">
        <p><strong>科目：${param.subject_cd}（${param.no}回）</strong></p>
      </div>

      <form action="TestRegistExecute.action" method="post">
        <!-- hidden fields -->
        <input type="hidden" name="ent_year" value="${param.ent_year}">
        <input type="hidden" name="class_num" value="${param.class_num}">
        <input type="hidden" name="subject_cd" value="${param.subject_cd}">
        <input type="hidden" name="no" value="${param.no}">

        <!-- 学生リスト・点数入力 -->
        <div class="mb-3">
          <table class="table table-bordered">
            <thead class="table-light">
              <tr>
                <th>入学年度</th>
                <th>クラス</th>
                <th>学生番号</th>
                <th>氏名</th>
                <th>点数</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="student" items="${studentList}">
                <tr>
                  <td>${param.ent_year}</td>
                  <td>${param.class_num}</td>
                  <td>${student.no}
                    <input type="hidden" name="student_no" value="${student.no}">
                  </td>
                  <td>${student.name}</td>
                  <td>
                    <input type="number" class="form-control" name="point" min="0" max="100" required>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>

        <div class="mb-3">
          <button type="submit" class="btn btn-secondary">登録して終了</button>
        </div>
      </form>
    </section>
  </c:param>
</c:import> 