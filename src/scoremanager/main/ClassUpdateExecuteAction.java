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

public class ClassUpdateExecuteAction extends Action {

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
        String oldClassNum = req.getParameter("old_class_num");
        String newClassNum = req.getParameter("new_class_num");

        // バリデーション
        Map<String, String> errors = new HashMap<>();
        if (newClassNum == null || newClassNum.trim().isEmpty()) {
            errors.put("class_num", "クラス番号を入力してください");
        }

        // 既存のクラスとの重複チェック（自分自身以外）
        if (!oldClassNum.equals(newClassNum)) {
            ClassNumDao dao = new ClassNumDao();
            ClassNum existingClass = dao.get(newClassNum, school);
            if (existingClass != null) {
                errors.put("class_num", "このクラス番号は既に登録されています");
            }
        }

        // エラーがある場合は入力画面に戻る
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("old_class_num", oldClassNum);
            req.setAttribute("new_class_num", newClassNum);
            req.getRequestDispatcher("class_update.jsp").forward(req, res);
            return;
        }

        // クラス情報を更新
        ClassNum classNum = new ClassNum();
        classNum.setClass_num(newClassNum);
        classNum.setOld_class_num(oldClassNum);
        classNum.setSchool(school);

        ClassNumDao dao = new ClassNumDao();
        boolean result = dao.update(classNum);

        if (result) {
            // 成功した場合は完了画面へ
            req.setAttribute("old_class_num", oldClassNum);
            req.setAttribute("new_class_num", newClassNum);
            req.getRequestDispatcher("class_update_done.jsp").forward(req, res);
        } else {
            // 失敗した場合はエラーメッセージを表示して入力画面へ
            errors.put("db", "クラスの更新に失敗しました");
            req.setAttribute("errors", errors);
            req.setAttribute("old_class_num", oldClassNum);
            req.setAttribute("new_class_num", newClassNum);
            req.getRequestDispatcher("class_update.jsp").forward(req, res);
        }
    }
} 