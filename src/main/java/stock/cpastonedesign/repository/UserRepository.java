package stock.cpastonedesign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stock.cpastonedesign.domain.User;
import java.util.Optional;

//users 테이블을 다루는 DB 담당자

@Repository //DB 작업 담당
public interface UserRepository extends JpaRepository<User, Long> { //Spring Data JPA가 제공하는 인터페이스

    //아이디(username)로 DB에서 회원 찾기
    Optional<User> findByUsername(String username);

}
