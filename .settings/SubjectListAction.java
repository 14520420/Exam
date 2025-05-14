package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectListAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログインユーザー(教員)の取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // DAOのインスタンス化
        SubjectDao subjectDao = new SubjectDao();

        // 科目リスト取得（学校コードに紐づく）
        List<Subject> subjects = subjectDao.filter(teacher.getSchool());

        // リクエスト属性へセット
        req.setAttribute("subjects", subjects);

        // JSPへフォワード
        req.getRequestDispatcher("subject_list.jsp").forward(req, res);
    }
} 