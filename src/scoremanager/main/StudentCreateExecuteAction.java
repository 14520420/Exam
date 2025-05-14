package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;

public class StudentCreateExecuteAction extends Action {
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");

		String error = null;
		String entYearStr = req.getParameter("ent_year");
		String classNum = req.getParameter("class_num");
		String no = req.getParameter("no");
		String name = req.getParameter("name");
		ClassNumDao cNumDao = new ClassNumDao();
		LocalDate todaysDate = LocalDate.now();
		;// LcalDateインスタンスを取得
		int year = todaysDate.getYear();// 現在の年を取得
		int entYear = Integer.parseInt(entYearStr);

		if (entYear == 0) {
			error = "入学年度を入力してください。";
			req.setAttribute("ageerror", error);
		}
		// 学生番号取得
		StudentDao dao = new StudentDao();
		Student student_cd;
		try {
			student_cd = dao.get(no);
		} catch (Exception e) {
			throw e;
		}
		// 学生番号の重複チェック
		if (student_cd != null) {
			error = "学生番号が重複しています";
			req.setAttribute("cderror", error);
		}
		// errorがnullではない場合
		if (error != null) {
			// もう一度必要情報取得後入力フォームへ
			List<String> c_list = null;

			// リスト初期化
			List<Integer> entYearSet = new ArrayList<>();
			// -10~+10年までlist登録
			for (int i = year - 10; i < year + 10; i++) {
				entYearSet.add(i);
			}
			// ユーザの学校コードをもとにクラス番号一覧取得
			c_list = cNumDao.filter(teacher.getSchool());

			req.setAttribute("f1", entYear);
			req.setAttribute("f2", no);
			req.setAttribute("f3", name);
			req.setAttribute("f4", classNum);
			req.setAttribute("class_num_set", c_list);
			req.setAttribute("ent_year_set", entYearSet);
			req.getRequestDispatcher("student_create.jsp").forward(req, res);
			// errorがnullの場合
		} else {
			Student s = new Student();
			s.setNo(no);
			s.setName(name);
			s.setEntYear(entYear);
			s.setClassNum(classNum);
			s.setAttend(true);
			s.setSchool(teacher.getSchool());
			boolean result = dao.save(s);

			if (result) {
				res.sendRedirect("student_create_done.jsp");
			} else {
				req.setAttribute("error", "登録に失敗しました。");
				req.getRequestDispatcher("student_create.jsp").forward(req, res);
			}
		}
	}
} 