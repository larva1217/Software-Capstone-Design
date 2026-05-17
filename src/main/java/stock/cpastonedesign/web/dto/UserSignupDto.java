package stock.cpastonedesign.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "password") //비밀번호는 민감함 정보여서 ToString 찍히면 안됨
public class UserSignupDto {

    //아이디
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min=4, max=20, message = "아이디는 4~20자여야 합니다.")
    private String username;

    //비밀번호
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 4, max = 30, message = "비밀번호는 4자 이상이어야 합니다.")
    private String password;
}