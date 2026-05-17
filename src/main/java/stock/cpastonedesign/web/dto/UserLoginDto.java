package stock.cpastonedesign.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "password")
public class UserLoginDto {

    //아이디
    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    //비밀번호
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}