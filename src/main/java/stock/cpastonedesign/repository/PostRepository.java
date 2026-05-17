package stock.cpastonedesign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stock.cpastonedesign.web.dto.Post;

import java.util.List;

//Post엔티티에 대한 DB 접근을 담당하는 레포지토리
public interface PostRepository extends JpaRepository<Post, Long> { //Spring Data JPA가 제공하는 인터페이스

    //게시글을 ID 최신순으로 정렬하여 조회
    List<Post> findAllByOrderByIdDesc();

}
