package stock.cpastonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // 이 클래스는 DB 테이블이다
@Getter
@Setter
@NoArgsConstructor //기본 생성자 자동 생성
public class Portfolio {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본키 자동 증가
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //필요할 때만 User를 가져와라
    @JoinColumn(name = "user_id") //profile의 user_id는 users테이블의 id를 참조
    private User user;

    //주식 종목 코드
    private String tickerSymbol;

    //보유 수량
    private Double quantity;

    //평균 매수가
    private Long averagePrice;

    //생성자
    public Portfolio(User user, String tickerSymbol, Double quantity, Long averagePrice) {
        this.user = user;
        this.tickerSymbol = tickerSymbol;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
    }

    //추가 매수 시 평균 매수가를 계산
    public void updatePosition(long newPrice, double newQuantity) {
        long totalCost = (long)(this.averagePrice * this.quantity) + (long)(newPrice * newQuantity);
        this.quantity += newQuantity;
        this.averagePrice = (long) (totalCost / this.quantity);
    }
    
}
