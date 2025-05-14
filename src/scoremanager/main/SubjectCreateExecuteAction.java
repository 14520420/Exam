package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectCreateExecuteAction extends Action {
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // 入力値の取得
        String code = req.getParameter("cd");
        String name = req.getParameter("name");

        // 入力チェック
        Map<String, String> errors = new HashMap<>();
        if (code == null || code.trim().isEmpty()) {
            errors.put("code", "科目コードを入力してください");
        } else if (code.length() != 3) {
            errors.put("code", "科目コードは3文字で入力してください");
        }

        if (name == null || name.trim().isEmpty()) {
            errors.put("name", "科目名を入力してください");
        }

        SubjectDao dao = new SubjectDao();
        Subject subject = new Subject();
        subject.setCd(code); // ←ここ修正！！
        subject.setName(name);
        subject.setSchool(teacher.getSchool());

        // 重複チェック
        if (errors.isEmpty() && dao.get(code, teacher.getSchool()) != null) {
            errors.put("code", "科目コードが重複しています");
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("subject", subject);
            req.setAttribute("subject_list", dao.filter(teacher.getSchool())); // ←JSPで使うならこれも追加しておくと安心
            req.getRequestDispatcher("subject_create.jsp").forward(req, res);
            return;
        }

        // 登録（INSERT）
        dao.save(subject);

        // 完了画面へ遷移
        req.getRequestDispatcher("subject_create_done.jsp").forward(req, res);
    }
}
 
