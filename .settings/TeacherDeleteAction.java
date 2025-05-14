package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
import dao.TeacherDao;
import tool.Action;

public class TeacherDeleteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン教師情報を取得
        HttpSession session = req.getSession();
        Teacher loginTeacher = (Teacher) session.getAttribute("user");

        // 未ログインの場合はログイン画面へリダイレクト
        if (loginTeacher == null || !loginTeacher.isAuthenticated()) {
            res.sendRedirect("../Login.action");
            return;
        }

        // 管理者権限のチェック
        if (!loginTeacher.isAdmin()) {
            req.setAttribute("error", "この機能を使用する権限がありません");
            req.getRequestDispatcher("error.jsp").forward(req, res);
            return;
        }

        // 教員IDを取得
        String id = req.getParameter("id");
        if (id == null || id.trim().isEmpty()) {
            res.sendRedirect("TeacherList.action");
            return;
        }

        // 自分自身は削除できないようにする
        if (id.equals(loginTeacher.getId())) {
            req.setAttribute("error", "自分自身を削除することはできません");
            req.getRequestDispatcher("error.jsp").forward(req, res);
            return;
        }

        // 教員情報を取得
        TeacherDao teacherDao = new TeacherDao();
        Teacher teacher = teacherDao.get(id);

        if (teacher == null) {
            // 教員が見つからない場合は一覧に戻る
            res.sendRedirect("TeacherList.action");
            return;
        }

        // リクエスト属性に設定
        req.setAttribute("teacher", teacher);

        // JSPへフォワード
        req.getRequestDispatcher("teacher_delete.jsp").forward(req, res);
    }
} 
