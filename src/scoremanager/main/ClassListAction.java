// classlist一覧表示
package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.ClassNum;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class ClassListAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res)
		throws Exception {
		HttpSession session = req.getSession(); //セッション
		Teacher teacher = (Teacher)session.getAttribute("user");

		// 変数宣言
		List<ClassNum> classes = null;

		//bean,dao
		StudentDao sDao = new StudentDao();

		// データ取得
		classes = sDao.class_count(teacher.getSchool());

		// レスポンス値セット
		req.setAttribute("classes", classes);

		// JSPへフォワード
		req.getRequestDispatcher("class_list.jsp").forward(req, res);
	}
}
