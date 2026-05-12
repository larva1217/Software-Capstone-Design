package stock.cpastonedesign.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import stock.cpastonedesign.domain.Portfolio;
import stock.cpastonedesign.domain.User;
import stock.cpastonedesign.repository.PortfolioRepository;
import stock.cpastonedesign.repository.UserRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChartController {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    @GetMapping("/chart")
    public String chartPage(Model model) {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<Portfolio> portfolioList = portfolioRepository.findAllByUserId(1L);

        model.addAttribute("user", user);
        model.addAttribute("portfolios", portfolioList);

        return "chart"; // resources/templates/chart.html 파일을 실행합니다.
    }
}