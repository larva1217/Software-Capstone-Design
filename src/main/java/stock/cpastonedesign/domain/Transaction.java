package stock.cpastonedesign.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //거래 여러 개->한명의 사용자, 필요할 때만 User를 가져와라
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") //user_id가 users.id를 참조 외래키
    private User user;

    private String tickerSymbol;
    private String type;
    private Long price;
    private Double quantity;
    private LocalDateTime createdAt;

}