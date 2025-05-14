package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.ClassNumDao;
import dao.SubjectDao;
import tool.Action;

/**
 * 成績登録画面を表示するアクションクラス
 */
public class TestRegist extends Action {

    /**
     * アクションの実行メソッド
     * @param request HTTPリクエスト
     * @param response HTTPレスポンス
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // セッションからログイン教師情報を取得
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // 未ログインの場合はログイン画面へリダイレクト
        if (teacher == null || !teacher.isAuthenticated()) {
            response.sendRedirect("../Login.action");
            return;
        }

        // 学校情報を取得
        School school = teacher.getSchool();

        // 入学年度リストを作成（現在年から5年前まで）
        int currentYear = LocalDate.now().getYear();
        List<Integer> yearList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            yearList.add(currentYear - i);
        }

        // クラス番号リストを取得
        ClassNumDao classNumDao = new ClassNumDao();
        List<String> classList = classNumDao.filter(school);

        // 科目一覧を取得
        SubjectDao subjectDao = new SubjectDao();
        List<Subject> subjectList = subjectDao.filter(school);

        // テスト回数リスト
        List<Integer> timesList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            timesList.add(i);
        }

        // リクエスト属性に設定
        request.setAttribute("yearList", yearList);
        request.setAttribute("classList", classList);
        request.setAttribute("subjectList", subjectList);
        request.setAttribute("timesList", timesList);

        // デフォルト選択値をセット
        request.setAttribute("selectedYear", yearList.isEmpty() ? "" : yearList.get(0));
        request.setAttribute("selectedClass", classList.isEmpty() ? "" : classList.get(0));
        request.setAttribute("selectedSubject", subjectList.isEmpty() ? "" : subjectList.get(0).getCd());
        request.setAttribute("selectedTimes", 1);

        // JSPへフォワード
        request.getRequestDispatcher("test_regist.jsp").forward(request, response);
    }}
