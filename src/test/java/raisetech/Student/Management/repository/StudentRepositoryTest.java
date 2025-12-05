package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(5);
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
  void 受講生の登録が行えること() {
    Student student = testStudent();

    sut.insertStudent(student);

    List<Student> actual = sut.search();

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
    Student student = testStudent();
    student.setId("2");
    student.setName("田中桃子");
    student.setFurigana("タナカモモコ");
    student.setNickname("モモコ");
    student.setEmailAddress("tanamomo@example.com");

    sut.updateStudent(student);
    Student actual = sut.searchStudent("2");

    assertThat(actual.getName()).isEqualTo("田中桃子");
    assertThat(actual.getFurigana()).isEqualTo("タナカモモコ");
    assertThat(actual.getNickname()).isEqualTo("モモコ");
    assertThat(actual.getEmailAddress()).isEqualTo("tanamomo@example.com");
  }


  @Test
  void 受講生のコース情報を更新できること(){
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