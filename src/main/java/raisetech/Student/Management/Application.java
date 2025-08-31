package raisetech.Student.Management;

import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

  private String name = "LAO Student";
  private String age = "28";


  private Map<String, String> student;


  public static void main(String[] args) {

    SpringApplication.run(Application.class, args);
  }

  @GetMapping("/studentInfo")
  public String getStudentInfo() {

    return this.name + " " + this.age + "age";
  }


  @PostMapping("/studentInfo")
  public void setStudentInfo(String name, String age) {
    this.name = student.get("name");
    this.age = student.get("age");
  }

  @PostMapping("/studentName")
  public void updateStudentName(String name) {
    this.name = name;
  }
}


