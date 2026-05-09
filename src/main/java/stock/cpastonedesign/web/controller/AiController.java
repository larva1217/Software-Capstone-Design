package stock.cpastonedesign.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import stock.cpastonedesign.service.AiService;
import stock.cpastonedesign.web.dto.MarketDataDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController //html이 아니라 JSON을 반환하는 컨트롤러
@RequestMapping("/api/ai") //이 클래스 안에 모든 API는 "/api/ai"로 시작
public class AiController {

    @Autowired
    private AiService aiService;

    //개별 주식 종목 분석
    @GetMapping(value = "/analyze", produces = "application/json; charset=UTF-8")
    public Map<String, String> analyze(@RequestParam String ticker) {
        String aiHtmlResult = aiService.getAiAnalysis(ticker);

        Map<String, String> response = new HashMap<>();
        response.put("analysisResult", aiHtmlResult);

        return response;
    }

    //국가 지수 및 시장 종합 분석
    @PostMapping(value = "/analyze-market", produces = "application/json; charset=UTF-8")
    public Map<String, String> analyzeMarket(@RequestBody List<MarketDataDto> marketDataList) {
        String aiHtmlResult = aiService.getMarketBriefing(marketDataList);

        Map<String, String> response = new HashMap<>();
        response.put("analysisResult", aiHtmlResult);

        return response;
    }
}
