<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
  <c:param name="title">得点管理システム</c:param>
  <c:param name="scripts"></c:param>

  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目情報登録</h2>

      <form action="SubjectCreateExecute.action" method="post">
        <div class="row mb-3">
          <div class="col-12">
            <label class="form-label" for="subject-cd">科目コード</label>
            <input type="text" class="form-control" id="subject-cd" name="cd"
              value="${subject.cd}" maxlength="3" required />
            <c:if test="${not empty errors['code']}">
              <p class="mt-2 fw-bold text-warning">${errors['code']}</p>
            </c:if>
          </div>
        </div>

        <div class="row mb-3">
          <div class="col-12">
            <label class="form-label" for="subject-name">科目名</label>
            <input type="text" class="form-control" id="subject-name" name="name"
              value="${subject.name}" maxlength="20"  required />
            <c:if test="${not empty errors['name']}">
              <p class="mt-2 fw-bold text-warning">${errors['name']}</p>
            </c:if>
          </div>
        </div>

        <div class="col-2 text-left mb-3">
          <button class="btn btn-primary" id="end">登録</button>
        </div>



      <div class="row px-4">
        <a href="SubjectList.action">戻る</a>
      </div>
      </form>
    </section>
  </c:param>
</c:import> 