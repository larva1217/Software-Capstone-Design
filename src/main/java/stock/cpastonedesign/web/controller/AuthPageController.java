package stock.cpastonedesign.web.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//로그인,회원가입 페이지 이동 + 로그아웃 처리
@Controller
public class AuthPageController {

    //로그인 페이지 보여주기
    @GetMapping("/login")
    public String loginPage(HttpSession session, RedirectAttributes ra) {

        //이미 로그인 상태
        if(session.getAttribute("loginUser") != null){
            ra.addFlashAttribute("alertMsg", "이미 로그인되어 있습니다.");
            return "redirect:/";
        }

        return "login";
    }

    //회원가입 페이지 보여주기
    @GetMapping("/signup")
    public String signupPage(HttpSession session, RedirectAttributes ra) {

        //이미 로그인 상태
        if(session.getAttribute("loginUser") != null){
            ra.addFlashAttribute("alertMsg", "이미 로그인되어 있습니다.");
            return "redirect:/";
        }

        return "signup";
    }

    //로그아웃
    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes ra) {
        //이미 로그아웃 상태
        if (session.getAttribute("loginUser") == null) {
            ra.addFlashAttribute("alertMsg", "이미 로그아웃 상태입니다.");
            return "redirect:/";
        }

        session.invalidate();
        ra.addFlashAttribute("alertMsg", "로그아웃 되었습니다.");
        return "redirect:/";
    }

}
