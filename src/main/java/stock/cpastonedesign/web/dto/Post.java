package stock.cpastonedesign.web.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
<<<<<<< HEAD
import stock.cpastonedesign.domain.User; // 유저 엔티티 위치 연결

@Getter
@Setter
@Entity // 이 클래스는 DB 테이블과 매핑됩니다.
=======
import stock.cpastonedesign.domain.User;

@Getter
@Setter
@Entity
>>>>>>> 4518b9e (커뮤니티 기능 및 로그인 기능 추가)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

<<<<<<< HEAD
    // ==========================================
    // ✨ 여기서부터가 유저 연동 핵심 코드입니다!
    // ==========================================
    @ManyToOne(fetch = FetchType.LAZY) // 여러 개의 게시글(Many)은 한 명의 작성자(One)에게 속함
    @JoinColumn(name = "user_id")      // DB 테이블에 'user_id'라는 이름의 컬럼(외래키) 생성
    private User author;               // 이 게시글의 작성자 정보
    // ==========================================

    public Post() {
    }
}
=======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    public Post() {
    }
}
>>>>>>> 4518b9e (커뮤니티 기능 및 로그인 기능 추가)
