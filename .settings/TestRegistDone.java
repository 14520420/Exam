package scoremanager.main;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

/**
 * 成績登録完了処理を行うアクションクラス
 */
public class TestRegistDone extends Action {

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

        // GET リクエストの場合は成績登録画面にリダイレクト
        if ("GET".equals(request.getMethod())) {
            response.sendRedirect("TestRegist.action");
            return;
        }

        // 学校情報を取得
        School school = teacher.getSchool();

        // リクエストパラメータの取得
        String subjectCd = request.getParameter("subjectId");
        String timesStr = request.getParameter("times");
        String studentCountStr = request.getParameter("studentCount");
        String classNum = request.getParameter("classNum");

        if (subjectCd == null || timesStr == null || studentCountStr == null || classNum == null) {
            // パラメータが不足している場合はエラー
            request.setAttribute("error", "必要なパラメータが不足しています");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        int times = Integer.parseInt(timesStr);
        int studentCount = Integer.parseInt(studentCountStr);

        // 科目情報を取得
        SubjectDao subjectDao = new SubjectDao();
        Subject subject = subjectDao.get(subjectCd, school);
        if (subject == null) {
            request.setAttribute("error", "科目が見つかりません");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // DAOの作成
        TestDao testDao = new TestDao();
        StudentDao studentDao = new StudentDao();

        // テストデータを保存するリスト
        List<Test> testList = new ArrayList<>();

        // 各学生の成績を保存リストに追加
        for (int i = 0; i < studentCount; i++) {
            String studentId = request.getParameter("studentId" + i);
            String scoreStr = request.getParameter("score" + i);

            if (studentId != null && scoreStr != null && !scoreStr.trim().isEmpty()) {
                try {
                    int score = Integer.parseInt(scoreStr);
                    // スコアの範囲チェック（0〜100）
                    if (score >= 0 && score <= 100) {
                        // 学生情報を取得
                        Student student = studentDao.get(studentId);
                        if (student != null) {
                            // テストデータを作成
                            Test test = new Test();
                            test.setStudent(student);
                            test.setSubject(subject);
                            test.setSchool(school);
                            test.setNo(times);
                            test.setPoint(score);
                            test.setClassNum(classNum);
                            testList.add(test);
                        }
                    }
                } catch (NumberFormatException e) {
                    // スコアが数値でない場合はスキップ
                }
            }
        }

        // 成績を保存
        boolean success = false;
        if (!testList.isEmpty()) {
            success = testDao.save(testList);
        }

        // 成功ステータスをセット
        request.setAttribute("registrationSuccess", success);

        // JSPへフォワード
        request.getRequestDispatcher("test_regist_done.jsp").forward(request, response);
    }
} 