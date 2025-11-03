package raisetech.Student.Management.service;


import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.Student.Management.controller.converter.StudentConverter;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.data.StudentCourses;
import raisetech.Student.Management.domain.StudentDetail;
import raisetech.Student.Management.repository.StudentRepository;

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
   * 受講生一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourses> studentCoursesList = repository.searchStundetsCoursesList();
    return converter.convertStudentDetails(studentList, studentCoursesList);
  }


  /**
   * 受講コース情報一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講コース情報一覧（全件）
   */
  public List<StudentCourses> searchStudentCousesList() {
    return repository.searchStundetsCoursesList();
  }


  /**
   * 受講生検索です。
   * IDに紐づく受講生の情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id
   * @return
   */
  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);
    List<StudentCourses> studentCourses = repository.searchStudentsCourses(student.getId());
    return new StudentDetail(student, studentCourses);
  }


  /**
   * 新規受講生登録です。
   *
   * @param studentDetail 受講生詳細情報
   * @return 新規受講生登録
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    repository.insertStudent(studentDetail.getStudent());
    for (StudentCourses studentCourses : studentDetail.getStudentCourses()) {
      studentCourses.setStudentId(studentDetail.getStudent().getId());
      studentCourses.setStartDate(LocalDateTime.now());
      studentCourses.setScheduledEndDate(LocalDateTime.now().plusYears(1));
      repository.insertStudentCourses(studentCourses);
    }
    return studentDetail;
  }


  /**
   * 受講生情報更新です
   *
   * @param studentDetail 受講生詳細情報
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourses studentCourses : studentDetail.getStudentCourses()) {
      repository.updateStudentCourses(studentCourses);
    }
  }
}
