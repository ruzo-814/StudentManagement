package raisetech.student.management.data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StudentCourse {

  private String coursesId;

  private String studentId;

  @NotBlank
  private String courseName;

  private LocalDateTime startDate;

  private LocalDateTime scheduledEndDate;

}