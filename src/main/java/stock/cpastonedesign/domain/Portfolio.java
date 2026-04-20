package stock.cpastonedesign.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Portfolio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String tickerSymbol;
    private Double quantity;
    private Long averagePrice;


    public Portfolio(User user, String tickerSymbol, Double quantity, Long averagePrice) {
        this.user = user;
        this.tickerSymbol = tickerSymbol;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
    }

    public void updatePosition(long newPrice, double newQuantity) {
        long totalCost = (long) (this.averagePrice * this.quantity) + (long) (newPrice * newQuantity);
        this.quantity += newQuantity;
        this.averagePrice = (long) (totalCost / this.quantity);
    }
}
