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

        /* 成功メッセージ */
        .success-message {
            background-color: #d4edda;
            color: #155724;
            padding: 10px;
            margin: 10px 0;
            border-radius: 3px;
        }

        /* リンクボタン */
        .action-links {
            margin-top: 20px;
        }

        .action-links a {
            color: #0000FF;
            text-decoration: none;
            margin-right: 15px;
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

            <div class="success-message">登録が完了しました</div>

            <div class="action-links">
                <a href="TestRegist.action">戻る</a>
                <a href="TestList.action">成績参照</a>
            </div>
        </div>
    </div>

    <!-- フッター -->
    <div class="footer">
        © 2023 TIC<br>
        大原学園
    </div>
</body>
</html> 