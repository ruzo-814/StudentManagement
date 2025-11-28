package raisetech.student.management.service;


import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;


/**
 * 受講生情報を取り扱うServiceです。
 * 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }


  /**
   * 受講生詳細一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    return converter.convertStudentDetails(studentList, studentCourseList);
  }


  /**
   * 受講コース情報一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講コース情報一覧（全件）
   */
  public List<StudentCourse> searchStudentCousesList() {
    return repository.searchStudentCourseList();
  }


  /**
   * 受講生詳細検索です。
   * IDに紐づく受講生の情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id
   * @return
   */
  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);
    List<StudentCourse> studentCourse = repository.searchStudentCourse(student.getId());
    return new StudentDetail(student, studentCourse);
  }


  /**
   * 新規受講生登録です。
   * 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値と日付情報（コース開始日、コース終了日）を設定します。
   *
   * @param studentDetail 受講生詳細情報
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    repository.insertStudent(student);
    studentDetail.getStudentCourseList().forEach(studentCourse -> {
      initStudentsCourse(studentCourse, student.getId());
      repository.insertStudentCourse(studentCourse);
    });
    return studentDetail;
  }


  /**
   * 受講生コース情報を登録する際の初期情報を設定します。
   *
   * @param studentCourse 受講生コース情報
   * @param id 受講生情報
   */
  void initStudentsCourse(StudentCourse studentCourse, String id) {
    LocalDateTime now = LocalDateTime.now();

    studentCourse.setStudentId(id);
    studentCourse.setStartDate(now);
    studentCourse.setScheduledEndDate(now.plusYears(1));
  }


  /**
   * 受講生情報更新です。
   * 受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細情報
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    studentDetail.getStudentCourseList()
        .forEach(studentCourse -> repository.updateStudentCourse(studentCourse));
  }
}
