package raisetech.student.management.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Student {

  private String id;

  @NotBlank
  private String name;

  @NotBlank
  private String furigana;

  @NotBlank
  private String nickname;

  @NotBlank @Email
  private String emailAddress;

  @NotBlank
  private String area;

  @NotNull @PositiveOrZero
  private int age;

  @NotBlank  @Pattern(regexp = "^(male|female|other)$", message = "male/female/otherの中から入力してください")
  private String gender;

  private String remark;

  private boolean isDeleted;

}
