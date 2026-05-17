package stock.cpastonedesign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stock.cpastonedesign.domain.Transaction;

//Transaction엔티티에 대한 DB 접근을 담당하는 레포지토리
public interface TransactionRepository extends JpaRepository<Transaction, Long> { //Spring Data JPA가 제공하는 인터페이스

}
