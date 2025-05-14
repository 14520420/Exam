package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.ClassNum;
import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import tool.Action;

public class TestRegistAction extends Action {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // ログインユーザー情報取得
            HttpSession session = request.getSession();
            Teacher teacher = (Teacher) session.getAttribute("user");

            if (teacher == null) {
                response.sendRedirect("Login.action");
                return;
            }

            // 入学年度リストを作成（現在年から5年前まで）
            int currentYear = LocalDate.now().getYear();
            List<Integer> entYearList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                entYearList.add(currentYear - i);
            }
            request.setAttribute("entYearList", entYearList);

            // クラス番号リストを取得
            ClassNumDao classNumDao = new ClassNumDao();
            List<String> classNumStrList = classNumDao.filter(teacher.getSchool());
            List<ClassNum> classList = new ArrayList<>();
            for (String num : classNumStrList) {
                ClassNum c = new ClassNum();
                c.setClass_num(num);
                c.setSchool(teacher.getSchool());
                classList.add(c);
            }
            request.setAttribute("classList", classList);

            // 科目一覧を取得
            SubjectDao subjectDao = new SubjectDao();
            request.setAttribute("subjectList", subjectDao.filter(teacher.getSchool()));

            // 検索条件の取得
            String entYear = request.getParameter("ent_year");
            String classNum = request.getParameter("class_num");
            String subjectCd = request.getParameter("subject_cd"); // 'subjectId' ではなく 'cd' を使用
            String no = request.getParameter("no");

            // 検索条件がすべて入力されていれば処理
            if (entYear != null && !entYear.isEmpty() &&
                classNum != null && !classNum.isEmpty() &&
                subjectCd != null && !subjectCd.isEmpty() &&
                no != null && !no.isEmpty()) {

                try {
                    // 学生一覧取得
                    StudentDao studentDao = new StudentDao();
                    List<Student> studentList = studentDao.filter(teacher.getSchool(),
                                                                Integer.parseInt(entYear),
                                                                classNum,
                                                                true);
                    request.setAttribute("studentList", studentList);

                    // 条件の保持（hidden再表示用）
                    request.setAttribute("selectedEntYear", entYear);
                    request.setAttribute("selectedClassNum", classNum);
                    request.setAttribute("selectedSubjectCd", subjectCd);
                    request.setAttribute("selectedNo", no);

                    request.getRequestDispatcher("test_regist_result.jsp").forward(request, response);
                } catch (Exception e) {
                    // エラー処理
                    request.setAttribute("error", "データ取得中にエラーが発生しました: " + e.getMessage());
                    request.getRequestDispatcher("test_regist.jsp").forward(request, response);
                }
            } else {
                // 初期表示
                request.getRequestDispatcher("test_regist.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // エラー処理
            request.setAttribute("error", "データ取得中にエラーが発生しました: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
} 