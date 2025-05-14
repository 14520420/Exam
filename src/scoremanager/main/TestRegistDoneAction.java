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
public class TestRegistDoneAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン教師情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // 未ログインの場合はログイン画面へリダイレクト
        if (teacher == null || !teacher.isAuthenticated()) {
            res.sendRedirect("../Login.action");
            return;
        }

        // 学校情報を取得
        School school = teacher.getSchool();

        // リクエストパラメータの取得
        String subjectId = req.getParameter("subjectId");
        String timesStr = req.getParameter("times");
        String studentCountStr = req.getParameter("studentCount");
        String classNum = req.getParameter("class");

        if (subjectId == null || timesStr == null || studentCountStr == null) {
            req.setAttribute("error", "必要なパラメータが不足しています");
            req.getRequestDispatcher("error.jsp").forward(req, res);
            return;
        }

        int times = Integer.parseInt(timesStr);
        int studentCount = Integer.parseInt(studentCountStr);

        // 科目情報を取得
        SubjectDao subjectDao = new SubjectDao();
        Subject subject = subjectDao.get(subjectId, school);
        if (subject == null) {
            req.setAttribute("error", "科目が見つかりません");
            req.getRequestDispatcher("error.jsp").forward(req, res);
            return;
        }

        // DAOの作成
        TestDao testDao = new TestDao();
        StudentDao studentDao = new StudentDao();

        // テストデータを保存するリスト
        List<Test> testList = new ArrayList<>();

        // 各学生の成績を保存リストに追加
        for (int i = 0; i < studentCount; i++) {
            String studentId = req.getParameter("studentId" + i);
            String scoreStr = req.getParameter("score" + i);

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
        req.setAttribute("registrationSuccess", success);

        // JSPへフォワード
        req.getRequestDispatcher("test_regist_done.jsp").forward(req, res);
    }
} 