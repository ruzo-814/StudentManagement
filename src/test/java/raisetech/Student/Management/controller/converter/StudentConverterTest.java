package raisetech.student.management.controller.converter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import raisetech.student.management.controller.StudentController;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

@WebMvcTest(StudentConverter.class)
class StudentConverterTest {

  void 受講生と受講コース情報が紐づけられ受講生詳細となっていること(){
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();

    Student student = new Student();
    student.setId("1");
    student.setName("John Miller");

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(List.of(course));


  }

}