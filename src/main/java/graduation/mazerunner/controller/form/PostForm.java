package graduation.mazerunner.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PostForm {

    @NotBlank(message = "제목을 입력해주세요!")
    private String title;

    @NotBlank(message = "빈 칸으로 게시글을 등록할 수 없습니다!")
    private String content;
}
