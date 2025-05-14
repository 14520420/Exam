package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Teacher;
import dao.SchoolDao;
import tool.Action;

public class TeacherCreateAction extends Action {
    
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
        
        // 管理者権限のチェック
        if (!teacher.isAdmin()) {
            req.setAttribute("error", "この機能を使用する権限がありません");
            req.getRequestDispatcher("error.jsp").forward(req, res);
            return;
        }
        
        // 学校一覧を取得
        SchoolDao schoolDao = new SchoolDao();
        List<School> schoolList = schoolDao.getAllSchools();
        
        // リクエスト属性に設定
        req.setAttribute("schoolList", schoolList);
        
        // JSPへフォワード
        req.getRequestDispatcher("teacher_create.jsp").forward(req, res);
    }
}