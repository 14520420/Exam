package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectUpdateAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // パラメータ取得
        String cd = req.getParameter("cd");

        // 教員情報から学校情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        School school = teacher.getSchool();

        // 科目取得
        SubjectDao dao = new SubjectDao();
        Subject subject = dao.get(cd, school);

        if (subject != null) {
            req.setAttribute("subject", subject);
            req.getRequestDispatcher("subject_update.jsp").forward(req, res);
        } else {
            req.setAttribute("error", "該当する科目が見つかりません。");
            req.getRequestDispatcher("/main/subject_list.jsp").forward(req, res);
        }
    }
} 