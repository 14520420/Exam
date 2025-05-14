<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>得点管理システム</title>
    <style>
        body {
            font-family: 'Meiryo', 'MS Gothic', sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        /* ヘッダー部分 */
        .header {
            background-color: #e6f0ff;
            padding: 15px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .header-title {
            font-size: 24px;
            font-weight: bold;
        }

        .user-info {
            text-align: right;
        }

        .logout-link {
            color: #0000FF;
            text-decoration: none;
        }

        /* メインコンテンツ */
        .main-container {
            display: flex;
            flex: 1;
        }

        /* 左側メニュー */
        .menu {
            width: 220px;
            padding: 15px;
            background-color: white;
        }

        .menu a {
            display: block;
            color: #0000FF;
            text-decoration: none;
            margin: 5px 0;
        }

        .menu a:hover {
            text-decoration: underline;
        }

        .submenu {
            margin-left: 15px;
        }

        /* 右側コンテンツエリア */
        .content {
            flex: 1;
            padding: 15px;
        }

        /* 見出し */
        .section-title {
            background-color: #f0f0f0;
            padding: 10px;
            font-size: 18px;
            margin-bottom: 20px;
        }

        /* 検索フォーム */
        .search-form {
            border: 1px solid #ddd;
            padding: 15px;
            margin-bottom: 15px;
            border-radius: 4px;
        }

        .search-form-row {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }

        .search-form label {
            margin-right: 5px;
            min-width: 70px;
        }

        .search-form select, .search-form input {
            padding: 5px;
            margin-right: 15px;
            border: 1px solid #ccc;
            border-radius: 3px;
            height: 30px;
        }

        /* ボタン */
        .btn {
            background-color: #5c7a99;
            color: white;
            border: none;
            padding: 8px 15px;
            border-radius: 3px;
            cursor: pointer;
        }

        /* テーブル */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        /* 入力フィールド */
        .score-input {
            width: 200px;
            padding: 5px;
            border: 1px solid #ccc;
            border-radius: 3px;
        }

        /* 説明文 */
        .instruction {
            color: #ff0000;
            font-size: 12px;
            margin-left: 5px;
        }

        /* フッター */
        .footer {
            background-color: #f0f0f0;
            text-align: center;
            padding: 10px;
            font-size: 12px;
            margin-top: auto;
        }
    </style>
</head>
<body>
    <!-- ヘッダー -->
    <div class="header">
        <div class="header-title">得点管理システム</div>
        <div class="user-info">
            ${sessionScope.userName}様
            <a href="logout.action" class="logout-link">ログアウト</a>
        </div>
    </div>

    <div class="main-container">
        <!-- メニュー -->
        <div class="menu">
            <div>メニュー</div>
            <a href="studentManage.action">学生管理</a>
            <div>成績管理</div>
            <div class="submenu">
                <a href="TestRegist.action">成績登録</a>
                <a href="TestList.action">成績参照</a>
            </div>
            <a href="subjectManage.action">科目管理</a>
        </div>

        <!-- メインコンテンツ -->
        <div class="content">
            <div class="section-title">成績管理</div>

            <!-- 検索フォーム -->
            <form action="TestRegistExecute.action" method="post">
                <div class="search-form">
                    <div class="search-form-row">
                        <label for="yearSelect">入学年度</label>
                        <select id="yearSelect" name="year">
                            <c:forEach items="${yearList}" var="year">
                                <option value="${year}" <c:if test="${selectedYear == year}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>

                        <label for="classSelect">クラス</label>
                        <select id="classSelect" name="class">
                            <c:forEach items="${classList}" var="classItem">
                                <option value="${classItem}" <c:if test="${selectedClass == classItem}">selected</c:if>>${classItem}</option>
                            </c:forEach>
                        </select>

                        <label for="subjectSelect">科目</label>
                        <select id="subjectSelect" name="subject">
                            <c:forEach items="${subjectList}" var="subject">
                                <option value="${subject.subjectId}" <c:if test="${selectedSubject == subject.subjectId}">selected</c:if>>${subject.subjectName}</option>
                            </c:forEach>
                        </select>

                        <label for="timesSelect">回数</label>
                        <select id="timesSelect" name="times">
                            <c:forEach items="${timesList}" var="times">
                                <option value="${times}" <c:if test="${selectedTimes == times}">selected</c:if>>${times}</option>
                            </c:forEach>
                        </select>

                        <button type="submit" class="btn">検索</button>
                    </div>
                </div>
            </form>

            <div>科目：${selectedSubjectName}（${selectedTimes}回）</div>

            <!-- 学生成績表 -->
            <form action="TestRegistDone.action" method="post">
                <input type="hidden" name="subjectId" value="${selectedSubject}">
                <input type="hidden" name="times" value="${selectedTimes}">

                <table>
                    <thead>
                        <tr>
                            <th>入学年度</th>
                            <th>クラス</th>
                            <th>学生番号</th>
                            <th>氏名</th>
                            <th>点数</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${studentScoreList}" var="student" varStatus="status">
                            <tr>
                                <td>${student.year}</td>
                                <td>${student.className}</td>
                                <td>${student.studentId}</td>
                                <td>${student.studentName}</td>
                                <td>
                                    <input type="hidden" name="studentId${status.index}" value="${student.studentId}">
                                    <input type="text" name="score${status.index}" value="${student.score}" class="score-input">
                                    <c:if test="${status.index == 0}">
                                        <span class="instruction">0～100の範囲で入力してください</span>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <input type="hidden" name="studentCount" value="${studentScoreList.size()}">
                <button type="submit" class="btn">登録して終了</button>
            </form>
        </div>
    </div>

    <!-- フッター -->
    <div class="footer">
        © 2023 TIC<br>
        大原学園
    </div>
</body>
</html> 