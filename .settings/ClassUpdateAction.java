package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.ClassNum;
import bean.School;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassUpdateAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン教師情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // 未ログインの場合はログイン画面へリダイレクト
        if (teacher == null || !teacher.isAuthenticated()) {
            res.sendRedirect("../Login.action");
            return;
        }

        // 学校情報を取得
        School school = teacher.getSchool();

        // クラス番号を取得
        String classNum = req.getParameter("class_num");
        if (classNum == null || classNum.trim().isEmpty()) {
            res.sendRedirect("ClassList.action");
            return;
        }

        // クラス情報を取得
        ClassNumDao dao = new ClassNumDao();
        ClassNum classInfo = dao.get(classNum, school);

        if (classInfo == null) {
            // クラスが見つからない場合は一覧に戻る
            res.sendRedirect("ClassList.action");
            return;
        }

        // リクエスト属性に設定
        req.setAttribute("class_num", classInfo);

        // JSPへフォワード
        req.getRequestDispatcher("class_update.jsp").forward(req, res);
    }
} 