<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
    <c:param name="title" value="科目情報削除" />
    <c:param name="scripts" value="" />
    <c:param name="content">
        <section class="me-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目情報削除</h2> <!-- ① タイトル -->

            <div class="px-4">
                <!-- ② 確認文 -->
                <p>
                    「${subject.name}（${subject.cd}）」を削除してよろしいですか？
                </p>

                <!-- 削除処理へのフォーム -->
                <form action="SubjectDeleteExecute.action" method="post">
                    <input type="hidden" name="cd" value="${subject.cd}" />

                    <!-- ③ 削除ボタン -->
                    <button class="btn btn-danger" type="submit">削除</button>

                    <!-- ④ 戻るリンク -->
                    <a class="btn btn-secondary ms-2" href="SubjectList.action">戻る</a>
                </form>
            </div>
        </section>
    </c:param>
</c:import>
 