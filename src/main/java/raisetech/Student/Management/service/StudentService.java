package raisetech.student.management.service;


import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.controller.request.StudentSearchCondition;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.CourseStatus.CourseStatusType;
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
   * 条件が空の場合、全件検索を行います
   *
   * @return 受講生一覧（全件）
   */
  public List<StudentDetail> searchStudentList(StudentSearchCondition condition) {
      List<Student> studentList = repository.searchByCondition(condition);
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
   * 受講生IDに紐づく受講生の情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id　受講生ID
   * @return
   */
  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);
    List<StudentCourse> studentCourse = repository.searchStudentCourse(student.getId());
    return new StudentDetail(student, studentCourse);
  }


  /**
   * コースの申し込み状況検索です。
   * コースIDに紐づくコース情報を取得します。
   *
   * @param coursesId 受講コースID
   * @return コース申し込み状況
   */
  public List<CourseStatus> searchCourseStatus(String coursesId) {
    if (coursesId == null) {
      return repository.searchCourseStatus();
    } else {
      return repository.searchCourseStatusByCourseId(coursesId);
    }
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

      initCourseStatus(studentCourse);
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
   * コース申し込み状況を初期設定で仮申し込みに設定します。
   *
   * @param studentCourse 受講生コース情報
   */
  void initCourseStatus(StudentCourse studentCourse){
    CourseStatus courseStatus = new CourseStatus();
    courseStatus.setCoursesId(studentCourse.getCoursesId());
    courseStatus.setStatus(CourseStatusType.TEMPORARY);
    repository.insertCourseStatus(courseStatus);
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


  /**
   * コース状況更新です。
   * コース申し込み状況を更新します。
   *
   * @param coursesId コースID
   */
  @Transactional
  public void updateCourseStatus(String coursesId, CourseStatusType nextStatus) {
    List<CourseStatus> courseStatusList = repository.searchCourseStatusByCourseId(coursesId);
    if (courseStatusList.isEmpty()) {
      throw new IllegalArgumentException("指定したコースが存在しません。");
    }
    CourseStatus current = courseStatusList.getFirst();

    if (!isValidTransition(current.getStatus(), nextStatus)) {
      throw new IllegalStateException(
          createInvalidStatusMessage(nextStatus));
    }
    repository.updateCourseStatus(coursesId, nextStatus);
  }


  /**
   *
   * @param current 現在のコース状況
   * @param next 更新したいコース状況
   * @return コース状況の変遷
   */
  private boolean isValidTransition(
      CourseStatusType current,
      CourseStatusType next) {

    return switch (current) {
      case TEMPORARY -> next == CourseStatusType.APPLIED;
      case APPLIED -> next == CourseStatusType.IN_PROGRESS;
      case IN_PROGRESS -> next == CourseStatusType.COMPLETED;
      case COMPLETED -> false;
    };
  }


  /**
   * コース申し込み状況更新の際のエラーメッセージです。
   *
   * @param nextStatus 次のコース申し込み状況
   * @return エラーメッセージ
   */
  private String createInvalidStatusMessage(CourseStatusType nextStatus) {
    return switch (nextStatus) {
      case APPLIED -> "仮申し込み状態ではありません。";
      case IN_PROGRESS -> "本申し込み状態ではありません。";
      case COMPLETED -> "受講中ではありません。";
      default -> "不正な状態です。";
    };
  }
}
