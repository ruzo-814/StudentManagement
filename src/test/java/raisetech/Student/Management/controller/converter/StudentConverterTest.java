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
    Student student1 = generateStudent("1", "John Miller", "Jon Mira", "Johnny",
        "john.miller@example.com", "New York", 20, "male", null, false);

    Student student2 = generateStudent("2", "Emily Johnson", "Emiri Jonson", "Emi",
        "emily.johnson@example.com", "Los Angeles", 19, "female", null, false);

    StudentCourse studentCourse1 = generateStudentCourse("1", "1",
        "Java Programming Basics",
        LocalDateTime.of(2025, 4, 1, 9, 0, 0),
        LocalDateTime.of(2025, 7, 1, 9, 0, 0));


    StudentCourse studentCourse2 = generateStudentCourse("2", "2",
        "Web Development with HTML",
        LocalDateTime.of(2025, 4, 1, 9, 0, 0),
        LocalDateTime.of(2025, 7, 1, 9, 0, 0));

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
    Student student1 = generateStudent("1", "John Miller", "Jon Mira", "Johnny",
        "john.miller@example.com", "New York", 20, "male", null, false);

    Student student2 = generateStudent("2", "Emily Johnson", "Emiri Jonson", "Emi",
        "emily.johnson@example.com", "Los Angeles", 19, "female", null, false);

    StudentCourse studentCourse1 = generateStudentCourse("1", "1",
        "Java Programming Basics",
        LocalDateTime.of(2025, 4, 1, 9, 0, 0),
        LocalDateTime.of(2025, 7, 1, 9, 0, 0));

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


  private static Student generateStudent(
      String id,
      String name,
      String furigana,
      String nickname,
      String emailAddress,
      String area,
      int age,
      String gender,
      String remark,
      boolean isDeleted
  ) {
    return new Student(
        id,
        name,
        furigana,
        nickname,
        emailAddress,
        area,
        age,
        gender,
        remark,
        isDeleted
    );
  }

  private static StudentCourse generateStudentCourse(
      String coursesId,
      String studentId,
      String courseName,
      LocalDateTime startDate,
      LocalDateTime scheduledEndDate
  ) {
    return new StudentCourse(
        coursesId,
        studentId,
        courseName,
        startDate,
        scheduledEndDate
    );
  }
}