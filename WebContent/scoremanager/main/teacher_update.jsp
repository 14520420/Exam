<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
  <c:param name="title">得点管理システム</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">教員情報変更</h2>

      <form action="TeacherUpdateExecute.action" method="post">
        <div class="row mb-3">
          <div class="col-12">
            <label class="form-label" for="id">教員ID</label>
            <input type="text" class="form-control" id="id" name="id"
              value="${teacher.id}" maxlength="20" readonly />
          </div>
        </div>

        <div class="row mb-3">
          <div class="col-12">
            <label class="form-label" for="password">パスワード</label>
            <input type="password" class="form-control" id="password" name="password"
              value="${password != null ? password : teacher.password}" maxlength="20" required />
            <c:if test="${not empty errors['password']}">
              <p class="mt-2 fw-bold text-warning">${errors['password']}</p>
            </c:if>
          </div>
        </div>

        <div class="row mb-3">
          <div class="col-12">
            <label class="form-label" for="name">氏名</label>
            <input type="text" class="form-control" id="name" name="name"
              value="${name != null ? name : teacher.name}" maxlength="30" required />
            <c:if test="${not empty errors['name']}">
              <p class="mt-2 fw-bold text-warning">${errors['name']}</p>
            </c:if>
          </div>
        </div>

        <div class="row mb-3">
          <div class="col-12">
            <label class="form-label" for="school_cd">所属校</label>
            <select class="form-select" id="school_cd" name="school_cd">
              <option value="">-- 選択してください --</option>
              <c:forEach var="school" items="${schoolList}">
                <option value="${school.cd}" <c:if test="${school_cd != null ? school_cd == school.cd : teacher.school.cd == school.cd}">selected</c:if>>${school.name}</option>
              </c:forEach>
            </select>
            <c:if test="${not empty errors['school_cd']}">
              <p class="mt-2 fw-bold text-warning">${errors['school_cd']}</p>
            </c:if>
          </div>
        </div>

        <div class="row mb-3">
          <div class="col-12">
            <div class="form-check">
              <input class="form-check-input" type="checkbox" id="is_admin" name="is_admin" <c:if test="${is_admin != null ? is_admin : teacher.admin}">checked</c:if> />
              <label class="form-check-label" for="is_admin">管理者権限を付与</label>
            </div>
          </div>
        </div>

        <div class="col-2 text-left mb-3">
          <button class="btn btn-primary" id="end">変更</button>
        </div>

        <div class="row px-4">
          <a href="TeacherList.action">戻る</a>
        </div>
      </form>
    </section>
  </c:param>
</c:import> 