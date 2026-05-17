package stock.cpastonedesign.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stock.cpastonedesign.service.TradingService;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradingController {

    private final TradingService tradingService;

    @PostMapping("/buy")
    public ResponseEntity<String> buy(@RequestParam String ticker,
                                      @RequestParam double quantity,
                                      @RequestParam long price,
                                      jakarta.servlet.http.HttpSession session) {

        // 1. 세션에서 유저 번호 꺼내기
        Long userId = (Long) session.getAttribute("loginUser");

        // 2. 세션 없으면 돌려보내기
        if (userId == null) {
            return ResponseEntity.badRequest().body("로그인이 필요합니다! 먼저 로그인을 해주세요.");
        }

        // 3. 유저 번호(userId)로 매수 진행
        tradingService.buyStock(userId, ticker, quantity, price);
        return ResponseEntity.ok(ticker + " 매수 성공!");
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sell(@RequestParam String ticker,
                                       @RequestParam double quantity,
                                       @RequestParam long price,
                                       jakarta.servlet.http.HttpSession session) {

        Long userId = (Long) session.getAttribute("loginUser");

        if (userId == null) {
            return ResponseEntity.badRequest().body("로그인이 필요합니다! 먼저 로그인을 해주세요.");
        }

        tradingService.sellStock(userId, ticker, quantity, price);
        return ResponseEntity.ok(ticker + " 매도 성공!");
    }
    //프론트엔드에서 "몇 주 가지고 있어?"라고 물어볼 때 대답해주는 API
    @org.springframework.web.bind.annotation.GetMapping("/quantity")
    public ResponseEntity<Double> getOwnedQuantity(@RequestParam String ticker, jakarta.servlet.http.HttpSession session) {
        Long userId = (Long) session.getAttribute("loginUser");
        if (userId == null) {
            return ResponseEntity.ok(0.0); // 비로그인이면 0주
        }

        // TradingService에 수량 조회 요청
        return ResponseEntity.ok(tradingService.getOwnedQuantity(userId, ticker));
    }

}