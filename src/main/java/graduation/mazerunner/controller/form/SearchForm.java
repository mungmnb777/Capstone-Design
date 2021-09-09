package graduation.mazerunner.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SearchForm {

    public String type;

    @NotBlank
    @Min(value = 2, message = "두 글자 이상 입력해주세요!")
    public String inputValue;
}
