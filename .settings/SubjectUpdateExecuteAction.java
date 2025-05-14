package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectUpdateExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        req.setCharacterEncoding("UTF-8");

        // パラメータ取得
        String cd = req.getParameter("cd");
        String name = req.getParameter("name");

        // セッションから学校情報取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        School school = teacher.getSchool();

        // 科目情報作成
        Subject subject = new Subject();
        subject.setCd(cd);
        subject.setName(name);
        subject.setSchool(school);

        // 更新処理
        SubjectDao dao = new SubjectDao();
        dao.save(subject);

        // 完了画面へフォワード
        req.setAttribute("subject", subject);
        req.getRequestDispatcher("subject_update_done.jsp").forward(req, res);
    }
} 