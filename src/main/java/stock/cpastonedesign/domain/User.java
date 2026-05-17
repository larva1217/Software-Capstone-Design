package stock.cpastonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity //이 클래스는 DB테이블이다.
@Table(name = "users") //DB테이블 이름 users
@Getter
@Setter
@NoArgsConstructor //기본 생성자 자동 생성
public class User {

    @Id //기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id를 DB가 자동으로 생성해라
    private Long id;

    //로그인 아이디
    private String username;
    
    //로그인 비밀번호
    private String password;

    //현재 보유 현금
    private double balance;

    //생성자
    public User(String username, double balance) {
        this.username = username;
        this.balance = balance;
    }

}