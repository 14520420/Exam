<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
  <c:param name="title">科目情報削除</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-success bg-opacity-25 py-2 px-4">科目情報削除</h2>
      <p>削除が完了しました。</p>
      <div class="mt-3">
        <a href="SubjectList.action" class="btn btn-link">科目一覧に戻る</a>
      </div>
    </section>
  </c:param>
</c:import> 