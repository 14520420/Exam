<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/common/base.jsp" %>
<h2>学生別成績一覧</h2>
<table border="1">
  <tr><th>科目名</th><th>得点</th></tr>
  <c:forEach var="g" items="${grades}">
    <tr>
      <td>${g.subjectName}</td>
      <td>${g.point}</td>
    </tr>
  </c:forEach>
</table>

 