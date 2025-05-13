// 削除処理
package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassDeleteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        // 変数定義
        String class_num, error = null;
        int c_count;
        boolean deci = false;
        // セッション
        HttpSession session = req.getSession(false);
        Teacher teacher = (Teacher)session.getAttribute("user");
        // bean,dao
        ClassNum cNum = new ClassNum();
        ClassNumDao cNumDao = new ClassNumDao();

        // 値取得
        class_num = req.getParameter("class_num");
        c_count = Integer.parseInt(req.getParameter("c_count"));

        // 在籍生徒がいる場合
        if (c_count > 0) {
        	// errorに文字セット
            error = "在籍している生徒を移したのち削除してください。";
            req.setAttribute("error", error);
        // 在籍生徒がいない場合
        } else {
        	// beanに値セット
        	cNum.setClass_num(class_num);
            cNum.setSchool(teacher.getSchool());

            // db処理結果取得
            deci = cNumDao.delete(cNum);
        }

        // 結果判定、文字登録
        if (deci) {
            req.setAttribute("suc", "削除が完了しました。");
        } else {
            req.setAttribute("suc", "削除に失敗しました。内容確認の上もう一度お願いします。");
        }

        req.getRequestDispatcher("class_delete_done.jsp").forward(req, res);
    }
}
