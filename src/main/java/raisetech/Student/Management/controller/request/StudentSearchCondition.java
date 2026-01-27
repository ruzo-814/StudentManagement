package raisetech.student.management.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生検索条件")
@Getter
@Setter

public class StudentSearchCondition {

  private String name;
  private String furigana;
  private String nickname;
  private String emailAddress;
  private Integer age;
  private String area;
  private String gender;
  private Boolean isDeleted;

  public boolean isEmpty() {
    if (name != null && !name.isBlank()) {
      return false;
    }

    if (furigana != null && !furigana.isBlank()) {
      return false;
    }

    if (nickname != null && !nickname.isBlank()) {
      return false;
    }

    if (emailAddress != null && !emailAddress.isBlank()) {
      return false;
    }

    if (age != null) {
      return false;
    }

    if (area != null && !area.isBlank()) {
      return false;
    }

    if (gender != null && !gender.isBlank()) {
      return false;
    }

    if (isDeleted != null) {
      return false;
    }
    return true;
  }
}