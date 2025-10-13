package raisetech.Student.Management.controller;


import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentCourses(Arrays.asList(new StudentCourses()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  //↓studentはregisterStudentの${student}の部分
  public String registerStudent(@ModelAttribute StudentDetail studentDetail,
      BindingResult result) { //←バリデーションをしてくれる
    if (result.hasErrors()) {
      return "registerStudent";
    }
    service.registerStudent(studentDetail);
    return "redirect:/studentList";
  }

  @GetMapping("/student/{id}")
  public String getStudent(@PathVariable String id, Model model) {
    StudentDetail studentDetail = service.searchStudent(id);
    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent";
  }

  @PostMapping("/updateStudent")
  //↓studentはregisterStudentの${student}の部分
  public String updateStudent(@ModelAttribute StudentDetail studentDetail,
      BindingResult result) { //←バリデーションをしてくれる
    if (result.hasErrors()) {
      return "updateStudent";
    }
    service.updateStudent(studentDetail);
    return "redirect:/studentList";
  }
}
