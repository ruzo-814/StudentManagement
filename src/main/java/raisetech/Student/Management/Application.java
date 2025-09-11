package raisetech.Student.Management;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

  @Autowired
  private StudentRepository repository;


  public static void main(String[] args) {

    SpringApplication.run(Application.class, args);
  }

  @GetMapping("/students")
  public List<Student> getStudentList () {
    return repository.searchStudent();
  }

  @GetMapping("/studentCourses")
  public List<StudentCourses> getStudentCousesList(){
    return repository.searchStudentCourses();
  }
}