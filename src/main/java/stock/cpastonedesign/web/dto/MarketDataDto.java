package stock.cpastonedesign.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketDataDto {

    //지수 이름
    private String name;

    //현재 가격
    private String price;

    //등락률
    private String changeRate;
}
