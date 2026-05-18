package stock.cpastonedesign.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor //기본 생성자 자동 생성
@AllArgsConstructor //모든 필드를 포함한 생성자 자동 생성
@Builder //builder 패턴 사용 가능
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본 키 자동 증가
    private Long id;

    //거래 여러 개->한명의 사용자, 필요할 때만 User를 가져와라
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") //user_id가 users.id를 참조 외래키
    private User user;

    //주식 종목 코드
    private String tickerSymbol;
    
    //거래 종류(매수, 매도)
    private String type;
    
    //거래 가격
    private Double price;
    
    //거래 수량
    private Double quantity;
    
    //거래가 발생한 시간
    private LocalDateTime createdAt;

}