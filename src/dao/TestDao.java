package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {

    private String baseSql = "SELECT * FROM test WHERE school_cd = ?";

    /**
     * ResultSetからTestオブジェクトのリストを生成する
     */
    private List<Test> postFilter(ResultSet rSet, School school) throws Exception {
        List<Test> list = new ArrayList<>();

        try {
            while (rSet.next()) {
                Test test = new Test();

                // student, subject はIDしか取れないので取得は別クラスに任せる
                Student student = new Student();
                student.setNo(rSet.getString("student_no"));
                test.setStudent(student);

                Subject subject = new Subject();
                subject.setCd(rSet.getString("subject_cd"));
                test.setSubject(subject);

                test.setSchool(school);
                test.setNo(rSet.getInt("no"));
                test.setPoint(rSet.getInt("point"));
                test.setClassNum(rSet.getString("class_num"));

                list.add(test);
            }
        } catch (SQLException e) {
            throw new Exception("テストデータの処理中にエラーが発生しました: " + e.getMessage());
        }

        return list;
    }

    /**
     * 特定のテスト情報を取得する
     */
    public Test get(Student student, Subject subject, School school, int no) throws Exception {
        Test test = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "SELECT * FROM test WHERE student_no=? AND subject_cd=? AND school_cd=? AND no=?")) {

            statement.setString(1, student.getNo());
            statement.setString(2, subject.getCd());
            statement.setString(3, school.getCd());
            statement.setInt(4, no);

            try (ResultSet rSet = statement.executeQuery()) {
                if (rSet.next()) {
                    test = new Test();
                    test.setStudent(student);
                    test.setSubject(subject);
                    test.setSchool(school);
                    test.setNo(no);
                    test.setPoint(rSet.getInt("point"));
                    test.setClassNum(rSet.getString("class_num"));
                }
            }
        } catch (SQLException e) {
            throw new Exception("テスト情報の取得中にエラーが発生しました: " + e.getMessage());
        }

        return test;
    }

    /**
     * 条件に基づいてテスト情報をフィルタリングする
     */
    public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school) throws Exception {
        List<Test> list = new ArrayList<>();

        try (Connection connection = getConnection()) {
            // 学生情報と結合して入学年度でフィルター
            String sql = baseSql + " AND class_num=? AND subject_cd=? AND no=?"
                    + " AND student_no IN (SELECT no FROM student WHERE ent_year=?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, school.getCd());
                statement.setString(2, classNum);
                statement.setString(3, subject.getCd());
                statement.setInt(4, num);
                statement.setInt(5, entYear);

                try (ResultSet rSet = statement.executeQuery()) {
                    list = postFilter(rSet, school);
                }
            }
        } catch (SQLException e) {
            throw new Exception("テスト情報のフィルタリング中にエラーが発生しました: " + e.getMessage());
        }

        return list;
    }

    /**
     * テスト情報のリストを保存する
     */
    public boolean save(List<Test> list) throws Exception {
        boolean result = true;
        Connection connection = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            for (Test test : list) {
                if (!save(test, connection)) {
                    result = false;
                    break;
                }
            }

            if (result) {
                connection.commit();
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    // ロールバックエラーは無視
                }
            }
            throw new Exception("テスト情報の保存中にエラーが発生しました: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    // 接続クローズエラーは無視
                }
            }
        }

        return result;
    }

    /**
     * 単一のテスト情報を保存する（内部メソッド）
     */
    public boolean save(Test test, Connection connection) throws Exception {
        boolean result = false;
        PreparedStatement statement = null;

        try {
            Test old = get(test.getStudent(), test.getSubject(), test.getSchool(), test.getNo());

            if (old == null) {
                // 新規追加
                statement = connection.prepareStatement(
                    "INSERT INTO test (student_no, subject_cd, school_cd, no, point, class_num) VALUES (?, ?, ?, ?, ?, ?)"
                );
                statement.setString(1, test.getStudent().getNo());
                statement.setString(2, test.getSubject().getCd());
                statement.setString(3, test.getSchool().getCd());
                statement.setInt(4, test.getNo());
                statement.setInt(5, test.getPoint());
                statement.setString(6, test.getClassNum());
            } else {
                // 更新
                statement = connection.prepareStatement(
                    "UPDATE test SET point=?, class_num=? WHERE student_no=? AND subject_cd=? AND school_cd=? AND no=?"
                );
                statement.setInt(1, test.getPoint());
                statement.setString(2, test.getClassNum());
                statement.setString(3, test.getStudent().getNo());
                statement.setString(4, test.getSubject().getCd());
                statement.setString(5, test.getSchool().getCd());
                statement.setInt(6, test.getNo());
            }

            result = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new Exception("テスト情報の保存中にエラーが発生しました: " + e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ステートメントクローズエラーは無視
                }
            }
        }

        return result;
    }

    /**
     * 複数の条件に基づいてテスト情報を検索する
     */
    public List<Test> selectByConditions(Map<String,String> cond, School school) throws Exception {
        List<Test> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            // 学生の入学年度を確実に取得するため、SELECT句に s.ent_year を追加
            "SELECT t.*, s.name student_name, s.ent_year, t.class_num " +
            "FROM test t JOIN student s ON t.student_no=s.no AND t.school_cd=s.school_cd " +
            "WHERE t.school_cd=?"
        );
        List<Object> prm = new ArrayList<>();
        prm.add(school.getCd());

        // 条件追加
        if (cond.containsKey("entYear") && !cond.get("entYear").isEmpty()) {
            sql.append(" AND s.ent_year=?");
            prm.add(Integer.parseInt(cond.get("entYear")));
        }
        if (cond.containsKey("classNum") && !cond.get("classNum").isEmpty()) {
            sql.append(" AND t.class_num=?");
            prm.add(cond.get("classNum"));
        }
        if (cond.containsKey("subjectCd") && !cond.get("subjectCd").isEmpty()) {
            sql.append(" AND t.subject_cd=?");
            prm.add(cond.get("subjectCd"));
        }
        if (cond.containsKey("no") && !cond.get("no").isEmpty()) {
            sql.append(" AND t.no=?");
            prm.add(Integer.parseInt(cond.get("no")));
        }
        if (cond.containsKey("studentInfo") && !cond.get("studentInfo").isEmpty()) {
            sql.append(" AND (s.no LIKE ? OR s.name LIKE ?)");
            String kw = "%" + cond.get("studentInfo") + "%";
            prm.add(kw);
            prm.add(kw);
        }
        sql.append(" ORDER BY s.ent_year, t.class_num, s.no, t.no");

        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql.toString())) {

            // パラメータをセット
            for (int i = 0; i < prm.size(); i++) {
                st.setObject(i + 1, prm.get(i));
            }

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Test t = new Test();
                    Student stu = new Student();
                    stu.setNo(rs.getString("student_no"));
                    stu.setName(rs.getString("student_name"));
                    // 入学年度を明示的にセット
                    stu.setEntYear(rs.getInt("ent_year"));
                    t.setStudent(stu);

                    Subject sbj = new Subject();
                    sbj.setCd(rs.getString("subject_cd"));
                    t.setSubject(sbj);

                    t.setSchool(school);
                    t.setNo(rs.getInt("no"));
                    t.setPoint(rs.getInt("point"));
                    t.setClassNum(rs.getString("class_num"));

                    list.add(t);
                }
            }
        } catch (SQLException e) {
            throw new Exception("テスト情報の検索中にエラーが発生しました: " + e.getMessage());
        }

        return list;
    }
} 