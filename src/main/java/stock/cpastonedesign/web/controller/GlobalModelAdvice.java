package stock.cpastonedesign.web.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import stock.cpastonedesign.repository.UserRepository;

@ControllerAdvice //스프링 MVC에서 모든 컨트롤러의 동작을 보조
@RequiredArgsConstructor //final필드 자동 의존성 주입
public class GlobalModelAdvice {

    private final UserRepository userRepository;

    //모든 컨트롤러 메서드가 호출되기 직전에 무조건 먼저 실행
    @ModelAttribute
    public void addLoginUser(HttpSession session, org.springframework.ui.Model model) {
        //세션 저장소에 loginUser라는 키로 저장된 로그인한 사용자의 ID
        Long loginUserId = (Long) session.getAttribute("loginUser");

        //없으면 메서드 종료
        if (loginUserId == null) {
            return;
        }

        //세션에 ID가 있으면 DB에서 해당 유저의 정보를 조회
        userRepository.findById(loginUserId).ifPresent(user -> {
            model.addAttribute("loginUserId", loginUserId);
            model.addAttribute("loginUsername", user.getUsername());
        });
    }

}
