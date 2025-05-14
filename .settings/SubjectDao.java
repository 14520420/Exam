package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;

public class SubjectDao extends Dao {

    private String baseSql = "SELECT * FROM subject WHERE school_cd=?";

    /**
     * ResultSetからSubjectオブジェクトのリストを生成する
     */
    private List<Subject> postFilter(ResultSet rSet, School school) throws Exception {
        List<Subject> list = new ArrayList<>();
        try {
            while (rSet.next()) {
                Subject subject = new Subject();
                subject.setCd(rSet.getString("cd"));
                subject.setName(rSet.getString("name"));
                subject.setSchool(school);
                list.add(subject);
            }
        } catch (SQLException e) {
            throw new Exception("科目データの処理中にエラーが発生しました: " + e.getMessage());
        }
        return list;
    }

    /**
     * 科目1件取得（複合主キー：cd + school_cd）
     */
    public Subject get(String cd, School school) throws Exception {
        Subject subject = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "SELECT * FROM subject WHERE cd = ? AND school_cd = ?")) {

            statement.setString(1, cd);
            statement.setString(2, school.getCd());

            try (ResultSet rSet = statement.executeQuery()) {
                if (rSet.next()) {
                    subject = new Subject();
                    subject.setCd(rSet.getString("cd"));
                    subject.setName(rSet.getString("name"));
                    subject.setSchool(school);
                }
            }
        } catch (SQLException e) {
            throw new Exception("科目情報の取得中にエラーが発生しました: " + e.getMessage());
        }

        return subject;
    }

    /**
     * 学校ごとの全科目取得
     */
    public List<Subject> filter(School school) throws Exception {
        List<Subject> list = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 baseSql + " ORDER BY cd ASC")) {

            statement.setString(1, school.getCd());

            try (ResultSet rSet = statement.executeQuery()) {
                list = postFilter(rSet, school);
            }
        } catch (SQLException e) {
            throw new Exception("科目一覧の取得中にエラーが発生しました: " + e.getMessage());
        }

        return list;
    }

    /**
     * 科目保存（INSERT or UPDATE）
     */
    public boolean save(Subject subject) throws Exception {
        boolean result = false;

        try (Connection connection = getConnection()) {
            PreparedStatement statement = null;

            try {
                Subject old = get(subject.getCd(), subject.getSchool());

                if (old == null) {
                    // 新規追加
                    statement = connection.prepareStatement(
                        "INSERT INTO subject (cd, name, school_cd) VALUES (?, ?, ?)"
                    );
                    statement.setString(1, subject.getCd());
                    statement.setString(2, subject.getName());
                    statement.setString(3, subject.getSchool().getCd());
                } else {
                    // 更新
                    statement = connection.prepareStatement(
                        "UPDATE subject SET name = ? WHERE cd = ? AND school_cd = ?"
                    );
                    statement.setString(1, subject.getName());
                    statement.setString(2, subject.getCd());
                    statement.setString(3, subject.getSchool().getCd());
                }

                int count = statement.executeUpdate();
                result = count > 0;
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException e) {
            throw new Exception("科目情報の保存中にエラーが発生しました: " + e.getMessage());
        }

        return result;
    }

    /**
     * 科目削除
     */
    public boolean delete(Subject subject) throws Exception {
        boolean result = false;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "DELETE FROM subject WHERE cd = ? AND school_cd = ?")) {

            statement.setString(1, subject.getCd());
            statement.setString(2, subject.getSchool().getCd());

            int count = statement.executeUpdate();
            result = count > 0;
        } catch (SQLException e) {
            throw new Exception("科目情報の削除中にエラーが発生しました: " + e.getMessage());
        }

        return result;
    } 
}