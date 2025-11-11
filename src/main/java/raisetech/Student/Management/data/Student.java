package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter

public class Student {

  @Schema(description = "受講生IDはSQLで自動採番されます", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  private String id;

  @Schema(description = "フルネームで入力してください。", example = "田中 太郎")
  @NotBlank
  private String name;

  @Schema(description = "ローマ字表記で入力してください", example = "タナカ タロウ")
  @NotBlank
  private String furigana;

  @Schema(description = "ひらがなまたはカタカナまたはローマ字で入力してください。", example = "タロー")
  @NotBlank
  private String nickname;

  @Schema(description = "メールアドレスを入力してください。", example = "taro@example.com")
  @NotBlank @Email
  private String emailAddress;

  @Schema(description = "済んでいる都市名を入力してください。", example = "東京")
  @NotBlank
  private String area;

  @Schema(description = "年齢を入力してください。", example = "20")
  @NotNull @PositiveOrZero
  private int age;

  @Schema(description = "性別を[male/female/other]の中から入力してください。", example = "male")
  @NotBlank  @Pattern(regexp = "^(male|female|other)$", message = "male/female/otherの中から入力してください")
  private String gender;

  private String remark;

  private boolean isDeleted;

}
