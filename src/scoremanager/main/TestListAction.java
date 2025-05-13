package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestListAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// 1. ログインチェック
		HttpSession ses = req.getSession();
		Teacher teacher = (Teacher) ses.getAttribute("user");
		if (teacher == null) {
			res.sendRedirect("Login.action");
			return;
		}
		School school = teacher.getSchool();

		// 2. リクエストパラメータ
		String entYear = nv(req.getParameter("f1"));
		String classNum = nv(req.getParameter("f2"));
		String subjectCd = nv(req.getParameter("f3"));
		String testNo = nv(req.getParameter("f4"));
		String studentKW = nv(req.getParameter("studentInfo"));

		// 3. プルダウン用データ
		int now = LocalDate.now().getYear();
		List<Integer> entYearSet = new ArrayList<>();
		for (int y = now - 10; y <= now; y++)
			entYearSet.add(y);

		ClassNumDao cDao = new ClassNumDao();
		SubjectDao sDao = new SubjectDao();
		List<String> classNumSet = nvl(cDao.filter(school));
		List<Subject> subjects = nvl(sDao.filter(school));

		req.setAttribute("ent_year_set", entYearSet);
		req.setAttribute("class_num_set", classNumSet);
		req.setAttribute("subjects", subjects);

		// 4. 検索条件マップ（空なら全件）
		Map<String, String> cond = new HashMap<>();
		if (!entYear.isEmpty())
			cond.put("entYear", entYear);
		if (!classNum.isEmpty())
			cond.put("classNum", classNum);
		if (!subjectCd.isEmpty())
			cond.put("subjectCd", subjectCd);
		if (!testNo.isEmpty())
			cond.put("no", testNo);
		if (!studentKW.isEmpty())
			cond.put("studentInfo", studentKW);
		// 追加：選択中の科目情報（1件）を取得してセット
		Subject selectedSubject = null;
		for (Subject s : subjects) {
			if (s.getCd().equals(subjectCd)) {
				selectedSubject = s;
				break;
			}
		}
		req.setAttribute("selectedSubject", selectedSubject);
		// 5. 得点取得
		TestDao tDao = new TestDao();
		List<Test> tests = tDao.selectByConditions(cond, school); // DAO側で cond
																	// 空=全件

		// 6. 画面へ
		req.setAttribute("conditions", cond);
		req.setAttribute("tests", nvl(tests));
		req.getRequestDispatcher("test_list.jsp").forward(req, res);
	}

	private String nv(String s) {
		return s == null ? "" : s.trim();
	}

	private <T> List<T> nvl(List<T> l) {
		return l == null ? new ArrayList<>() : l;
	}
}
