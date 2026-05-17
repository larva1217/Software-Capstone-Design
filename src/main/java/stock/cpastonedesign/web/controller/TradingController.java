package stock.cpastonedesign.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stock.cpastonedesign.service.TradingService;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradingController {

    private final TradingService tradingService;

    @PostMapping("/buy")
    public ResponseEntity<String> buy(@RequestParam String ticker, @RequestParam double quantity, @RequestParam long price, jakarta.servlet.http.HttpSession session) {

        //세션에 로그인한 사용자 ID 조회
        Long userId = (Long) session.getAttribute("loginUser");

        //세션 없으면 돌려보내기
        if (userId == null) {
            return ResponseEntity.badRequest().body("로그인이 필요합니다! 먼저 로그인을 해주세요.");
        }

        //userId로 매수 진행
        tradingService.buyStock(userId, ticker, quantity, price);

        //200 ok코드와 성공 텍스트를 body에 담아 반환
        return ResponseEntity.ok(ticker + " 매수 성공!");
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sell(@RequestParam String ticker, @RequestParam double quantity, @RequestParam long price, jakarta.servlet.http.HttpSession session) {

        //세션에 로그인한 사용자 ID 조회
        Long userId = (Long) session.getAttribute("loginUser");

        //로그인 안 했으면 400에러 처리
        if (userId == null) {
            return ResponseEntity.badRequest().body("로그인이 필요합니다! 먼저 로그인을 해주세요.");
        }

        //매도 비즈니스 로직
        tradingService.sellStock(userId, ticker, quantity, price);
        return ResponseEntity.ok(ticker + " 매도 성공!");
    }

    //특정 종목의 현재 보유 수량을 조회하는 API
    @GetMapping("/quantity")
    public ResponseEntity<Double> getOwnedQuantity(@RequestParam String ticker, jakarta.servlet.http.HttpSession session) {

        //세션에 로그인한 사용자 ID 조회
        Long userId = (Long) session.getAttribute("loginUser");

        //로그인 안 했으면 0주
        if (userId == null) {
            return ResponseEntity.ok(0.0);
        }

        //로그인 사용자면 TradingService에 수량 조회 요청
        return ResponseEntity.ok(tradingService.getOwnedQuantity(userId, ticker));
    }

}