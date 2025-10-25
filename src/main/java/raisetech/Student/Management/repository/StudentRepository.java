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
 *受講生情報を扱うリポジトリ。
 *
 * 全件検索や単一情報での検索、コース情報の検索が行えるクラスです
 */

@Mapper
public interface StudentRepository {


  /**
   * 全件検索します。
   *
   * @return 全件検索した受講生情報の一覧
   */

  @Select("SELECT * FROM students WHERE is_deleted = false")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentCourses> searchStundetsCoursesList();

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudent(String id);

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourses> searchStudentsCourses(String studentId);

  @Insert("INSERT INTO students (name, furigana, nickname, email_address, area, age, gender, remark, is_deleted)" +
      "VALUES (#{name}, #{furigana}, #{nickname}, #{emailAddress}, #{area}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStudent(Student student);

  @Insert("INSERT INTO students_courses (student_id, course_name, start_date, scheduled_end_date)" +
      "VALUES (#{studentId}, #{courseName}, #{startDate}, #{scheduledEndDate})")
  @Options(useGeneratedKeys = true, keyProperty = "coursesId")
  void insertStudentCourses(StudentCourses studentCourses);

  @Update("UPDATE students SET name = #{name}, furigana = #{furigana}, nickname = #{nickname}, email_address = #{emailAddress}, "
      + "area = #{area}, age = #{age}, gender = #{gender}, remark = #{remark}, is_deleted = #{deleted} WHERE id = #{id}")
  void updateStudent(Student student);

  @Update("UPDATE students_courses SET course_name = #{courseName} WHERE courses_ID = #{coursesId}")
  void updateStudentCourses(StudentCourses studentCourses);
}

//複雑な処理はできない
//複雑なSQLを書くことはできる
//リポジトリはDBにアクセスしてSQLを発行するだけ、それ以前にやって腰いことがあるならServiceに書く
//Javaの世界で扱えるDBみたいな場所