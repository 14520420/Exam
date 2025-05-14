package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.ClassNum;
import bean.School;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class ClassListAction extends Action {

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

        // クラス情報を取得（学生数を含む）
        StudentDao studentDao = new StudentDao();
        List<ClassNum> classList = studentDao.class_count(school);

        // リクエスト属性に設定
        req.setAttribute("classList", classList);

        // JSPへフォワード
        req.getRequestDispatcher("class_list.jsp").forward(req, res);
    }
} 