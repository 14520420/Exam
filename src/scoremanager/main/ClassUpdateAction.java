// 変更のためjspへ
package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;

public class ClassUpdateAction extends Action {

	@Override
	public void execute(
		HttpServletRequest req, HttpServletResponse res
	) throws Exception {
		// 変数定義
		String class_num;
		int c_count;

		// 値取得
        class_num = req.getParameter("class_num");
        c_count = Integer.parseInt(req.getParameter("c_count"));

        // 値セット
        req.setAttribute("old_class_num", class_num);
        req.setAttribute("c_count", c_count);

		req.getRequestDispatcher("class_update.jsp").forward(req, res);
	}

}
