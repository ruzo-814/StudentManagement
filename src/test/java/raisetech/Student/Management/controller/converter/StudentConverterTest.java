package raisetech.student.management.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;


class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生リストとコース情報リストが紐づけられ受講生詳細リストとなっていること() {
    //事前準備
    Student student1 = getStudent1();

    Student student2 = getStudent2();

    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setCoursesId("1");
    studentCourse1.setStudentId("1");
    studentCourse1.setCourseName("Java Programming Basics");
    studentCourse1.setStartDate(LocalDateTime.of(2025, 4, 1, 9, 0, 0));
    studentCourse1.setScheduledEndDate(LocalDateTime.of(2025, 7, 1, 9, 0, 0));

    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setCoursesId("2");
    studentCourse2.setStudentId("2");
    studentCourse2.setCourseName("Web Development with HTML");
    studentCourse2.setStartDate(LocalDateTime.of(2025, 4, 1, 9, 0, 0));
    studentCourse2.setScheduledEndDate(LocalDateTime.of(2025, 7, 1, 9, 0, 0));

    List<Student> studentList = new ArrayList<>();
    studentList.add(student1);
    studentList.add(student2);

    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(studentCourse1);
    studentCourseList.add(studentCourse2);

    //実行
    List<StudentDetail> actual = sut.convertStudentDetails(studentList,
        studentCourseList);

    //検証
    assertEquals(studentList.size(), actual.size());
  }


  @Test
  void コース情報が存在しない受講生であっても受講生詳細が作られること() {
    //事前準備
    Student student1 = getStudent1();

    Student student2 = getStudent2();

    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setCoursesId("1");
    studentCourse1.setStudentId("1");
    studentCourse1.setCourseName("Java Programming Basics");
    studentCourse1.setStartDate(LocalDateTime.of(2025, 4, 1, 9, 0, 0));
    studentCourse1.setScheduledEndDate(LocalDateTime.of(2025, 7, 1, 9, 0, 0));

    List<Student> studentList = new ArrayList<>();
    studentList.add(student1);
    studentList.add(student2);

    List<StudentCourse> studentCourseList = List.of(studentCourse1);

    //実行
    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    //検証
    assertEquals(studentList.size(), actual.size());

    StudentDetail actual1 = actual.get(0);
    StudentDetail actual2 = actual.get(1);

    assertThat(actual1.getStudent()).isEqualTo(student1);
    assertThat(actual2.getStudent()).isEqualTo(student2);

  }

  private static Student getStudent2() {
    Student student2 = new Student();
    student2.setId("2");
    student2.setName("Emily Johnson");
    student2.setFurigana("Emiri Jonson");
    student2.setNickname("Emi");
    student2.setEmailAddress("emily.johnson@example.com");
    student2.setArea("Los Angeles");
    student2.setAge(19);
    student2.setGender("female");
    student2.setRemark(null);
    student2.setDeleted(false);
    return student2;
  }

  private static Student getStudent1() {
    Student student1 = new Student();
    student1.setId("1");
    student1.setName("John Miller");
    student1.setFurigana("Jon Mira");
    student1.setNickname("Johnny");
    student1.setEmailAddress("john.miller@example.com");
    student1.setArea("New York");
    student1.setAge(20);
    student1.setGender("male");
    student1.setRemark(null);
    student1.setDeleted(false);
    return student1;
  }

}