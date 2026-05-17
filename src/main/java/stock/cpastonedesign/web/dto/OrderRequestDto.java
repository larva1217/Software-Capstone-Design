package stock.cpastonedesign.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {

    //주식 종목
    private String symbol;

    //수량
    private int quantity;

    //가격
    private double price;

    //매수인지 매도인지
    private String orderType;

}