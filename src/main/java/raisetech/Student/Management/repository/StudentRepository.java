package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

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
  List<Student> search();


  /**
   * 受講生コース情報の全件検索を行います。
   *
   * @return 受講生コース情報一覧（全件）
   */
  List<StudentCourse> searchStundetCourseList();


  /**
   * 受講生の検索を行います。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  Student searchStudent(String id);


  /**
   * 受講生IDに紐づくコース情報の検索を行います。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourse(String studentId);


  /**
   * 受講生の新規登録を行います。
   * IDに関しては自動採番を行います。
   *
   * @param student 受講生
   */
  void insertStudent(Student student);


  /**
   * 受講コース情報の新規登録を行います。
   *
   * @param studentCourse 受講コース情報
   */
  void insertStudentCourse(StudentCourse studentCourse);


  /**
   * 受講生情報の更新を行います。
   *
   * @param student 受講生
   */
  void updateStudent(Student student);


  /**
   * 受講生コース情報のコース名の更新を行います。
   *
   * @param studentCourse 受講コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);
}

//複雑な処理はできない
//複雑なSQLを書くことはできる
//リポジトリはDBにアクセスしてSQLを発行するだけ、それ以前にやって腰いことがあるならServiceに書く
//Javaの世界で扱えるDBみたいな場所