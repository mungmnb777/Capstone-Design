package graduation.mazerunner.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MakerForm {

    @NotNull(message = "제목을 입력해주세요!")
    private String title;

    @NotBlank
    private String content;

    @NotNull
    @Min(value = 2, message = "맵의 높이는 2부터 30까지 지원합니다.")
    @Max(value = 30, message = "맵의 높이는 2부터 30까지 지원합니다.")
    private int height;

    @NotNull
    @Min(value = 2, message = "맵의 너비는 2부터 30까지 지원합니다.")
    @Max(value = 30, message = "맵의 너비는 2부터 30까지 지원합니다.")
    private int width;

    @NotNull
    @Min(value = 0, message = "최소 입력값은 0입니다.")
    @Max(value = 900, message = "최대 입력값은 900입니다.")
    private int breakCount;
}
