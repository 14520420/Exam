package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectDeleteExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        req.setCharacterEncoding("UTF-8");

        String cd = req.getParameter("cd");

        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        SubjectDao dao = new SubjectDao();
        Subject subject = dao.get(cd, teacher.getSchool());

        if (subject != null) {
            dao.delete(subject);
        }

        req.getRequestDispatcher("subject_delete_done.jsp").forward(req, res);
    }
} 