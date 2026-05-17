package stock.cpastonedesign.web.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import stock.cpastonedesign.repository.UserRepository;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAdvice {

    private final UserRepository userRepository;

    @ModelAttribute
    public void addLoginUser(HttpSession session, org.springframework.ui.Model model) {
        Long loginUserId = (Long) session.getAttribute("loginUser");
        if (loginUserId == null) {
            return;
        }
        userRepository.findById(loginUserId).ifPresent(user -> {
            model.addAttribute("loginUserId", loginUserId);
            model.addAttribute("loginUsername", user.getUsername());
        });
    }
}
