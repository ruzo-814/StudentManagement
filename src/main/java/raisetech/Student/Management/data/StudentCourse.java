package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter

public class StudentCourse {

  @Schema(description = "受講コースIDはSQLで自動採番されます", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  private String coursesId;

  @Schema(description = "受講生IDはSQLで自動採番されます", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  private String studentId;

  @Schema(description = "受講コース名を入力してください。", example = "Javaコース")
  @NotBlank
  private String courseName;

  private LocalDateTime startDate;

  private LocalDateTime scheduledEndDate;

}