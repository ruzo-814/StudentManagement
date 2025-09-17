package raisetech.Student.Management.service;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.data.StudentCourses;
import raisetech.Student.Management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    List<Student> students = repository.search();
    return students.stream()
        .filter(student -> student.getAge() >= 20 && student.getAge() < 30)
        .collect(Collectors.toList());
  }

  public List<StudentCourses> searchStudentCousesList() {
    List<StudentCourses> studentsCourses = repository.searchCourses();
    return studentsCourses.stream()
        .filter(studentCourse -> studentCourse.getCourseName().equals("Java Programming Basics"))
        .collect(Collectors.toList());
  }
}
