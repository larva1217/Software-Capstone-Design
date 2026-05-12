package stock.cpastonedesign.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketDataDto {

    private String name;        // 지수 이름
    private String price;       // 현재 가격
    private String changeRate;  // 등락률

}
