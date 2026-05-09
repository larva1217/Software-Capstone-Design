package stock.cpastonedesign.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import stock.cpastonedesign.web.dto.MarketDataDto;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service //비즈니스 로직을 처리하는 서비스
public class AiService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    //application.properties에 설정한 키를 apiKey 변수에 집어넣기
    @Value("${google.gemini.api.key}")
    private String apiKey;


    // 개별 주식 종목 분석
    public String getAiAnalysis(String ticker) {
        String prompt =
                "당신은 월스트리트 트레이딩 데스크에서 10년 이상 근무한 수석 주식 애널리스트입니다.\n" +
                        "사용자가 '" + ticker + "' 종목에 대한 실시간 퀵 분석을 요청했습니다.\n\n" +
                        "🚨 다음 3가지 항목을 포함하세요: 1) 📊 기술적 분석 2) 📰 핵심 뉴스 3) 💡 투자 판단\n\n" +
                        "🔥 작성 규칙: 개조식 작성, <br><br> 줄바꿈 사용, <strong> 강조, " +
                        "상승은 <span style=\"color:#ff4d4d;\">빨간색</span>, 하락은 <span style=\"color:#00e676;\">초록색</span> 적용.\n\n" +
                        "📌 출력 포맷:\n" +
                        "<strong>📊 기술적 분석</strong><br>\n[내용]<br><br>\n" +
                        "<strong>📰 핵심 뉴스</strong><br>\n[내용]<br><br>\n" +
                        "<strong>💡 투자 판단</strong><br>\n<strong>[매수/관망/주의]</strong>: [이유]";

        return callGeminiApi(prompt);
    }

    // 국가 지수 및 시장 종합 분석
    public String getMarketBriefing(List<MarketDataDto> marketDataList) {
        StringBuilder marketStatus = new StringBuilder();
        for (MarketDataDto data : marketDataList) {
            marketStatus.append(String.format("- %s: %s (%s)\n", data.getName(), data.getPrice(), data.getChangeRate()));
        }

        String prompt =
                "당신은 글로벌 매크로 헤지펀드의 수석 투자 전략가입니다.\n" +
                        "현재 시장 데이터를 바탕으로 종합 브리핑을 작성하세요.\n\n" +
                        "[현재 시장 데이터]\n" + marketStatus.toString() + "\n" +
                        "🚨 다음 3가지 항목을 포함하세요: 1) 🌍 글로벌 동향 2) 🔍 주요 특징(변동성 큰 지표) 3) 💡 투자 전략\n\n" +
                        "🔥 작성 규칙: 개조식 작성, <br><br> 줄바꿈 사용, <strong> 강조, " +
                        "긍정/상승은 <span style=\"color:#ff4d4d;\">빨간색</span>, 부정/하락은 <span style=\"color:#00e676;\">초록색</span> 적용.\n\n" +
                        "📌 출력 포맷:\n" +
                        "<strong>🌍 글로벌 시장 동향</strong><br>\n[내용]<br><br>\n" +
                        "<strong>🔍 주요 특징</strong><br>\n[내용]<br><br>\n" +
                        "<strong>💡 투자 전략</strong><br>\n<strong>[전략]</strong>: [이유]";

        return callGeminiApi(prompt);
    }

    //공통 Gemini API 호출 로직
    private String callGeminiApi(String prompt) {



        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", prompt);
        Map<String, Object> parts = new HashMap<>();
        parts.put("parts", List.of(textPart));
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(parts));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            return rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
        } catch (HttpClientErrorException.TooManyRequests e) {
            return "잠시 후 다시 시도해주세요 (API 제한 초과)";
        } catch (Exception e) {
            return "분석 중 오류가 발생했습니다." + e.getMessage();
        }
    }

}
