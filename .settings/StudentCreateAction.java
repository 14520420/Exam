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
import tool.Action;


public class StudentCreateAction extends Action{
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//セッションのユーザデータを取得
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");

		String entYearStr = "";//入力された入学年度
		String classNum = "";//入力されたクラス番号
		String isAttendStr = "";
		int entYear = 0;//入学年度
		boolean isAttend = false;
		List<Student> students = null;//学生リスト

		LocalDate todaysDate = LocalDate.now();;//LcalDateインスタンスを取得
		int year = todaysDate.getYear();//現在の年を取得

		ClassNumDao cNumDao = new ClassNumDao();//クラス番号Dao

		//リクエストパラメータ―の取得
		entYearStr = req.getParameter("f1");
		classNum = req.getParameter("f2");
		isAttendStr = req.getParameter("f3");

		//ビジネスロジック
		if (entYearStr != null) {
			//数値に変換
			entYear = Integer.parseInt(entYearStr);
		}
		//list初期化
		List<Integer> entYearSet = new ArrayList<>();

		//10年前～１年後までリストに追加
		for (int i = year - 10; i < year + 1; i++) {
			entYearSet.add(i);
		}

		//DBからデータ取得
		List<String> list = cNumDao.filter(teacher.getSchool());


		//レスポンス値をセット 6
		//リクエストに入学年度をセット
		req.setAttribute ("f1", entYear);

		//リクエストにクラス番号をセット
		req.setAttribute ("f2", classNum);

		if (isAttendStr != null) {

		isAttend = true;

		req. setAttribute ("f3", isAttendStr) ;
		}

		//リクエストに学生をセット
		req. setAttribute ("students", students) ;

		//リクエストにデータをセット
		req. setAttribute ("class_num_set", list) ;
		req. setAttribute ("ent_year_set", entYearSet) ;

		//フォワード
		req. getRequestDispatcher ("student_create.jsp"). forward (req, res) ;

	}
} 