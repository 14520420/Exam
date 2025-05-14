package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ClassNum;
import bean.School;
import bean.Student;

public class StudentDao extends Dao {
    private String baseSql = "select * from student where school_cd=?";

    /**
     * 学校コード、入学年度、クラス番号で学生を検索
     * 引数の型をStringで受け取り、適切に変換して既存のfilterメソッドを呼び出す
     */
    public static List<Student> filter(String schoolCd, String entYearStr, String classNum) throws Exception {
        StudentDao dao = new StudentDao();
        School school = new SchoolDao().get(schoolCd);

        // 学校が見つからない場合は空リストを返す
        if (school == null) {
            return new ArrayList<>();
        }

        boolean isAttend = true; // デフォルトで在学中のみ対象

        if (entYearStr != null && !entYearStr.isEmpty() && classNum != null && !classNum.isEmpty()) {
            // 入学年度とクラス番号が指定されている場合
            try {
                int entYear = Integer.parseInt(entYearStr);
                return dao.filter(school, entYear, classNum, isAttend);
            } catch (NumberFormatException e) {
                // 入学年度が数値でない場合
                throw new Exception("入学年度は数値で指定してください。");
            }
        } else if (entYearStr != null && !entYearStr.isEmpty()) {
            // 入学年度のみ指定されている場合
            try {
                int entYear = Integer.parseInt(entYearStr);
                return dao.filter(school, entYear, isAttend);
            } catch (NumberFormatException e) {
                throw new Exception("入学年度は数値で指定してください。");
            }
        } else {
            // どちらも指定されていない場合
            return dao.filter(school, isAttend);
        }
    }

    // 個人データ取得
    public Student get(String no) throws Exception {
        // インスタンス初期化
        Student student = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM student WHERE no = ? ORDER BY class_num, ent_year asc")) {

            statement.setString(1, no);
            ResultSet rSet = statement.executeQuery();
            SchoolDao schoolDao = new SchoolDao(); // 学校Dao初期化

            if (rSet.next()) {
                // リザルトセットが存在時、検索結果セット
                student = new Student();
                student.setNo(rSet.getString("no"));
                student.setName(rSet.getString("name"));
                student.setEntYear(rSet.getInt("ent_year"));
                student.setClassNum(rSet.getString("class_num"));
                student.setAttend(rSet.getBoolean("is_attend"));
                student.setSchool(schoolDao.get(rSet.getString("school_cd")));
            }
        } catch (SQLException e) {
            throw new Exception("学生情報の取得中にエラーが発生しました: " + e.getMessage());
        }

