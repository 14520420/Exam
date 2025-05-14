package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectCreateAction extends Action {
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからユーザー情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // 科目一覧を取得
        SubjectDao subjectDao = new SubjectDao();
        List<Subject> list = subjectDao.filter(teacher.getSchool());

        // 空のSubjectをリクエストにセット（フォーム用）
        Subject subject = new Subject();
        req.setAttribute("subject", subject);
        req.setAttribute("subject_list", list);

        // JSPへフォワード
        req.getRequestDispatcher("subject_create.jsp").forward(req, res);
    }
} 