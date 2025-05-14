package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.StudentGrade;

public class StudentGradeDao extends Dao {
    /**
     * 特定学生の科目ごとの成績を取得する
     * @param studentNo 学生番号
     * @return 学生の成績リスト
     * @throws Exception
     */
    public List<StudentGrade> findByStudent(String studentNo) throws Exception {
        List<StudentGrade> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT sub.name, t.point " +
                 "FROM test t " +
                 "JOIN subject sub ON t.subject_cd = sub.cd " +
                 "WHERE t.student_no = ? " +
                 "ORDER BY sub.cd")) {

            stmt.setString(1, studentNo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    StudentGrade g = new StudentGrade();
                    g.setSubjectName(rs.getString("name"));
                    g.setPoint(rs.getInt("point"));
                    list.add(g);
                }
            }
        } catch (Exception e) {
            throw new Exception("学生別成績データの取得中にエラーが発生しました: " + e.getMessage(), e);
        }

        return list;
    }
} 