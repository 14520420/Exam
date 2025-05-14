package scoremanager.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Teacher;
import dao.SchoolDao;
import dao.TeacherDao;
import tool.Action;

public class TeacherUpdateExecuteAction extends Action {

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

        // リクエストパラメータの取得
        String id = req.getParameter("id");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String schoolCd = req.getParameter("school_cd");
        boolean isAdmin = "on".equals(req.getParameter("is_admin"));

        // バリデーション
        Map<String, String> errors = new HashMap<>();
        if (id == null || id.trim().isEmpty()) {
            errors.put("id", "教員IDを入力してください");
        }
        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "パスワードを入力してください");
        }
        if (name == null || name.trim().isEmpty()) {
            errors.put("name", "氏名を入力してください");
        }
        if (schoolCd == null || schoolCd.trim().isEmpty()) {
            errors.put("school_cd", "学校を選択してください");
        }
     // エラーがある場合は学校リストを取得して入力画面に戻る
        if (!errors.isEmpty()) {
            SchoolDao schoolDao = new SchoolDao();
            List<School> schoolList = schoolDao.getAllSchools();

            req.setAttribute("errors", errors);
            req.setAttribute("id", id);
            req.setAttribute("password", password);
            req.setAttribute("name", name);
            req.setAttribute("school_cd", schoolCd);
            req.setAttribute("is_admin", isAdmin);
            req.setAttribute("schoolList", schoolList);

            req.getRequestDispatcher("teacher_update.jsp").forward(req, res);
            return;
        }

        // 学校情報を取得
        SchoolDao schoolDao = new SchoolDao();
        School school = schoolDao.get(schoolCd);

        // 教員情報を更新
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setPassword(password);
        teacher.setName(name);
        teacher.setSchool(school);
        teacher.setAdmin(isAdmin);

        TeacherDao teacherDao = new TeacherDao();
        boolean result = teacherDao.save(teacher);

        if (result) {
            // 成功した場合は完了画面へ
            req.setAttribute("teacher", teacher);
            req.getRequestDispatcher("teacher_update_done.jsp").forward(req, res);
        } else {
            // 失敗した場合はエラーメッセージを表示して入力画面へ
            errors.put("db", "教員情報の更新に失敗しました");

            List<School> schoolList = schoolDao.getAllSchools();

            req.setAttribute("errors", errors);
            req.setAttribute("id", id);
            req.setAttribute("password", password);
            req.setAttribute("name", name);
            req.setAttribute("school_cd", schoolCd);
            req.setAttribute("is_admin", isAdmin);
            req.setAttribute("schoolList", schoolList);

            req.getRequestDispatcher("teacher_update.jsp").forward(req, res);
        }
    }
}