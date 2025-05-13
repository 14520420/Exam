// 入力内容判定、db登録
package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassCreateExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// 変数定義
		String class_num, error = null;
		boolean deci;
		// セッション
		HttpSession session = req.getSession(false);
		Teacher teacher = (Teacher)session.getAttribute("user");
		// bean,dao
		ClassNum cNum = new ClassNum();
		ClassNumDao cNumDao = new ClassNumDao();
		ClassNum class_cd = null;

		// 値取得
		class_num = req.getParameter("class_num");

		// dbクラス番号取得
		try {
			class_cd = cNumDao.get(class_num,teacher.getSchool());
		} catch(Exception e) {
			throw e;
		}

		// 重複チェック
		if (class_cd != null) {
			//errorに文字セット
			error = "クラス番号が重複しています";
			req.setAttribute("cderror", error);
		}

		// errorがnullでなければ入力フォームに
		if (error != null) {
			req.setAttribute("f1", class_num);
			req.getRequestDispatcher("class_create.jsp").forward(req, res);
		// errorがnullなら登録処理を実施し結果フォームへ
		} else {
			// beanに値セット
			cNum.setClass_num(class_num);
			cNum.setSchool(teacher.getSchool());

			// db処理結果取得
			deci = cNumDao.save(cNum);
			// 結果判定、文字登録
			if (deci) {
				req.setAttribute("suc", "登録が完了しました。");
			} else {
				req.setAttribute("suc", "登録に失敗しました。内容確認の上もう一度お願いします。");
			}

			req.getRequestDispatcher("class_create_done.jsp").forward(req, res);
		}

	}

}
