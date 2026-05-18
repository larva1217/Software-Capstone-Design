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
import stock.cpastonedesign.service.StockService;

import java.util.ArrayList; // 🔥 빈 리스트 처리를 위해 추가!
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChartController {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final StockService stockService;
    private Double profitRate;

    @GetMapping("/chart")
    public String chartPage(Model model, HttpSession session) {

        // 현재 로그인한 유저의 ID 가져오기
        Long loginUserId = (Long) session.getAttribute("loginUser");

        if (loginUserId != null) {

            // 내 정보 가져오기
            User user = userRepository.findById(loginUserId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            // 내 보유 종목 가져오기
            List<Portfolio> portfolioList =
                    portfolioRepository.findAllByUserId(loginUserId);

            for (Portfolio p : portfolioList) {
                double profitRate = 0.0; // 현재가 없으니까 0 처리
                p.setProfitRate(profitRate);
            }

            model.addAttribute("user", user);
            model.addAttribute("portfolios", portfolioList);

        } else {

            // 비회원 상태 → 에러 방지용 가짜 유저 + 빈 포트폴리오
            User guestUser = new User();
            guestUser.setBalance(0.0);

            model.addAttribute("user", guestUser);
            model.addAttribute("portfolios", new ArrayList<>());
        }

        return "chart";
    }

}