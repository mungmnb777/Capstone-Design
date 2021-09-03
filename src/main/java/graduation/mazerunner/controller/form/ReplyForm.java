package graduation.mazerunner.controller.form;

import graduation.mazerunner.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyForm {

    private Long id;

    private String content;

    private Member member;
}
