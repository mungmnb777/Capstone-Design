package graduation.mazerunner.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignupForm {

    @NotBlank(message = "ID는 필수 입력 값입니다.")
    private String id;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String passwordCheck;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;
}
