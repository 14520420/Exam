package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import dao.StudentDao;
import tool.Action;

public class StudentUpdateExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // フォームから送信されたデータを取得
        String no = req.getParameter("no");
        String name = req.getParameter("name");
        int entYear = Integer.parseInt(req.getParameter("ent_year"));
        String classNum = req.getParameter("class_num");
        boolean isAttend = "on".equals(req.getParameter("is_attend")); // チェックボックスの値は"on"で送信される

        // 学生オブジェクトを作成して、フォームから取得したデータをセット
        Student student = new Student();
        student.setNo(no);
        student.setName(name);
        student.setEntYear(entYear);
        student.setClassNum(classNum);
        student.setAttend(isAttend);

        // 学生情報をデータベースに保存（更新）
        StudentDao dao = new StudentDao();
        boolean isUpdated = dao.save(student); // 学生情報の保存または更新

        // 更新が成功した場合、完了画面にリダイレクト
        if (isUpdated) {
            req.getRequestDispatcher("student_update_done.jsp").forward(req, res);
        } else {
            req.setAttribute("error", "学生情報の更新に失敗しました。");
            req.getRequestDispatcher("/main/student_list.jsp").forward(req, res);
        }
    }
} 