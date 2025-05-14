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

/**
 * 成績参照画面を表示するアクションクラス
 */
public class TestList extends Action {

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

        // リクエストパラメータの取得
        String entYear = nv(request.getParameter("f1"));
        String classNum = nv(request.getParameter("f2"));
        String subjectCd = nv(request.getParameter("f3"));
        String testNo = nv(request.getParameter("f4"));
        String studentKW = nv(request.getParameter("studentInfo"));

        // プルダウン用データの準備
        int now = LocalDate.now().getYear();
        List<Integer> entYearSet = new ArrayList<>();
        for (int y = now - 10; y <= now; y++) {
            entYearSet.add(y);
        }

        // クラスと科目リストの取得
        ClassNumDao cDao = new ClassNumDao();
        SubjectDao sDao = new SubjectDao();
        List<String> classNumSet = nvl(cDao.filter(school));
        List<Subject> subjects = nvl(sDao.filter(school));

        // リクエスト属性に設定
        request.setAttribute("ent_year_set", entYearSet);
        request.setAttribute("class_num_set", classNumSet);
        request.setAttribute("subjects", subjects);

        // 検索条件マップの作成
        Map<String, String> cond = new HashMap<>();
        if (!entYear.isEmpty()) cond.put("entYear", entYear);
        if (!classNum.isEmpty()) cond.put("classNum", classNum);
        if (!subjectCd.isEmpty()) cond.put("subjectCd", subjectCd);
        if (!testNo.isEmpty()) cond.put("no", testNo);
        if (!studentKW.isEmpty()) cond.put("studentInfo", studentKW);

        // テスト情報の取得
        TestDao tDao = new TestDao();
        List<Test> tests = tDao.selectByConditions(cond, school);

        // リクエスト属性に設定
        request.setAttribute("conditions", cond);
        request.setAttribute("tests", nvl(tests));

        // JSPへフォワード
        request.getRequestDispatcher("test_list.jsp").forward(request, response);
    }

    // null または 空文字 を空文字に変換
    private String nv(String s) {
        return s == null ? "" : s.trim();
    }

    // null のリストを空のリストに変換
    private <T> List<T> nvl(List<T> l) {
        return l == null ? new ArrayList<>() : l;
    }
} 