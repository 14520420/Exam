package scoremanager.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

/**
 * 成績入力画面を表示するアクションクラス
 */
public class TestRegistExecute extends Action {

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
        String year = request.getParameter("year");
        String classNum = request.getParameter("class");
        String subjectCd = request.getParameter("subject");
        String timesStr = request.getParameter("times");

        if (year == null || classNum == null || subjectCd == null || timesStr == null) {
            // パラメータが不足している場合は登録画面へリダイレクト
            response.sendRedirect("TestRegist.action");
            return;
        }

        int times = Integer.parseInt(timesStr);
        int entYear = Integer.parseInt(year);

        // DAO インスタンスを作成
        StudentDao studentDao = new StudentDao();
        SubjectDao subjectDao = new SubjectDao();
        TestDao testDao = new TestDao();

        // 科目情報を取得
        Subject subject = subjectDao.get(subjectCd, school);
        if (subject == null) {
            request.setAttribute("error", "科目が見つかりません");
            request.getRequestDispatcher("TestRegist.action").forward(request, response);
            return;
        }

        // 検索条件を持つMapを作成
        Map<String, String> conditions = new HashMap<>();
        conditions.put("entYear", year);
        conditions.put("classNum", classNum);
        conditions.put("subjectCd", subjectCd);
        conditions.put("no", String.valueOf(times));

        // テスト情報を取得
        List<Test> testList = testDao.selectByConditions(conditions, school);

        // 学生リストを取得
        List<Student> studentList = studentDao.filter(school, entYear, classNum, true);

        // 学生ごとにテスト情報を設定
        List<Test> studentScoreList = new ArrayList<>();

        // 既存データがある場合は学生リストと結合
        if (!testList.isEmpty()) {
            // 学生番号をキーにしたテストマップを作成
            Map<String, Test> testMap = new HashMap<>();
            for (Test test : testList) {
                testMap.put(test.getStudent().getNo(), test);
            }

            // 学生リストを元にスコアリストを作成
            for (Student student : studentList) {
                Test test = testMap.get(student.getNo());
                if (test == null) {
                    // テストデータがない場合は新規作成
                    test = new Test();
                    test.setStudent(student);
                    test.setSubject(subject);
                    test.setSchool(school);
                    test.setClassNum(classNum);
                    test.setNo(times);
                    test.setPoint(0); // デフォルト値
                }
                studentScoreList.add(test);
            }
        } else {
            // テストデータがない場合は学生リストからスコアリストを新規作成
            for (Student student : studentList) {
                Test test = new Test();
                test.setStudent(student);
                test.setSubject(subject);
                test.setSchool(school);
                test.setClassNum(classNum);
                test.setNo(times);
                test.setPoint(0); // デフォルト値
                studentScoreList.add(test);
            }
        }

        // リクエスト属性に設定
        request.setAttribute("yearList", getYearList());
        request.setAttribute("classList", getClassList(school));
        request.setAttribute("subjectList", subjectDao.filter(school));
        request.setAttribute("timesList", getTimesList());

        // 選択値をセット
        request.setAttribute("selectedYear", year);
        request.setAttribute("selectedClass", classNum);
        request.setAttribute("selectedSubject", subjectCd);
        request.setAttribute("selectedTimes", timesStr);
        request.setAttribute("selectedSubjectName", subject.getName());

        // 学生スコアリストをセット
        request.setAttribute("studentScoreList", studentScoreList);

        // JSPへフォワード
        request.getRequestDispatcher("test_regist_execute.jsp").forward(request, response);
    }

    // 入学年度リストを生成（ヘルパーメソッド）
    private List<Integer> getYearList() {
        List<Integer> yearList = new ArrayList<>();
        int currentYear = java.time.LocalDate.now().getYear();
        for (int i = 0; i < 5; i++) {
            yearList.add(currentYear - i);
        }
        return yearList;
    }

    // クラスリストを取得（ヘルパーメソッド）
    private List<String> getClassList(School school) throws Exception {
        ClassNumDao classNumDao = new ClassNumDao();
        return classNumDao.filter(school);
    }

    // テスト回数リストを生成（ヘルパーメソッド）
    private List<Integer> getTimesList() {
        List<Integer> timesList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            timesList.add(i);
        }
        return timesList;
    }
} 