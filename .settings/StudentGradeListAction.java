package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.StudentGrade;
import dao.StudentGradeDao;
import tool.Action;

public class StudentGradeListAction extends Action {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String studentNo = request.getParameter("student_no");
        StudentGradeDao dao = new StudentGradeDao();
        List<StudentGrade> grades = dao.findByStudent(studentNo);

        request.setAttribute("grades", grades);
        request.getRequestDispatcher("scoremanager/main/student_grade_list.jsp").forward(request, response);
    }
}
 