        return student;
    }

    // filter設定　studentbeanへの登録処理のまとめ
    private List<Student> postFilter(ResultSet rSet, School school) throws Exception {
        List<Student> list = new ArrayList<>();
        try {
            while (rSet.next()) {
                Student student = new Student();
                student.setNo(rSet.getString("no"));
                student.setName(rSet.getString("name"));
                student.setEntYear(rSet.getInt("ent_year"));
                student.setClassNum(rSet.getString("class_num"));
                student.setAttend(rSet.getBoolean("is_attend"));
                student.setSchool(school);
                list.add(student);
            }
        } catch (SQLException | NullPointerException e) {
            throw new Exception("学生データの処理中にエラーが発生しました: " + e.getMessage());
        }
        return list;
    }

    // 入学年度、クラスナンバー一致
    public List<Student> filter(School school, int entYear, String classNum, boolean isAttend) throws Exception {
        List<Student> list = new ArrayList<>();
        String condition = " and ent_year=? and class_num=?";
        String order = " order by class_num, ent_year asc";
        String conditionIsAttend = isAttend ? " and is_attend=true" : "";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(baseSql + condition + conditionIsAttend + order)) {

            statement.setString(1, school.getCd());
            statement.setInt(2, entYear);
            statement.setString(3, classNum);
            ResultSet set = statement.executeQuery();
            list = postFilter(set, school);
        } catch (SQLException e) {
            throw new Exception("学生情報のフィルタリング中にエラーが発生しました: " + e.getMessage());
        }

        return list;
    }

    // 入学年度一致
    public List<Student> filter(School school, int entYear, boolean isAttend) throws Exception {
        List<Student> list = new ArrayList<>();
        String condition = " and ent_year=?";
        String order = " order by class_num, ent_year asc";
        String conditionIsAttend = isAttend ? " and is_attend=true" : "";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(baseSql + condition + conditionIsAttend + order)) {

            statement.setString(1, school.getCd());
            statement.setInt(2, entYear);
            ResultSet rSet = statement.executeQuery();
            list = postFilter(rSet, school);
        } catch (SQLException e) {
            throw new Exception("学生情報のフィルタリング中にエラーが発生しました: " + e.getMessage());
        }

        return list;
    }

    // 在学中のみ
    public List<Student> filter(School school, boolean isAttend) throws Exception {
        List<Student> list = new ArrayList<>();
        String order = " order by class_num, ent_year asc";
        String conditionIsAttend = isAttend ? " and is_attend=true" : "";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(baseSql + conditionIsAttend + order)) {

            statement.setString(1, school.getCd());
            ResultSet rSet = statement.executeQuery();
            list = postFilter(rSet, school);
        } catch (SQLException e) {
            throw new Exception("学生情報のフィルタリング中にエラーが発生しました: " + e.getMessage());
        }

        return list;
    }

    // クラス番号、学校番号
    public List<Student> filter(String classnum, School school) throws Exception {
        List<Student> list = new ArrayList<>();
        String condition = " and class_num = ?";
        String order = " order by ent_year asc, name";
        String conditionIsAttend = " and is_attend=true";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(baseSql + condition + conditionIsAttend + order)) {

            statement.setString(1, school.getCd());
            statement.setString(2, classnum);
            ResultSet rSet = statement.executeQuery();
            list = postFilter(rSet, school);
        } catch (SQLException e) {
            throw new Exception("学生情報のフィルタリング中にエラーが発生しました: " + e.getMessage());
        }

        return list;
    }

    // 更新、新規追加
    public boolean save(Student student) throws Exception {
        boolean result = false;

        try (Connection connection = getConnection()) {
            PreparedStatement statement = null;

            try {
                Student old = get(student.getNo());

                if (old == null) {
                    // 新規追加
                    statement = connection.prepareStatement(
                            "INSERT INTO student(no, name, ent_year, class_num, is_attend, school_cd) values(?, ?, ?, ?, ?, ?)");
                    statement.setString(1, student.getNo());
                    statement.setString(2, student.getName());
                    statement.setInt(3, student.getEntYear());
                    statement.setString(4, student.getClassNum());
                    statement.setBoolean(5, student.isAttend());
                    statement.setString(6, student.getSchool().getCd());
                } else {
                    // 更新
                    statement = connection.prepareStatement(
                            "UPDATE student SET name = ?, ent_year = ?, class_num = ?, is_attend = ? WHERE no = ?");
                    statement.setString(1, student.getName());
                    statement.setInt(2, student.getEntYear());
                    statement.setString(3, student.getClassNum());
                    statement.setBoolean(4, student.isAttend());
                    statement.setString(5, student.getNo());
                }

                int count = statement.executeUpdate();
                result = count > 0;
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException e) {
            throw new Exception("学生情報の保存中にエラーが発生しました: " + e.getMessage());
        }

        return result;
    }

    // postFilterのclassnumバージョン
    private List<ClassNum> cFilter(ResultSet rSet) throws Exception {
        List<ClassNum> list = new ArrayList<>();
        try {
            while (rSet.next()) {
                ClassNum classNum = new ClassNum();
                classNum.setClass_num(rSet.getString("class_num"));
                classNum.setC_count(rSet.getInt("c_count"));
                list.add(classNum);
            }
        } catch (SQLException | NullPointerException e) {
            throw new Exception("クラスデータの処理中にエラーが発生しました: " + e.getMessage());
        }
        return list;
    }

    // クラスごとの学生数付き取得
    public List<ClassNum> class_count(School school) throws Exception {
        List<ClassNum> c_list = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                "SELECT class_num.class_num, Count(student.no) AS c_count FROM class_num LEFT JOIN student " +
                "ON class_num.class_num = student.class_num AND class_num.school_cd = student.school_cd " +
                "WHERE class_num.school_cd=? GROUP BY class_num.class_num ORDER BY class_num.class_num")) {

            statement.setString(1, school.getCd());
            ResultSet rSet = statement.executeQuery();
            c_list = cFilter(rSet);
        } catch (SQLException e) {
            throw new Exception("クラス情報の取得中にエラーが発生しました: " + e.getMessage());
        }

        return c_list;
    }
} 