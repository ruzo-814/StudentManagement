package raisetech.Student.Management.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.Student.Management.controller.converter.StudentConverter;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.data.StudentCourses;
import raisetech.Student.Management.domain.StudentDetail;
import raisetech.Student.Management.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourses> studentCourses = service.searchStudentCousesList();

    model.addAttribute("studentList", converter.convertStudentDetails(students,
        studentCourses)); //←studentDetailから返ってきた生徒情報を入れるリスト名
    return "studentList"; //←HTMLのクラス名（上のstudentListとは別物。上のは名前任意）
  }

  @GetMapping("/studentCoursesList")
  public List<StudentCourses> getStudentCousesList() {
    return service.searchStudentCousesList();
  }


  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }


  @PostMapping("/registerStudent")
  //↓studentはregisterStudentの${student}の部分
  public String registerStudent(@ModelAttribute StudentDetail studentDetail) {
    return "";
  }

}
