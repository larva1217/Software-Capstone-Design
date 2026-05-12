package stock.cpastonedesign.web.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import stock.cpastonedesign.domain.User; // 유저 엔티티 위치 연결
import stock.cpastonedesign.domain.User;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import stock.cpastonedesign.domain.User;

@Getter
@Setter
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    // 여러 개의 게시글(Many)은 한 명의 작성자(One)에게 속함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    public Post() {
    }
}