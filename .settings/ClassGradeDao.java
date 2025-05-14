package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.ClassGrade;

public class ClassGradeDao extends Dao {
    /**
     * 特定クラスの科目ごとの平均点を取得する
     * @param classNum クラス番号
     * @return 科目ごとの平均点リスト
     * @throws Exception
     */
    public List<ClassGrade> findByClass(String classNum) throws Exception {
        List<ClassGrade> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT sub.name, AVG(t.point) AS avg_point " +
                 "FROM test t JOIN subject sub ON t.subject_cd = sub.cd " +
                 "WHERE t.class_num = ? " +
                 "GROUP BY sub.name " +
                 "ORDER BY sub.name")) {

            stmt.setString(1, classNum);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ClassGrade g = new ClassGrade();
                    g.setSubjectName(rs.getString("name"));
                    g.setAvgPoint(rs.getDouble("avg_point"));
                    list.add(g);
                }
            }
        } catch (Exception e) {
            throw new Exception("クラス別成績データの取得中にエラーが発生しました: " + e.getMessage(), e);
        }

        return list;
    }
} 