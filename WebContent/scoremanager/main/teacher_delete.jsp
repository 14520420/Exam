<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
  <c:param name="title">教員削除確認</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">教員削除確認</h2>

      <div class="alert alert-danger">
        <h4 class="alert-heading">削除の確認</h4>
        <p>以下の教員情報を削除します。この操作は取り消せません。</p>
      </div>

      <div class="card mb-4">
        <div class="card-header">教員情報</div>
        <div class="card-body">
          <dl class="row">
            <dt class="col-sm-3">教員ID:</dt>
            <dd class="col-sm-9">${teacher.id}</dd>

            <dt class="col-sm-3">氏名:</dt>
            <dd class="col-sm-9">${teacher.name}</dd>

            <dt class="col-sm-3">所属校:</dt>
            <dd class="col-sm-9">${teacher.school.name}</dd>

            <dt class="col-sm-3">管理者権限:</dt>
            <dd class="col-sm-9">${teacher.isAdmin() ? '有り' : '無し'}</dd>
          </dl>
        </div>
      </div>

      <form action="TeacherDeleteExecute.action" method="post">
        <input type="hidden" name="id" value="${teacher.id}">
        <button type="submit" class="btn btn-danger">削除する</button>
        <a href="TeacherList.action" class="btn btn-secondary ms-2">キャンセル</a>
      </form>
    </section>
  </c:param>
</c:import> 