package raisetech.student.management.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.TestIdSearchException;
import raisetech.student.management.exception.TestSearchException;
import raisetech.student.management.exception.TestRegisterException;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生一覧（全件）
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細検索です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable @Size(min = 1, max = 3) String id) {
    return service.searchStudent(id);
  }

  /**
   * 受講コース情報検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講コース情報一覧（全件）
   */
  @GetMapping("/studentCoursesList")
  public List<StudentCourse> getStudentCousesList() {
    return service.searchStudentCousesList();
  }

  /**
   * 新規受講生登録です。
   *
   * @param studentDetail 受講生詳細情報
   * @return 実行結果
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responceStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responceStudentDetail);
  }

  /**
   * 受講生情報更新です。 キャンセルフラグの更新もここで行います。（論理削除）
   *
   * @param studentDetail 受講生詳細情報
   * @return 実行結果
   */
  @PutMapping("/updateStudent")
  // PutMapping = 全体的な更新
  // PatchMapping = 部分的な更新
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }


  /**
   * 受講生一覧検索の例外処理用のメソッドです。
   *
   * @return 入力されたURLが間違っていた場合の例外処理
   * @throws TestSearchException
   */
  @GetMapping("/testSearchException")
  public List<StudentDetail> testSearchException() throws TestSearchException {
    throw new TestSearchException(
        "現在このAPIは利用できません。URLは「studentList」ではなく「students」を利用してください。");
  }


  /**
   * 受講生検索の例外処理用のメソッドです。
   *
   * @return 入力されたユーザーIDが論理削除されていた場合の例外処理
   * @throws TestIdSearchException
   */
  @GetMapping("/testIdSearchException")
  public List<StudentDetail> testIdSearchException() throws TestIdSearchException {
    throw new TestIdSearchException(
        "[ID:〇〇]入力されたユーザーIDは退会済みです。");
  }


  /**
   * 新規受講生登録の例外処理用のメソッドです。
   *
   * @return 入力された内容がバリデーションに引っかかった場合の例外処理
   * @throws TestRegisterException
   */
  @PostMapping("/RegisterException")
  public ResponseEntity<StudentDetail> testRegisterException() throws TestRegisterException {
    throw new TestRegisterException("性別は[male/female/other]の中から一つを入力してください。");
  }

}
