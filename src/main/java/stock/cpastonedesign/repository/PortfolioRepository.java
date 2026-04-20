package stock.cpastonedesign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stock.cpastonedesign.domain.Portfolio;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByUserIdAndTickerSymbol(Long userId, String tickerSymbol);
    List<Portfolio> findAllByUserId(Long userId);
}