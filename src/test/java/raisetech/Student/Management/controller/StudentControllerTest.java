package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;


@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean //←MockBeanを書き換えた
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    //when(service.searchStudentList()).thenReturn(List.of(new StudentDetail()));

    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk());
    //     .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();
    //verify：モックされたメソッドが「呼ばれたかどうか」を確認するもの
  }


  @Test
  void 受講生詳細のID検索が実行できて空のリストが返ってくること() throws Exception {

    Student student = new Student();
    student.setId("1");
    student.setName("John Miller");
    student.setFurigana("Jon Mira");
    student.setNickname("Johnny");
    student.setEmailAddress("john.miller@example.com");
    student.setArea("New York");
    student.setAge(20);
    student.setGender("male");
    student.setRemark(null);
    student.setDeleted(false);

    StudentCourse course = new StudentCourse();
    course.setCoursesId("1");
    course.setStudentId("1");
    course.setCourseName("Java Programming Basics");
    course.setStartDate(LocalDateTime.of(2025,4,1,9,0,0));
    course.setScheduledEndDate(LocalDateTime.of(2025,7,1,9,0,0));

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(List.of(course));

    when(service.searchStudent("1")).thenReturn(studentDetail);

    mockMvc.perform(get("/student/{id}", 1))
        .andExpect(status().isOk())
        .andExpect(content().json("""
        {
          "student": {
            "id": "1",
            "name": "John Miller",
            "furigana": "Jon Mira",
            "nickname": "Johnny",
            "emailAddress": "john.miller@example.com",
            "area": "New York",
            "age": 20,
            "gender": "male",
            "remark": null,
            "deleted": false
          },
          "studentCourseList": [
            {
              "coursesId": "1",
              "studentId": "1",
              "courseName": "Java Programming Basics",
              "startDate": "2025-04-01T09:00:00",
              "scheduledEndDate": "2025-07-01T09:00:00"
            }
          ]
        }
        """));

    verify(service, times(1)).searchStudent("1");
  }


  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setId("1");
    student.setName("山田花子");
    student.setFurigana("ヤマダハナコ");
    student.setNickname("はなこ");
    student.setEmailAddress("yamahana@example.com");
    student.setArea("東京");
    student.setGender("female");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
    //assert：メソッドの「結果（戻り値や状態）」を確認するもの
  }


  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックがかかること() {
    Student student = new Student();
    student.setId("テストです。");
    student.setName("山田花子");
    student.setFurigana("ヤマダハナコ");
    student.setNickname("はなこ");
    student.setEmailAddress("yamahana@example.com");
    student.setArea("東京");
    student.setGender("female");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);
    //↑ SetはListのように複数の情報を持っている

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力するようにしてください。");
  }

  @Test

}