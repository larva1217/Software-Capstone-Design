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

    //매수
    @Transactional
    public void buyStock(Long userId, String ticker, double quantity, long price) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        long totalCost = (long) (price * quantity);

        // 1. 잔액 체크
        if (user.getBalance() < totalCost) {
            throw new RuntimeException("잔액이 부족합니다. 현재 잔액: " + user.getBalance());
        }

        // 2. 현금 차감
        user.setBalance(user.getBalance() - totalCost);

        // 3. 포트폴리오 업데이트
        Portfolio portfolio = portfolioRepository.findByUserIdAndTickerSymbol(userId, ticker)
                .orElse(new Portfolio(user, ticker, 0.0, 0L));

        portfolio.updatePosition(price, quantity); // 평단가와 수량 계산
        portfolioRepository.save(portfolio);

        // 4. 거래 기록 저장
        saveTransaction(user, ticker, "BUY", price, quantity);
    }

    //매도
    @Transactional
    public void sellStock(Long userId, String ticker, double quantity, long price) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 1. 보유 수량 체크
        Portfolio portfolio = portfolioRepository.findByUserIdAndTickerSymbol(userId, ticker)
                .orElseThrow(() -> new RuntimeException("보유하지 않은 종목입니다."));

        if (portfolio.getQuantity() < quantity) {
            throw new RuntimeException("보유 수량이 부족합니다. 현재 수량: " + portfolio.getQuantity());
        }

        // 2. 포트폴리오 업데이트 (수량 감소)
        portfolio.setQuantity(portfolio.getQuantity() - quantity);

        // 수량이 0이 되면 포트폴리오에서 삭제하거나 그대로 둠
        if (portfolio.getQuantity() <= 0) {
            portfolioRepository.delete(portfolio);
        } else {
            portfolioRepository.save(portfolio);
        }

        // 3. 현금 증가 (매도 금액만큼 balance 추가)
        long totalGain = (long) (price * quantity);
        user.setBalance(user.getBalance() + totalGain);

        // 4. 거래 기록 저장
        saveTransaction(user, ticker, "SELL", price, quantity);
    }

    // 공통 거래 기록 저장 메서드
    private void saveTransaction(User user, String ticker, String type, long price, double quantity) {
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
    // 보유 수량만 쏙 빼오는 메서드
    public double getOwnedQuantity(Long userId, String ticker) {
        return portfolioRepository.findByUserIdAndTickerSymbol(userId, ticker)
                .map(Portfolio::getQuantity)
                .orElse(0.0);
    }
}