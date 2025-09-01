package raisetech.Student.Management;

import java.util.HashMap;
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
  public Map<String, String> getStudentInfo() {
    return student;
  }

  @PostMapping("/setStudent")
  public void setStudentInfo(@RequestParam Map<String, String> setStudent) {
    if (this.student == null) {
      this.student = new HashMap<>();
    }
    this.student.putAll(setStudent);
}

  @PostMapping("/updateStudentName")
  public void updateStudentName(@RequestParam String name) {
      this.student.put("name", name);
  }

  @PostMapping("/updateStudentAge")
  public void updateStudentAge(@RequestParam String age) {
      this.student.put("age", age);
  }
}


