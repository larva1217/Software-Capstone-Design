package stock.cpastonedesign.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import stock.cpastonedesign.service.MarketService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MarketIndexController {

    @Autowired
    MarketService marketService;

    @GetMapping("/")
    public String index(Model model) {
        // 서비스에서 크롤링한 데이터를 가져와서
        List<Map<String, Object>> indices = marketService.getLiveIndices();

        // 반드시 "indices"라는 이름으로 담아야 HTML의 ${indices}가 인식합니다.
        model.addAttribute("indices", indices);

        return "index";
    }

    @GetMapping("/api/stock/price")
    @ResponseBody
    public Map<String, Object> getStockPrice(@RequestParam String symbol) {
        Map<String, Object> response = new HashMap<>();

        // 트레이딩뷰 심볼은 "NASDAQ:NVDA" 식으로 올 수 있으니 ":" 뒤만 잘라주는 처리가 필요
        String Symbol = symbol.contains(":") ? symbol.split(":")[1] : symbol;

        double price = marketService.getRealTimePrice(Symbol);

        response.put("currentPrice", price);

        return response;
    }

}
