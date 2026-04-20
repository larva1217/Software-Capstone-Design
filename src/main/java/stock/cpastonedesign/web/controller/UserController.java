package stock.cpastonedesign.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock.cpastonedesign.domain.User;
import stock.cpastonedesign.service.UserService;
import stock.cpastonedesign.web.dto.UserLoginDto;
import stock.cpastonedesign.web.dto.UserSignupDto;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserSignupDto request) {
        try {
            // 프론트엔드에서 받은 가입신청 UserService에 넘겨 처리
            userService.signup(request.getUsername(), request.getPassword());
            return ResponseEntity.ok("회원가입 성공! 1,000만원이 지급되었습니다.");
        } catch (RuntimeException e) {
            // [추가된 부분] 아이디가 중복될 경우 여기서 에러 메시지를 뱉어냅니다!
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto request, HttpServletRequest httpRequest) {
        HttpSession session;
        try {
            // 1. UserService에게 검사 시키기
            User loginUser = userService.login(request.getUsername(), request.getPassword());

            // 2. 검사 통과. 세션 발급해주기
            session = httpRequest.getSession();
            // 팔찌에 "loginUser"라는 이름으로 이 사람의 ID(번호)를 적어둡니다.
            session.setAttribute("loginUser", loginUser.getId());

            return ResponseEntity.ok(loginUser.getUsername() + "님, 환영합니다!");

        } catch (RuntimeException e) {
            // 비밀번호가 틀렸거나 없는 아이디면 쫓아냄
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // [추가된 부분] 로그아웃 기능 추가
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        // 사용자가 차고 있는 세션 팔찌를 가위로 잘라 폐기합니다.
        session.invalidate();
        return ResponseEntity.ok("로그아웃이 완료되었습니다.");
    }

}