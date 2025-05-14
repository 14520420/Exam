package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ClassNum;
import bean.School;

public class ClassNumDao extends Dao {
    public ClassNum get(String class_num, School school) throws Exception {
        // クラス番号インスタンス初期化
        ClassNum classNum = new ClassNum();
        // データベースコネクション確率
        Connection connection = getConnection();
        // プリペアードステートメントにSQLセット
        PreparedStatement statement = null;
        try {
            // DB処理
            statement = connection
                    .prepareStatement("SELECT * FROM class_num WHERE class_num = ? AND school_cd = ?");
            statement.setString(1, class_num);
            statement.setString(2, school.getCd());
            ResultSet rSet = statement.executeQuery();
            SchoolDao sDao = new SchoolDao();
            if (rSet.next()) {
                // リザルトが存在する場合クラス番号インスタンスに結果をセット
                classNum.setClass_num(rSet.getString("class_num"));
                classNum.setSchool(sDao.get(rSet.getString("school_cd")));
            } else {
                // リザルトが存在しない場合nullセット
                classNum = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            // コネクションを閉じる
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
        return classNum;
    }

    // 学校コード一致のクラスコード取得
    public List<String> filter(School school) throws Exception {
        List<String> list = new ArrayList<>(); // リスト初期化
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            // DB処理
            statement = connection
                    .prepareStatement("SELECT class_num FROM class_num WHERE school_cd = ? ORDER BY class_num");
            statement.setString(1, school.getCd());
            ResultSet rSet = statement.executeQuery();
            while (rSet.next()) {
                list.add(rSet.getString("class_num"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
        return list;
    }

    // クラス情報リストを取得
    public List<ClassNum> filterObjects(School school) throws Exception {
        List<ClassNum> list = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection
                .prepareStatement("SELECT * FROM class_num WHERE school_cd = ? ORDER BY class_num");
            statement.setString(1, school.getCd());
            ResultSet rSet = statement.executeQuery();

            while (rSet.next()) {
                ClassNum classNum = new ClassNum();
                classNum.setClass_num(rSet.getString("class_num"));
                classNum.setSchool(school);
                list.add(classNum);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
        return list;
    }

    // 新規登録処理
    public boolean save(ClassNum classNum) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        // 実行件数
        int count = 0;

        try {
            // 既存のクラスを確認
            ClassNum existing = get(classNum.getClass_num(), classNum.getSchool());

            if (existing == null) {
                // 新規登録
                statement = connection.prepareStatement(
                        "INSERT INTO class_num(school_cd, class_num) values(?, ?)");
                statement.setString(1, classNum.getSchool().getCd());
                statement.setString(2, classNum.getClass_num());
            } else {
                // 更新（このケースはほぼないはずだが念のため）
                statement = connection.prepareStatement(
                        "UPDATE class_num SET school_cd = ? WHERE class_num = ?");
                statement.setString(1, classNum.getSchool().getCd());
                statement.setString(2, classNum.getClass_num());
            }

            count = statement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    // 削除
    public boolean delete(ClassNum classNum) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        // 実行件数
        int count = 0;

        try {
            statement = connection.prepareStatement(
                    "DELETE FROM class_num WHERE school_cd = ? AND class_num = ?");
            statement.setString(1, classNum.getSchool().getCd());
            statement.setString(2, classNum.getClass_num());
            count = statement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    // 変更
    public boolean update(ClassNum classNum) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement1 = null;
        PreparedStatement statement2 = null;
        int count = 0;

        try {
            // トランザクション開始
            connection.setAutoCommit(false);

            // 1. クラス番号がnullでないことを確認
            if (classNum.getClass_num() == null || classNum.getSchool() == null || classNum.getSchool().getCd() == null) {
                throw new Exception("クラス番号または学校コードがnullです");
            }

            // 2. 学生テーブルのclass_num更新
            String updateStudentSql = "UPDATE student SET class_num = ? WHERE school_cd = ? AND class_num = ?";
            statement1 = connection.prepareStatement(updateStudentSql);
            statement1.setString(1, classNum.getClass_num());
            statement1.setString(2, classNum.getSchool().getCd());
            statement1.setString(3, classNum.getOld_class_num());

            int studentUpdateCount = statement1.executeUpdate();
            count += studentUpdateCount;

            // 3. クラステーブルのclass_num更新
            String updateClassNumSql = "UPDATE class_num SET class_num = ? WHERE school_cd = ? AND class_num = ?";
            statement2 = connection.prepareStatement(updateClassNumSql);
            statement2.setString(1, classNum.getClass_num());
            statement2.setString(2, classNum.getSchool().getCd());
            statement2.setString(3, classNum.getOld_class_num());

            int classNumUpdateCount = statement2.executeUpdate();
            count += classNumUpdateCount;

            // 成功した場合のみコミット
            if (count >= 2) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (SQLException e) {
            connection.rollback();
            throw new Exception("データベースエラー: " + e.getMessage(), e);
        } catch (Exception e) {
            connection.rollback();
            throw new Exception("エラー: " + e.getMessage(), e);
        } finally {
            // 資源解放とトランザクション終了
            if (statement1 != null) {
                try {
                    statement1.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (statement2 != null) {
                try {
                    statement2.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);  // 自動コミットに戻す
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
    }
} 