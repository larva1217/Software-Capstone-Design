package stock.cpastonedesign.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {
    private String symbol;
    private int quantity;
    private double price;
    private String orderType;
}