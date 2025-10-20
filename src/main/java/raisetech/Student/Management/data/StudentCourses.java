package raisetech.Student.Management.data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StudentCourses {

  private String coursesId;
  private String studentId;
  private String courseName;
  private LocalDateTime startDate;
  private LocalDateTime scheduledEndDate;

}