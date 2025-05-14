<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
  <c:param name="title">得点管理システム</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">クラス情報変更</h2>

      <form action="ClassUpdateExecute.action" method="post">
        <input type="hidden" name="old_class_num" value="${class_num.class_num}" />

        <div class="row mb-3">
          <div class="col-12">
            <label class="form-label" for="class-num">現在のクラス番号</label>
            <input type="text" class="form-control" id="old-class-num" value="${class_num.class_num}" disabled />
          </div>
        </div>

        <div class="row mb-3">
          <div class="col-12">
            <label class="form-label" for="new-class-num">新しいクラス番号</label>
            <input type="text" class="form-control" id="new-class-num" name="new_class_num"
              value="${new_class_num != null ? new_class_num : class_num.class_num}" maxlength="10" required />
            <c:if test="${not empty errors['class_num']}">
              <p class="mt-2 fw-bold text-warning">${errors['class_num']}</p>
            </c:if>
          </div>
        </div>

        <div class="col-2 text-left mb-3">
          <button class="btn btn-primary" id="end">変更</button>
        </div>

        <div class="row px-4">
          <a href="ClassList.action">戻る</a>
        </div>
      </form>
    </section>
  </c:param>
</c:import> 