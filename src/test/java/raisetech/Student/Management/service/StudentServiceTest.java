package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
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
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    //事前準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();

    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    //実行
    sut.searchStudentList();

    //検証
    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);

    //後処理
    //例）ここでDBを元に戻る
  }


  @Test
  void 受講生詳細のID検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
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
  void 新規受講生登録_リポジトリの処理が適切に呼び出せていること() {
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
  void 受講生情報の更新_リポジトリの処理が適切に呼び出せていること() {
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