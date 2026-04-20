package stock.cpastonedesign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stock.cpastonedesign.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
