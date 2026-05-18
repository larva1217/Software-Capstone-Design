package stock.cpastonedesign.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stock.cpastonedesign.domain.Portfolio;
import stock.cpastonedesign.domain.Transaction;
import stock.cpastonedesign.domain.User;
import stock.cpastonedesign.repository.PortfolioRepository;
import stock.cpastonedesign.repository.TransactionRepository;
import stock.cpastonedesign.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TradingService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final TransactionRepository transactionRepository;

    //주식 매수
    @Transactional
    public void buyStock(Long userId, String ticker, double quantity, double price) {
        //DB에서 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        //총 매수 금액 계산
        long totalCost = (long) (price * quantity);

        //잔액 체크
        if (user.getBalance() < totalCost) {
            throw new RuntimeException("잔액이 부족합니다. 현재 잔액: " + user.getBalance());
        }

        //현금 차감
        user.setBalance(user.getBalance() - totalCost);

        //포트폴리오 업데이트
        Portfolio portfolio = portfolioRepository.findByUserIdAndTickerSymbol(userId, ticker)
                .orElse(new Portfolio(user, ticker, 0.0, 0.0));

        //새로운 평단가와 보유 수량 갱싱
        portfolio.updatePosition(price, quantity); // 평단가와 수량 계산
        portfolioRepository.save(portfolio);

        //거래 기록 생성 및 저장
        saveTransaction(user, ticker, "BUY", price, quantity);
    }

    //주식 매도
    @Transactional
    public void sellStock(Long userId, String ticker, double quantity, double price) {
        //DB에서 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        //해당 종목을 보유하고 있는지 확인
        Portfolio portfolio = portfolioRepository.findByUserIdAndTickerSymbol(userId, ticker)
                .orElseThrow(() -> new RuntimeException("보유하지 않은 종목입니다."));

        //매도하려는 수량이 실제 보유 수량보다 많은지 확인
        if (portfolio.getQuantity() < quantity) {
            throw new RuntimeException("보유 수량이 부족합니다. 현재 수량: " + portfolio.getQuantity());
        }

        //포트폴리오 업데이트 
        portfolio.setQuantity(portfolio.getQuantity() - quantity);

        //수량이 0이 되면 포트폴리오에서 삭제하거나 그대로 둠
        if (portfolio.getQuantity() <= 0) {
            portfolioRepository.delete(portfolio);
        } else {
            portfolioRepository.save(portfolio);
        }

        //현금 증가 (매도 금액만큼 balance 추가)
        long totalGain = (long) (price * quantity);
        user.setBalance(user.getBalance() + totalGain);

        //거래 기록 생성 및 저장
        saveTransaction(user, ticker, "SELL", price, quantity);
    }

    // 공통 거래 기록 저장 메서드
    private void saveTransaction(User user, String ticker, String type, double price, double quantity) {
        Transaction transaction = Transaction.builder()
                .user(user)
                .tickerSymbol(ticker)
                .type(type)
                .price(price)
                .quantity(quantity)
                .createdAt(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);
    }

    //특정 사용자가 보유한 특정 종목의 수량만 조회
    public double getOwnedQuantity(Long userId, String ticker) {
        return portfolioRepository.findByUserIdAndTickerSymbol(userId, ticker)
                .map(Portfolio::getQuantity)
                .orElse(0.0);
    }

}