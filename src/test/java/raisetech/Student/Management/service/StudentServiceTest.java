package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.controller.request.StudentSearchCondition;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;


@ExtendWith(MockitoExtension.class)
class StudentServiceTest {


  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_検索条件が空の場合にリポジトリとコンバーターの処理が適切に呼び出せていること() {
    //事前準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentSearchCondition condition = new StudentSearchCondition();

    when(repository.searchByCondition(condition)).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    //実行
    sut.searchStudentList(condition);

    //検証
    verify(repository, times(1)).searchByCondition(condition);
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);

    //後処理
    //例）ここでDBを元に戻る
  }


  @Test
  void 受講生の一覧検索_検索条件を指定した場合にリポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setName("test");

    when(repository.searchByCondition(condition)).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.searchStudentList(condition);

    verify(repository).searchByCondition(condition);
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }


    @Test
    void 受講生詳細のID検索_リポジトリとコンバーターの処理が適切に呼び出せていること () {
      //事前準備
      String id = "14";
      Student student = new Student();
      student.setId(id);
      List<StudentCourse> studentCourseList = new ArrayList<>();

      when(repository.searchStudent(id)).thenReturn(student);
      when(repository.searchStudentCourse(id)).thenReturn(studentCourseList);

      StudentDetail expected = new StudentDetail(student, studentCourseList);//expected = 期待する結果

      //実行
      StudentDetail actual = sut.searchStudent(id);//actual = 実際の結果

      //検証
      verify(repository, times(1)).searchStudent(id);
      verify(repository, times(1)).searchStudentCourse(id);

      assertEquals(expected.getStudent().getId(), actual.getStudent().getId());

      //後処理
    }


  @Test
  void コース申込状況検索_coursesId指定の場合はID検索が呼ばれること() {
    when(repository.searchCourseStatusByCourseId("5")).thenReturn(List.of());

    sut.searchCourseStatus("5");

    verify(repository, times(1)).searchCourseStatusByCourseId("5");
    verify(repository, times(0)).searchCourseStatus();
  }


  @Test
  void コース申込状況検索_coursesIdがnullの場合は全件検索が呼ばれること() {
    when(repository.searchCourseStatus()).thenReturn(List.of());

    sut.searchCourseStatus(null);

    verify(repository, times(1)).searchCourseStatus();
    verify(repository, times(0)).searchCourseStatusByCourseId(any());
  }


  @Test
  void コース状況が仮申込から本申込に更新できること() {
    CourseStatus status = new CourseStatus();
    status.setStatus(CourseStatus.CourseStatusType.TEMPORARY);

    when(repository.searchCourseStatusByCourseId("1")).thenReturn(List.of(status));

    sut.updateCourseStatus("1", CourseStatus.CourseStatusType.APPLIED);

    verify(repository).updateCourseStatus("1", CourseStatus.CourseStatusType.APPLIED);
  }


  @Test
  void コース状況が本申し込みから受講中に更新できること() {
    CourseStatus status = new CourseStatus();
    status.setStatus(CourseStatus.CourseStatusType.APPLIED);

    when(repository.searchCourseStatusByCourseId("2")).thenReturn(List.of(status));

    sut.updateCourseStatus("2", CourseStatus.CourseStatusType.IN_PROGRESS);

    verify(repository).updateCourseStatus("2", CourseStatus.CourseStatusType.IN_PROGRESS);
  }


  @Test
  void コース状況が受講中から受講完了に更新できること() {
    CourseStatus status = new CourseStatus();
    status.setStatus(CourseStatus.CourseStatusType.IN_PROGRESS);

    when(repository.searchCourseStatusByCourseId("3")).thenReturn(List.of(status));

    sut.updateCourseStatus("3", CourseStatus.CourseStatusType.COMPLETED);

    verify(repository).updateCourseStatus("3", CourseStatus.CourseStatusType.COMPLETED);
  }


  @Test
  void コース状況の変遷が正しくない場合に例外が発生すること() {
    CourseStatus status = new CourseStatus();
    status.setStatus(CourseStatus.CourseStatusType.TEMPORARY);

    when(repository.searchCourseStatusByCourseId("1")).thenReturn(List.of(status));

    Assertions.assertThrows(IllegalStateException.class,
        () -> sut.updateCourseStatus("1", CourseStatus.CourseStatusType.COMPLETED));
  }


  @Test
  void コース状況IDが存在しない場合に例外が発生すること() {
    when(repository.searchCourseStatusByCourseId("999")).thenReturn(List.of());

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> sut.updateCourseStatus("999", CourseStatus.CourseStatusType.APPLIED));
  }


  @Test
    void 新規受講生登録_リポジトリの処理が適切に呼び出せていること () {
      //事前準備
      Student student = new Student();
      StudentCourse studentCourse = new StudentCourse();
      List<StudentCourse> studentCourseList = List.of(studentCourse);
      StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

      //実行
      sut.registerStudent(studentDetail);

      //検証
      verify(repository, times(1)).insertStudent(student);
      verify(repository, times(1)).insertStudentCourse(studentCourse);

      //後処理
    }

    @Test
    void 受講生詳細の登録_初期化処理が行われること () {
      String id = "999";
      Student student = new Student();
      student.setId(id);
      StudentCourse studentCourse = new StudentCourse();

      sut.initStudentsCourse(studentCourse, student.getId());

      Assertions.assertEquals(id, studentCourse.getStudentId());
      Assertions.assertEquals(LocalDateTime.now().getHour(),
          studentCourse.getStartDate().getHour());
      Assertions.assertEquals(LocalDateTime.now().plusYears(1).getYear(),
          studentCourse.getScheduledEndDate().getYear());
    }

    @Test
    void 受講生情報の更新_リポジトリの処理が適切に呼び出せていること () {
      //事前準備
      Student student = new Student();
      StudentCourse studentCourse = new StudentCourse();
      List<StudentCourse> studentCourseList = List.of(studentCourse);
      StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

      //実行
      sut.updateStudent(studentDetail);

      //検証
      verify(repository, times(1)).updateStudent(student);
      verify(repository, times(1)).updateStudentCourse(studentCourse);

      //後処理
    }

  }