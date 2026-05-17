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
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본키
    private Long id;

    //글 제목
    private String title;

    //글 내용
    @Column(columnDefinition = "TEXT")
    private String content;

    // 여러 개의 게시글은 한 명의 작성자에게 속함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;
    
    //생성자
    public Post() {
        
    }

}