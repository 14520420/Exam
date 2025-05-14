<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
  <c:param name="title">クラス情報登録・編集</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <c:choose>
        <c:when test="${isNew}">
          <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">クラス情報登録</h2>
        </c:when>
        <c:otherwise>
          <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">クラス情報変更</h2>
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

      <form action="${isNew ? 'ClassCreateExecute.action' : 'ClassUpdateExecute.action'}" method="post" class="needs-validation" novalidate>
        <div class="row g-3">
          <!-- 元のクラス番号（更新時のみ） -->
          <c:if test="${!isNew}">
            <input type="hidden" name="old_class_num" value="${classNum.old_class_num}">
          </c:if>

          <!-- クラス番号 -->
          <div class="col-md-6">
            <label for="class_num" class="form-label">クラス番号 <span class="text-danger">*</span></label>
            <input type="text" class="form-control ${not empty errors.class_num ? 'is-invalid' : ''}" id="class_num" 
                name="class_num" value="${classNum.class_num}" required maxlength="10">
            <c:if test="${not empty errors.class_num}">
              <div class="invalid-feedback">${errors.class_num}</div>
            </c:if>
          </div>

          <!-- 学校 -->
          <div class="col-md-6">
            <label for="school" class="form-label">所属校</label>
            <input type="text" class="form-control" id="school" value="${classNum.school.name}" readonly>
          </div>

          <!-- 学生数（更新時のみ） -->
          <c:if test="${!isNew && not empty studentCount}">
            <div class="col-md-6">
              <label class="form-label">所属学生数</label>
              <p class="form-control-plaintext">${studentCount}名</p>
              <small class="text-muted">※クラスを変更すると、所属する学生のクラスも自動的に変更されます。</small>
            </div>
          </c:if>
        </div>

        <!-- 送信ボタン -->
        <div class="mt-4">
          <button type="submit" class="btn btn-primary">
            <c:choose>
              <c:when test="${isNew}">登録</c:when>
              <c:otherwise>更新</c:otherwise>
            </c:choose>
          </button>
          <a href="ClassList.action" class="btn btn-secondary ms-2">キャンセル</a>
        </div>
      </form>
    </section>
  </c:param>
</c:import> 