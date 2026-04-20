package stock.cpastonedesign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stock.cpastonedesign.domain.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //아이디(username)로 DB에서 회원 찾기
    Optional<User> findByUsername(String username);
}
