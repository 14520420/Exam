package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.ClassNum;
import bean.School;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassCreateExecuteAction extends Action {

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

        // リクエストパラメータの取得
        String classNum = req.getParameter("class_num");

        // バリデーション
        Map<String, String> errors = new HashMap<>();
        if (classNum == null || classNum.trim().isEmpty()) {
            errors.put("class_num", "クラス番号を入力してください");
        }

        // 既存のクラスとの重複チェック
        ClassNumDao dao = new ClassNumDao();
        ClassNum existingClass = dao.get(classNum, school);
        if (existingClass != null) {
            errors.put("class_num", "このクラス番号は既に登録されています");
        }

        // エラーがある場合は入力画面に戻る
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("class_num", classNum);
            req.getRequestDispatcher("class_create.jsp").forward(req, res);
            return;
        }

        // クラス情報を保存
        ClassNum newClass = new ClassNum();
        newClass.setClass_num(classNum);
        newClass.setSchool(school);

        boolean result = dao.save(newClass);

        if (result) {
            // 成功した場合は完了画面へ
            req.setAttribute("class_num", classNum);
            req.getRequestDispatcher("class_create_done.jsp").forward(req, res);
        } else {
            // 失敗した場合はエラーメッセージを表示して入力画面へ
            errors.put("db", "クラスの登録に失敗しました");
            req.setAttribute("errors", errors);
            req.setAttribute("class_num", classNum);
            req.getRequestDispatcher("class_create.jsp").forward(req, res);
        }
    }
} 