package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Teacher;

public class TeacherDao extends Dao {

    /**
     * getメソッド 教員IDを指定して教員インスタンスを1件取得する
     *
     * @param id:String 教員ID
     * @return 教員クラスのインスタンス（存在しない場合はnull）
     * @throws Exception
     */
    public Teacher get(String id) throws Exception {
        // 教員インスタンスを初期化
        Teacher teacher = new Teacher();
        // コネクションを確立
        Connection connection = getConnection();
        // プリペアードステートメント
        PreparedStatement statement = null;

        try {
            // SQL文をセット
            statement = connection.prepareStatement("select * from teacher where id=?");
            // 教員IDをバインド
            statement.setString(1, id);
            // SQLを実行
            ResultSet resultSet = statement.executeQuery();

            // 学校情報を取得するためのSchoolDao
            SchoolDao schoolDao = new SchoolDao();

            if (resultSet.next()) {
                // 結果がある場合、教員情報をセット
                teacher.setId(resultSet.getString("id"));
                teacher.setPassword(resultSet.getString("password"));
                teacher.setName(resultSet.getString("name"));
                teacher.setAdmin(resultSet.getBoolean("is_admin"));
                // 学校情報をセット（学校コードから検索）
                teacher.setSchool(schoolDao.get(resultSet.getString("school_cd")));
            } else {
                // 結果がなければ null
                teacher = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            // statementを閉じる
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            // connectionを閉じる
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }

        return teacher;
    }

    /**
     * 指定した学校の教員一覧を取得
     *
     * @param school 学校情報
     * @return 教員リスト
     * @throws Exception
     */
    public List<Teacher> filter(School school) throws Exception {
        List<Teacher> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(
                "SELECT * FROM teacher WHERE school_cd = ? ORDER BY id"
            );
            statement.setString(1, school.getCd());
            resultSet = statement.executeQuery();

            SchoolDao schoolDao = new SchoolDao();

            while (resultSet.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(resultSet.getString("id"));
                teacher.setPassword(resultSet.getString("password"));
                teacher.setName(resultSet.getString("name"));
                teacher.setAdmin(resultSet.getBoolean("is_admin"));
                teacher.setSchool(schoolDao.get(resultSet.getString("school_cd")));
                list.add(teacher);
            }
        } catch (Exception e) {
            throw new Exception("教員情報の取得中にエラーが発生しました: " + e.getMessage());
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.close(); } catch (SQLException e) {}
        }

        return list;
    }

    /**
     * 教員情報の保存（新規登録または更新）
     *
     * @param teacher 教員情報
     * @return 成功した場合true、失敗した場合false
     * @throws Exception
     */
    public boolean save(Teacher teacher) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        int count = 0;

        try {
            connection = getConnection();

            // 既存の教員を検索
            Teacher existing = get(teacher.getId());

            if (existing == null) {
                // 新規登録
                statement = connection.prepareStatement(
                    "INSERT INTO teacher (id, password, name, school_cd, is_admin) VALUES (?, ?, ?, ?, ?)"
                );
                statement.setString(1, teacher.getId());
                statement.setString(2, teacher.getPassword());
                statement.setString(3, teacher.getName());
                statement.setString(4, teacher.getSchool().getCd());
                statement.setBoolean(5, teacher.isAdmin());
            } else {
                // 更新
                statement = connection.prepareStatement(
                    "UPDATE teacher SET password = ?, name = ?, school_cd = ?, is_admin = ? WHERE id = ?"
                );
                statement.setString(1, teacher.getPassword());
                statement.setString(2, teacher.getName());
                statement.setString(3, teacher.getSchool().getCd());
                statement.setBoolean(4, teacher.isAdmin());
                statement.setString(5, teacher.getId());
            }

            count = statement.executeUpdate();

        } catch (Exception e) {
            throw new Exception("教員情報の保存中にエラーが発生しました: " + e.getMessage());
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.close(); } catch (SQLException e) {}
        }

        return count > 0;
    }

    /**
     * 教員情報の削除
     *
     * @param id 教員ID
     * @return 成功した場合true、失敗した場合false
     * @throws Exception
     */
    public boolean delete(String id) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        int count = 0;

        try {
            connection = getConnection();
            statement = connection.prepareStatement("DELETE FROM teacher WHERE id = ?");
            statement.setString(1, id);
            count = statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception("教員情報の削除中にエラーが発生しました: " + e.getMessage());
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.close(); } catch (SQLException e) {}
        }

        return count > 0;
    }

    /**
     * loginメソッド 教員IDとパスワードで認証する
     *
     * @param id:String 教員ID
     * @param password:String パスワード
     * @return 認証成功：教員クラスのインスタンス、失敗：null
     * @throws Exception
     */
    public Teacher login(String id, String password) throws Exception {
        // 教員情報を取得
        Teacher teacher = get(id);
        // 教員が存在しない、またはパスワードが一致しない場合
        if (teacher == null || !teacher.getPassword().equals(password)) {
            return null;
        }
        // 認証成功
        return teacher;
    }
} 