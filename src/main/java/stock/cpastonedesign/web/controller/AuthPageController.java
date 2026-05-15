package stock.cpastonedesign.web.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

//로그인,회원가입 페이지 이동 + 로그아웃 처리
@Controller
public class AuthPageController {

    //로그인 페이지 보여주기
    @GetMapping("/login")
    public String loginPage(HttpSession session) {

        //이미 로그인 상태
        if(session.getAttribute("loginUser") != null){
            return "redirect:/";
        }

        return "login";
    }

    //회원가입 페이지 보여주기
    @GetMapping("/signup")
    public String signupPage(HttpSession session) {

        //이미 로그인 상태
        if(session.getAttribute("loginUser") != null){
            return "redirect:/";
        }

        return "signup";
    }

    //로그아웃
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); //세션 삭제
        return "redirect:/";
    }

}
