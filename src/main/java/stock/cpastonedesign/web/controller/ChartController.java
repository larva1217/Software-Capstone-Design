package stock.cpastonedesign.web.controller;

import jakarta.servlet.http.HttpSession; // 🔥 세션 확인을 위해 추가!
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import stock.cpastonedesign.domain.Portfolio;
import stock.cpastonedesign.domain.User;
import stock.cpastonedesign.repository.PortfolioRepository;
import stock.cpastonedesign.repository.UserRepository;

import java.util.ArrayList; // 🔥 빈 리스트 처리를 위해 추가!
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChartController {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    @GetMapping("/chart")
    public String chartPage(Model model, HttpSession session) {

        // 1. 현재 로그인한 유저의 ID 가져오기
        Long loginUserId = (Long) session.getAttribute("loginUser");

        if (loginUserId != null) {
            // 2. [로그인 상태] 진짜 내 정보와 내 주식 지갑 가져오기
            User user = userRepository.findById(loginUserId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            List<Portfolio> portfolioList = portfolioRepository.findAllByUserId(loginUserId);

            model.addAttribute("user", user);
            model.addAttribute("portfolios", portfolioList);

        } else {
            // 3. [비회원 상태] 에러 방지용 가짜 유저(잔고 0원)와 텅 빈 주식 지갑 넘기기
            User guestUser = new User();
            guestUser.setBalance(0.0); // 비회원은 잔고 0원 노출

            model.addAttribute("user", guestUser);
            model.addAttribute("portfolios", new ArrayList<>()); // 빈 주식 목록
        }

        return "chart";
    }
}