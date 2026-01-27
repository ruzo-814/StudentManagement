package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.controller.request.StudentSearchCondition;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.CourseStatus.CourseStatusType;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    StudentSearchCondition condition = new StudentSearchCondition();

    List<Student> expected = List.of(
        new Student("1", "John Miller", "Jon Mira", "Johnny",
            "john.miller@example.com", "New York", 20, "male", null, false),
        new Student("2", "Emily Johnson", "Emiri Jonson", "Em",
            "emily.johnson@example.com", "Los Angeles", 19, "female", null, false),
        new Student("3", "Michael Brown", "Maikeru Buraun", "Mike",
            "michael.brown@example.com", "Chicago", 22, "male", null, false),
        new Student("4", "Sophia Davis", "Sofia Deibisu", "Sophie",
            "sophia.davis@example.com", "Houston", 18, "female", null, false),
        new Student("5", "Alex Smith", "Arekkusu Sumisu", "Alex",
            "alex.smith@example.com", "Seattle", 21, "other", null, false)
    );

    List<Student> actual = sut.searchByCondition(condition);

    assertThat(actual.size()).isEqualTo(5);
    assertThat(actual).isEqualTo(expected);
  }


  @Test
  void 受講生コース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(5);
  }


  @Test
  void 受講生のID検索が行えること() {
    Student actual = sut.searchStudent("5");

    assertThat(actual.getId()).isEqualTo("5");
  }


  @Test
  void 受講生IDに紐づくコース情報が検索できること() {
    List<StudentCourse> actual = sut.searchStudentCourse("3");

    assertThat(actual.get(0).getStudentId()).isEqualTo("3");
  }


  @Test
  void コース申込状況の全件検索ができること() {
    List<CourseStatus> actual = sut.searchCourseStatus();

    assertThat(actual.size()).isEqualTo(5);
  }


  @Test
  void コース申込状況のID検索ができること() {
    List<CourseStatus> actual = sut.searchCourseStatusByCourseId("5");

    assertThat(actual).hasSize(1);
    assertThat(actual.get(0).getStatus())
        .isEqualTo(CourseStatusType.TEMPORARY);
  }


  @Test
  void 受講生の登録が行えること() {
    Student student = testStudent();
    StudentSearchCondition condition = new StudentSearchCondition();

    sut.insertStudent(student);

    List<Student> actual = sut.searchByCondition(condition);

    assertThat(actual.size()).isEqualTo(6);
  }


  @Test
  void 受講生のコース情報が登録できること() {
    StudentCourse studentCourse = testStudentCourse();

    sut.insertStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(6);
  }


  @Test
  void 受講生情報を更新できること() {
    Student updateStudent = testStudent();
    updateStudent.setId("2");
    updateStudent.setName("田中桃子");
    updateStudent.setFurigana("タナカモモコ");
    updateStudent.setNickname("モモコ");
    updateStudent.setEmailAddress("tanamomo@example.com");

    sut.updateStudent(updateStudent);
    Student actual = sut.searchStudent("2");

    assertThat(actual).isEqualTo(updateStudent);
  }


  @Test
  void 受講生のコース情報を更新できること() {
    StudentCourse studentCourse = testStudentCourse();
    studentCourse.setCoursesId("5");
    studentCourse.setCourseName("Javaエンジニアスタンダードコース");

    sut.updateStudentCourse(studentCourse);
    List<StudentCourse> actual = sut.searchStudentCourse("5");

    assertThat(actual.get(0).getCourseName()).isEqualTo("Javaエンジニアスタンダードコース");
  }


  private static Student testStudent() {
    Student student = new Student();
    student.setName("山田花子");
    student.setFurigana("ヤマダハナコ");
    student.setNickname("はなこ");
    student.setEmailAddress("yamahana@example.com");
    student.setArea("東京");
    student.setAge(20);
    student.setGender("female");
    student.setRemark("");
    student.setDeleted(false);
    return student;
  }


  private static StudentCourse testStudentCourse() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("5");
    studentCourse.setCourseName("Java Programming Basics");
    studentCourse.setStartDate(LocalDateTime.of(2025, 4, 1, 9, 0, 0));
    studentCourse.setScheduledEndDate(LocalDateTime.of(2025, 7, 1, 9, 0, 0));
    return studentCourse;
  }
}