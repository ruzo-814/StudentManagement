package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.controller.request.StudentSearchCondition;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.CourseStatus.CourseStatusType;
import raisetech.student.management.data.Student;
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
   * 受講生一覧検索です。 条件指定がなかった場合、全件検索が実行されます。
   *
   * @return 受講生一覧（全件）
   */
  @Operation(summary = "一覧検索",
      description = "受講生の一覧を検索します。",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "受講生一覧を表示します。",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Student.class))),
          @ApiResponse(responseCode = "400", description = "URLが間違っています。"),}
  )
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList(StudentSearchCondition condition) {
    return service.searchStudentList(condition);
  }

  /**
   * 受講生詳細検索です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @Operation(summary = "受講生ID検索",
      description = "指定したIDの受講生を検索します。",
      parameters = {@Parameter(name = "id", description = "受講生ID（例：20）", required = true)
      },
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "指定したIDの受講生情報を表示します。",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class))),
          @ApiResponse(responseCode = "404", description = "指定したIDの受講生が存在しません。"),}
  )
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable @Size(min = 1, max = 3) String id) {
    return service.searchStudent(id);
  }

  /**
   * 受講コース情報検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講コース情報一覧（全件）
   */
  @Operation(summary = "コース一覧検索",
      description = "コース情報の一覧を検索します。",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "コース情報の一覧を表示します。",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentCourse.class))),
          @ApiResponse(responseCode = "400", description = "URLが間違っています。"),})
  @GetMapping("/studentCoursesList")
  public List<StudentCourse> getStudentCousesList() {
    return service.searchStudentCousesList();
  }


  /**
   * コース申し込み状況確認です。
   *
   * @param coursesId 受講コースID
   * @return コース申し込み状況
   */
  @Operation(summary = "コース申し込み状況確認",
      description = "指定した受講コースの申し込み状況を取得します。",
      parameters = {@Parameter(name = "coursesId", description = "受講コースID", required = true)},
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "コース申し込み状況を表示します。",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = CourseStatus.class)
              )),
          @ApiResponse(responseCode = "404", description = "指定したIDの受講生が存在しません。")}
  )
  @GetMapping("/CourseStatus")
  public List<CourseStatus> getCourseStatus(
      @RequestParam(required = false)
      @PathVariable
      @Pattern(regexp = "^\\d+$", message = "受講コースIDは数字のみです")
      String coursesId) {

    return service.searchCourseStatus(coursesId);
  }


  /**
   * 新規受講生登録です。
   *
   * @param studentDetail 受講生詳細情報
   * @return 実行結果
   */
  @Operation(summary = "受講生登録",
      description = "新規受講生を登録します。",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "登録する受講生情報（IDは不要）",
          content = @Content(schema = @Schema(implementation = StudentDetail.class))
      ),
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "登録した受講生情報を表示します。",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class))),
          @ApiResponse(responseCode = "400", description = "不正な入力値です。"),}
  )
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
  @Operation(summary = "受講生情報更新",
      description = "受講生の登録情報を更新します。",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "登録する受講生情報（IDは不要）",
          content = @Content(schema = @Schema(implementation = StudentDetail.class))),
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "更新後の受講生情報を表示します。",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class)
              )
          ),
          @ApiResponse(responseCode = "400", description = "不正な入力値です。")}
  )
  @PutMapping("/updateStudent")
  // PutMapping = 全体的な更新
  // PatchMapping = 部分的な更新
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }


  /**
   * コースの本登録を行います。
   *
   * @param coursesId 受講コースID
   * @return 実行結果
   */
  @Operation(
      summary = "コース本登録",
      description = "仮申し込み状態のコースを本登録状態に変更します。",
      parameters = {@Parameter(name = "coursesId", description = "受講コースID", required = true)},
      responses = {
          @ApiResponse(responseCode = "200", description = "本登録が完了しました。"),
          @ApiResponse(responseCode = "400", description = "仮申し込み状態ではありません。"),
          @ApiResponse(responseCode = "404", description = "指定したコースが存在しません。")
      }
  )
  @PatchMapping("/CourseStatus/{coursesId}/apply")
  public ResponseEntity<String> applyCourse(
      @PathVariable @Pattern(regexp = "^\\d+$", message = "受講コースIDは数字のみです") String coursesId) {
    service.updateCourseStatus(coursesId, CourseStatusType.APPLIED);
    return ResponseEntity.ok("本登録が完了しました。");
  }


  /**
   * コースの受講を開始します。
   *
   * @param coursesId 受講コースID
   * @return 実行結果
   */
  @Operation(
      summary = "コース受講中",
      description = "本申し込み状態のコースを受講中に変更します。",
      parameters = {@Parameter(name = "coursesId", description = "受講コースID", required = true)},
      responses = {
          @ApiResponse(responseCode = "200", description = "受講開始しました。"),
          @ApiResponse(responseCode = "400", description = "本申し込み状態ではありません。"),
          @ApiResponse(responseCode = "404", description = "指定したコースが存在しません。")
      }
  )
  @PatchMapping("/CourseStatus/{coursesId}/inProgress")
  public ResponseEntity<String> inProgress(
      @PathVariable @Pattern(regexp = "^\\d+$", message = "受講コースIDは数字のみです") String coursesId) {
    service.updateCourseStatus(coursesId, CourseStatusType.IN_PROGRESS);
    return ResponseEntity.ok("受講開始しました。");
  }


  /**
   * コースの受講を終了します。
   *
   * @param coursesId 受講コースID
   * @return 実行結果
   */
  @Operation(
      summary = "コース受講終了",
      description = "受講中のコースを受講終了します。",
      parameters = {@Parameter(name = "coursesId", description = "受講コースID", required = true)},
      responses = {
          @ApiResponse(responseCode = "200", description = "受講終了しました。"),
          @ApiResponse(responseCode = "400", description = "受講中ではありません。"),
          @ApiResponse(responseCode = "404", description = "指定したコースが存在しません。")
      }
  )
  @PatchMapping("/CourseStatus/{coursesId}/completed")
  public ResponseEntity<String> completed(
      @PathVariable @Pattern(regexp = "^\\d+$", message = "受講コースIDは数字のみです") String coursesId) {
    service.updateCourseStatus(coursesId, CourseStatusType.COMPLETED);
    return ResponseEntity.ok("受講終了しました。");
  }


  /**
   * 受講生一覧検索の例外処理用のメソッドです。
   *
   * @return 入力されたURLが間違っていた場合の例外処理
   * @throws TestSearchException
   */
  @Operation(summary = "一覧検索例外処理",
      description = "入力した一覧検索のURLが間違っていた場合の例外処理を実行します。")
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
  @Operation(summary = "ID検索例外処理",
      description = "指定したIDが論理削除されていた場合の例外処理を実行します。")
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
  @Operation(summary = "受講生登録例外処理",
      description = "新規受講生を登録する際の性別欄の入力がバリデーションに引っかかった場合の例外処理を実行します。")
  @PostMapping("/RegisterException")
  public ResponseEntity<StudentDetail> testRegisterException() throws TestRegisterException {
    throw new TestRegisterException("性別は[male/female/other]の中から一つを入力してください。");
  }

}
