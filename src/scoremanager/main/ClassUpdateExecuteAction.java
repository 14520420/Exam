// 変更処理
package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassUpdateExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// 変数定義
		String class_num,old_class_num, messege = null, error = null;
		int c_count;
		boolean deci = false;
		// セッション
		HttpSession session = req.getSession(false);
		Teacher teacher = (Teacher)session.getAttribute("user");
		// bean,dao
		ClassNum cNum = new ClassNum();
		ClassNumDao cNumDao = new ClassNumDao();
		ClassNum class_cd = null;

		// 値取得
		class_num = req.getParameter("class_num");
		old_class_num = req.getParameter("old_class_num");
		c_count = Integer.parseInt(req.getParameter("c_count"));

		// クラス番号取得
		try {
			class_cd = cNumDao.get(class_num,teacher.getSchool());
		} catch(Exception e) {
			throw e;
		}

		// クラス番号重複チェック
		if (!class_num.equals(old_class_num)) {
			if (class_cd != null) {
				error = "クラス番号が重複しています";
				req.setAttribute("cderror", error);
			}
		// クラス番号が同じだった場合
		} else {
			messege = "変更はありませんでした。";
		}

		// errorがnullではない場合入力フォームへ
		if (error != null) {
		    req.setAttribute("c_count", c_count);
		    req.setAttribute("old_class_num", old_class_num);
		    req.setAttribute("f1", class_num);
		    req.getRequestDispatcher("class_update.jsp").forward(req, res);
		    // もし `messege` が存在する場合
		}else if (messege != null) {
	        req.setAttribute("suc", messege);
	        req.getRequestDispatcher("class_update_done.jsp").forward(req, res);
	    } else {
	        // `messege` が null の場合
	        // 学生がいる場合
	        if (c_count > 0) {
	            cNum.setClass_num(class_num);
	            cNum.setSchool(teacher.getSchool());
	            cNum.setOld_class_num(old_class_num);
	            // 学生のクラス番号も変更
	            deci = cNumDao.update(cNum);
	        } else {
	            cNum.setClass_num(class_num);
	            cNum.setSchool(teacher.getSchool());
	            cNum.setOld_class_num(old_class_num);
	            // クラス番号のみ変更
	            deci = cNumDao.r_update(cNum);
	        }

	        // 結果登録
	        if (deci) {
	            req.setAttribute("suc", "変更が完了しました。");
	        } else {
	            req.setAttribute("suc", "変更に失敗しました。内容確認の上もう一度お願いします。");
	        }

	        req.getRequestDispatcher("class_update_done.jsp").forward(req, res);
	    }


	}

}
