package stock.cpastonedesign.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stock.cpastonedesign.service.AiService;

import java.util.HashMap;
import java.util.Map;

@RestController //html이 아니라 JSON을 반환하는 컨트롤러
@RequestMapping("/api/ai") //이 클래스 안에 모든 API는 "/api/ai"로 시작
public class AiController {

    @Autowired
    private AiService aiService;

    //GET /api/ai/analyze, 응답 타입 JSON, UTF-8
    @GetMapping(value = "/analyze", produces = "application/json; charset=UTF-8")
    public Map<String, String> analyze(@RequestParam String ticker) { // /api/ai/analyze?ticker=NVDA
        // AI 서비스 호출
        String aiHtmlResult = aiService.getAiAnalysis(ticker);

        // Map 객체를 리턴하면 @RestController 덕분에 자동으로 JSON으로 변환
        Map<String, String> response = new HashMap<>();
        response.put("analysisResult", aiHtmlResult);

        return response;

    }
}
