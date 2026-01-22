package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.data.CourseStatus.CourseStatusType;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;


@WebMvcTest(StudentController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
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

    verify(service, times(1)).searchStudentList(any());
    //verify：モックされたメソッドが「呼ばれたかどうか」を確認するもの
  }


  @Test
  void 受講生詳細のID検索が実行できてID検索した受講生詳細が返ってくること() throws Exception {
    String id = "999";
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }


  @Test
  void コース情報の一覧検索ができて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/studentCoursesList"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentCousesList();
  }


  @Test
  void コース申込状況のID検索が実行できてID検索したコース申し込み状況が返ってくること()
      throws Exception {
    mockMvc.perform(get("/CourseStatus")
            .param("coursesId", "1"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchCourseStatus("1");
  }


  @Test
  void コース申込状況の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/CourseStatus"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchCourseStatus(null);
  }


  @Test
  void 受講生詳細に新規受講生を登録できること() throws Exception {
    mockMvc.perform(post("/registerStudent")
            .contentType("application/json")
            .content("""
                {"student": {
                "name": "鈴木　健司",
                "furigana": "スズキ　ケンジ",
                "nickname": "スズケン",
                "emailAddress": "suzu.ken@example.com",
                "area": "大阪",
                "age": 23,
                "gender": "male",
                "remark": null,
                "deleted": false },
                "studentCourseList": [{
                "courseName": "Java Programming Basics"}]
                }
                """))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }


  @Test
  void 受講生詳細の情報を更新できること() throws Exception {
    mockMvc.perform(put("/updateStudent")
            .contentType("application/json")
            .content("""
                {"student": {
                "id": 22,
                "name": "鈴木　健司",
                "furigana": "スズキ　ケンジ",
                "nickname": "スズケン",
                "emailAddress": "suzu.ken@example.com",
                "area": "大阪",
                "age": 23,
                "gender": "male",
                "remark": null,
                "deleted": false },
                "studentCourseList": [{
                "coursesId": 24,
                "studentId": 22,
                "courseName": "Java Programming Basics",
                "startDate": "2025-04-01T09:00:00",
                "scheduledEndDate": "2025-07-01T09:00:00" }]
                }
                """))
        .andExpect(status().isOk())
        .andExpect(content().string("更新処理が成功しました。"));

    verify(service, times(1)).updateStudent(any(StudentDetail.class));
  }


  @Test
  void コース申し込み状況を仮申込から本申し込みに更新できること() throws Exception {
    mockMvc.perform(patch("/CourseStatus/{coursesId}/apply", "1"))
        .andExpect(status().isOk())
        .andExpect(content().string("本登録が完了しました。"));

    verify(service, times(1))
        .updateCourseStatus("1", CourseStatusType.APPLIED);
  }


  @Test
  void コース申し込み状況を本申し込みから受講中に更新できること() throws Exception {
    mockMvc.perform(patch("/CourseStatus/{coursesId}/inProgress", "2"))
        .andExpect(status().isOk())
        .andExpect(content().string("受講開始しました。"));

    verify(service, times(1))
        .updateCourseStatus("2", CourseStatusType.IN_PROGRESS);
  }


  @Test
  void コース申し込み状況を受講中から受講完了に更新できること() throws Exception {
    mockMvc.perform(patch("/CourseStatus/{coursesId}/completed", "3"))
        .andExpect(status().isOk())
        .andExpect(content().string("受講終了しました。"));

    verify(service, times(1))
        .updateCourseStatus("3", CourseStatusType.COMPLETED);
  }


  @Test
  void 受講生詳細の例外APIが実行できて400エラーが返ってくること() throws Exception {
    mockMvc.perform(get("/testSearchException"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string(
            "現在このAPIは利用できません。URLは「studentList」ではなく「students」を利用してください。"));
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
  void 受講生詳細の受講生で性別に指定された文字以外を用いた時に入力チェックがかかること() {
    Student student = new Student();
    student.setId("1");
    student.setName("山田花子");
    student.setFurigana("ヤマダハナコ");
    student.setNickname("はなこ");
    student.setEmailAddress("yamahana@example.com");
    student.setArea("東京");
    student.setGender("女");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("male/female/otherの中から入力してください。");
  }


  @Test
  void 受講生詳細の受講生でメールアドレスの欄にURLが入力されていた時に入力チェックがかかること() {
    Student student = new Student();
    student.setId("1");
    student.setName("山田花子");
    student.setFurigana("ヤマダハナコ");
    student.setNickname("はなこ");
    student.setEmailAddress("https://example.com");
    student.setArea("東京");
    student.setGender("female");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("メールアドレスを入力してください。");
  }


  @Test
  void 受講生詳細のコース情報で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCoursesId("1");
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("Java Programming Basics");
    studentCourse.setStartDate(LocalDateTime.of(2025, 4, 1, 9, 0, 0));
    studentCourse.setScheduledEndDate(LocalDateTime.of(2025, 7, 1, 9, 0, 0));

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations.size()).isEqualTo(0);
  }


  @Test
  void 受講生詳細のコース情報でIDに数字以外を用いた時に入力チェックがかかること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCoursesId("テストです");
    studentCourse.setStudentId("testです");
    studentCourse.setCourseName("Java Programming Basics");
    studentCourse.setStartDate(LocalDateTime.of(2025, 4, 1, 9, 0, 0));
    studentCourse.setScheduledEndDate(LocalDateTime.of(2025, 7, 1, 9, 0, 0));

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations.size()).isEqualTo(2);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力するようにしてください。");
  }


  @Test
  void 仮申し込み状態で本申し込み以外のAPIを呼ぶと400エラーが返ること() throws Exception {
    doThrow(new IllegalStateException("受講中ではありません。"))
        .when(service)
        .updateCourseStatus("1", CourseStatusType.COMPLETED);

    mockMvc.perform(patch("/CourseStatus/{coursesId}/completed", "1"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("受講中ではありません。"));
  }


  @Test
  void 存在しない申し込み状況IDで受講開始APIを呼ぶと404が返ること() throws Exception {
    doThrow(new IllegalArgumentException("指定したコースが存在しません。"))
        .when(service)
        .updateCourseStatus("999", CourseStatusType.IN_PROGRESS);

    mockMvc.perform(patch("/CourseStatus/{coursesId}/inProgress", "999"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("指定したコースが存在しません。"));
  }
}