<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
  <c:param name="title">教員情報登録・編集</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <c:choose>
        <c:when test="${isNew}">
          <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">教員情報登録</h2>
        </c:when>
        <c:otherwise>
          <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">教員情報編集</h2>
        </c:otherwise>
      </c:choose>

      <c:if test="${not empty errors}">
        <div class="alert alert-danger">
          <ul class="mb-0">
            <c:forEach var="error" items="${errors}">
              <li>${error.value}</li>
            </c:forEach>
          </ul>
        </div>
      </c:if>

      <form action="${isNew ? 'TeacherCreateExecute.action' : 'TeacherUpdateExecute.action'}" method="post" class="needs-validation" novalidate>
        <div class="row g-3">
          <!-- 教員ID -->
          <div class="col-md-6">
            <label for="id" class="form-label">教員ID <span class="text-danger">*</span></label>
            <input type="text" class="form-control ${not empty errors.id ? 'is-invalid' : ''}" id="id" name="id"
                value="${teacher.id}" ${isNew ? '' : 'readonly'} required>
            <c:if test="${not empty errors.id}">
              <div class="invalid-feedback">${errors.id}</div>
            </c:if>
          </div>

          <!-- パスワード -->
          <div class="col-md-6">
            <label for="password" class="form-label">パスワード <span class="text-danger">*</span></label>
            <input type="password" class="form-control ${not empty errors.password ? 'is-invalid' : ''}" id="password"
                name="password" value="${teacher.password}" required>
            <c:if test="${not empty errors.password}">
              <div class="invalid-feedback">${errors.password}</div>
            </c:if>
          </div>

          <!-- 教員名 -->
          <div class="col-md-6">
            <label for="name" class="form-label">氏名 <span class="text-danger">*</span></label>
            <input type="text" class="form-control ${not empty errors.name ? 'is-invalid' : ''}" id="name"
                name="name" value="${teacher.name}" required>
            <c:if test="${not empty errors.name}">
              <div class="invalid-feedback">${errors.name}</div>
            </c:if>
          </div>

          <!-- 学校 -->
          <div class="col-md-6">
            <label for="school" class="form-label">所属校</label>
            <input type="text" class="form-control" id="school" value="${teacher.school.name}" readonly>
            <input type="hidden" name="school_cd" value="${teacher.school.cd}">
          </div>

          <!-- 管理者権限 -->
          <div class="col-md-6">
            <div class="form-check mt-4">
              <input class="form-check-input" type="checkbox" id="is_admin" name="is_admin"
                  ${teacher.isAdmin() ? 'checked' : ''}>
              <label class="form-check-label" for="is_admin">
                管理者権限を付与する
              </label>
            </div>
          </div>
        </div>

        <!-- 送信ボタン -->
        <div class="mt-4">
          <button type="submit" class="btn btn-primary">
            <c:choose>
              <c:when test="${isNew}">登録</c:when>
              <c:otherwise>更新</c:otherwise>
            </c:choose>
          </button>
          <a href="TeacherList.action" class="btn btn-secondary ms-2">キャンセル</a>
        </div>
      </form>
    </section>
  </c:param>
</c:import> 