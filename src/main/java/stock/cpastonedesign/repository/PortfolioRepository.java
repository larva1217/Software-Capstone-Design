package stock.cpastonedesign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stock.cpastonedesign.domain.Portfolio;

import java.util.List;
import java.util.Optional;

//Portfolio 엔티티에 대한 DB 접근을 담당하는 레포지토리
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> { //Spring Data JPA가 제공하는 인터페이스

    //사용자 ID와 주식 종목 코드로 포트폴리오 조회
    Optional<Portfolio> findByUserIdAndTickerSymbol(Long userId, String tickerSymbol);

    //사용자의 포트폴리오 목록 조회
    List<Portfolio> findAllByUserId(Long userId);

}