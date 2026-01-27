package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.student.management.controller.request.StudentSearchCondition;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

/**
 *受講生テーブルと受講生コース情報を紐づけるRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索（条件任意）を行います
   * @param condition 検索条件
   * @return 受講生
   */
  List<Student> searchByCondition(StudentSearchCondition condition);


  /**
   * 受講生コース情報の全件検索を行います。
   *
   * @return 受講生コース情報一覧（全件）
   */
  List<StudentCourse> searchStudentCourseList();


  /**
   * 受講生のID検索を行います。
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
   * 受講コースIDに紐づく申し込み状況の検索を行います。
   *
   * @param coursesId 受講コースID
   * @return コース申し込み状況
   */
  List<CourseStatus> searchCourseStatusByCourseId(String coursesId);


  /**
   * コースの申し込み状況の全件検索を行います。
   *
   * @return コース申し込み状況
   */
  List<CourseStatus> searchCourseStatus();


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
   * 受講コース情報の新規登録を行います。
   *
   * @param courseStatus コース申し込み状況
   */
  void insertCourseStatus(CourseStatus courseStatus);


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


  /**
   * コース申し込み状況を更新します。
   *
   * @param coursesId 受講コースID
   * @param status 申し込み状況
   */
  void updateCourseStatus(String coursesId, CourseStatus.CourseStatusType status);
}

//複雑な処理はできない
//複雑なSQLを書くことはできる
//リポジトリはDBにアクセスしてSQLを発行するだけ、それ以前にやって腰いことがあるならServiceに書く
//Javaの世界で扱えるDBみたいな場所