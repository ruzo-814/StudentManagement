package raisetech.Student.Management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.data.StudentCourses;

/**
 *受講生テーブルと受講生コース情報を紐づけるRepositoryです。
 */
@Mapper
public interface StudentRepository {


  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧（全件）
   */
  @Select("SELECT * FROM students WHERE is_deleted = false")
  List<Student> search();


  /**
   * 受講生コース情報の全件検索を行います。
   *
   * @return 受講生コース情報一覧（全件）
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourses> searchStundetsCoursesList();


  /**
   * 受講生の検索を行います。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudent(String id);


  /**
   * 受講生IDに紐づくコース情報の検索を行います。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourses> searchStudentsCourses(String studentId);


  /**
   * 受講生の登録を行います。
   *
   * @param student 受講生
   */
  @Insert("INSERT INTO students (name, furigana, nickname, email_address, area, age, gender, remark, is_deleted)" +
      "VALUES (#{name}, #{furigana}, #{nickname}, #{emailAddress}, #{area}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStudent(Student student);


  /**
   * 受講コース情報の登録を行います。
   *
   * @param studentCourses 受講コース情報
   */
  @Insert("INSERT INTO students_courses (student_id, course_name, start_date, scheduled_end_date)" +
      "VALUES (#{studentId}, #{courseName}, #{startDate}, #{scheduledEndDate})")
  @Options(useGeneratedKeys = true, keyProperty = "coursesId")
  void insertStudentCourses(StudentCourses studentCourses);


  /**
   * 受講生情報の更新を行います。
   *
   * @param student 受講生
   */
  @Update("UPDATE students SET name = #{name}, furigana = #{furigana}, nickname = #{nickname}, email_address = #{emailAddress}, "
      + "area = #{area}, age = #{age}, gender = #{gender}, remark = #{remark}, is_deleted = #{deleted} WHERE id = #{id}")
  void updateStudent(Student student);


  /**
   * 受講生コース情報の更新を行います。
   *
   * @param studentCourses 受講コース情報
   */
  @Update("UPDATE students_courses SET course_name = #{courseName} WHERE courses_ID = #{coursesId}")
  void updateStudentCourses(StudentCourses studentCourses);
}

//複雑な処理はできない
//複雑なSQLを書くことはできる
//リポジトリはDBにアクセスしてSQLを発行するだけ、それ以前にやって腰いことがあるならServiceに書く
//Javaの世界で扱えるDBみたいな場所