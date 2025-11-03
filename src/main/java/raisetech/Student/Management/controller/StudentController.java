package raisetech.Student.Management.controller;

<<<<<<< HEAD

=======
>>>>>>> origin/main
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.PathVariable;
=======
>>>>>>> origin/main
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.Student.Management.data.StudentCourses;
import raisetech.Student.Management.domain.StudentDetail;
import raisetech.Student.Management.service.StudentService;


/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }


  /**
   * 受講生一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生一覧（全件）
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }


  /**
   * 受講生検索です。
   * IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable String id) {
    return service.searchStudent(id);
  }


  /**
   * 受講コース情報検索です。
   * 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講コース情報一覧（全件）
   */
  @GetMapping("/studentCoursesList")
  public List<StudentCourses> getStudentCousesList() {
    return service.searchStudentCousesList();
  }

<<<<<<< HEAD

  /**
   * 新規受講生登録です。
   *
   * @param studentDetail 受講生詳細情報
   * @return 新規受講生登録
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail) {
    StudentDetail responceStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responceStudentDetail);
=======
  @PostMapping("/registerStudent")
  public ResponseEntity<String> registerStudent(@RequestBody StudentDetail studentDetail) {
    service.registerStudent(studentDetail);
    return ResponseEntity.ok("登録処理が成功しました。");
>>>>>>> origin/main
  }


  /**
   * 受講生情報更新です。
   *
   * @param studentDetail 受講生詳細情報
   * @return 受講生情報更新
   */
  @PostMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }
}
