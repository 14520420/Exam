package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Teacher;
import dao.SchoolDao;
import dao.TeacherDao;
import tool.Action;

public class TeacherUpdateAction extends Action {

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

        // 教員情報を取得
        TeacherDao teacherDao = new TeacherDao();
        Teacher teacher = teacherDao.get(id);

        if (teacher == null) {
            // 教員が見つからない場合は一覧に戻る
            res.sendRedirect("TeacherList.action");
            return;
        }

        // 学校一覧を取得
        SchoolDao schoolDao = new SchoolDao();
        List<School> schoolList = schoolDao.getAllSchools();

        // リクエスト属性に設定
        req.setAttribute("teacher", teacher);
        req.setAttribute("schoolList", schoolList);

        // JSPへフォワード
        req.getRequestDispatcher("teacher_update.jsp").forward(req, res);
    }
}