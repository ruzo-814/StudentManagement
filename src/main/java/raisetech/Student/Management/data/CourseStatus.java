package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "コース申し込み状況")
@Getter
@Setter

public class CourseStatus {

  @Schema(description = "申し込み状況IDはSQLで自動採番されます", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください。")
  private String statusId;

  @Schema(description = "受講コースIDはSQLで自動採番されます", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください。")
  private String coursesId;

  private CourseStatusType status;

  public enum CourseStatusType {
    TEMPORARY, APPLIED, IN_PROGRESS, COMPLETED
  }
}

