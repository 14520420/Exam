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
import dao.TestDao;
import tool.Action;

public class TestRegistExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // ログインユーザー情報取得
            HttpSession session = request.getSession();
            Teacher teacher = (Teacher) session.getAttribute("user");

            if (teacher == null) {
                response.sendRedirect("Login.action");
                return;
            }

            School school = teacher.getSchool();

            // リクエストパラメータの取得
            String[] studentNos = request.getParameterValues("student_no");
            String[] points = request.getParameterValues("point");
            String entYear = request.getParameter("ent_year");
            String classNum = request.getParameter("class_num");
            String subjectCd = request.getParameter("subject_cd"); // 'subjectId' ではなく 'cd' を使用
            String noStr = request.getParameter("no");

            if (studentNos == null || points == null) {
                throw new Exception("学生番号または点数のパラメータが不足しています");
            }

            int no = Integer.parseInt(noStr);

            // テスト情報のリスト作成
            List<Test> testList = new ArrayList<>();

            Subject subject = new Subject();
            subject.setCd(subjectCd);
            subject.setSchool(school);

            for (int i = 0; i < studentNos.length; i++) {
                Student student = new Student();
                student.setNo(studentNos[i]);

                Test test = new Test();
                test.setStudent(student);
                test.setSubject(subject);
                test.setSchool(school);
                test.setNo(no);
                test.setClassNum(classNum);

                try {
                    int point = Integer.parseInt(points[i]);
                    test.setPoint(point);
                } catch (NumberFormatException e) {
                    // 無効な点数の場合は0点にする
                    test.setPoint(0);
                }

                testList.add(test);
            }

            // テスト情報を保存
            TestDao testDao = new TestDao();
            boolean success = testDao.save(testList);

            if (success) {
                // 登録完了画面にフォワード
                request.getRequestDispatcher("test_regist_done.jsp").forward(request, response);
            } else {
                throw new Exception("成績の登録に失敗しました");
            }
        } catch (Exception e) {
            // エラー情報をセット
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
} 