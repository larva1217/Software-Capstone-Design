package stock.cpastonedesign.service;

import lombok.extern.slf4j.Slf4j;
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

@Service
@Slf4j
public class AiService {

    //AI가 주고받는 데이터를 자바가 읽을 수 있게
    private final ObjectMapper objectMapper = new ObjectMapper();

    //application.properties에 설정한 키를 apiKey 변수에 집어넣기
    @Value("${google.gemini.api.key}")
    private String apiKey;


    //개별 주식 종목 분석
    public String getAiAnalysis(String ticker) {

        log.info("AI 개별 종목 분석 요청 : {}", ticker);

        String prompt =
                "당신은 월스트리트 트레이딩 데스크에서 30년 이상 근무한 수석 주식 애널리스트입니다.\n" +
                        "사용자가 '" + ticker + "' 종목에 대한 실시간 퀵 분석을 요청했습니다.\n\n" +

                        "다음 3가지 항목을 반드시 포함하세요.\n" +
                        "1) 기술적 분석\n" +
                        "2) 핵심 뉴스\n" +
                        "3) 투자 판단\n\n" +

                        "작성 규칙:\n" +
                        "- 반드시 HTML 형식으로 작성\n" +
                        "- Markdown(*, -, #) 사용 금지\n" +
                        "- 각 문장은 <br> 태그로 줄바꿈\n" +
                        "- 문단 사이는 <br><br> 사용\n" +
                        "- 제목은 <strong> 태그 사용\n" +
                        "- 상승 내용은 <span style=\"color:#ff4d4d;\">빨간색</span>\n" +
                        "- 하락 내용은 <span style=\"color:#00e676;\">초록색</span>\n\n" +

                        "📌 출력 포맷:\n" +
                        "<strong>📊 기술적 분석</strong><br>\n" +
                        "• 내용<br>\n" +
                        "• 내용<br><br>\n" +

                        "<strong>📰 핵심 뉴스</strong><br>\n" +
                        "• 내용<br>\n" +
                        "• 내용<br><br>\n" +

                        "<strong>💡 투자 판단</strong><br>\n" +
                        "<strong>[매수/관망/주의]</strong>: 이유";

        return callGeminiApi(prompt);
    }

    //국가 지수 및 시장 종합 분석
    public String getMarketBriefing(List<MarketDataDto> marketDataList) {

        log.info("시장 종합 브리핑 요청");

        //전달받은 시장 데이터를 프롬프트에 삽입할 문자열로 변환
        StringBuilder marketStatus = new StringBuilder();
        for (MarketDataDto data : marketDataList) {
            marketStatus.append(String.format("- %s: %s (%s)\n", data.getName(), data.getPrice(), data.getChangeRate()));
        }

        String prompt =
                "당신은 글로벌 매크로 헤지펀드의 수석 투자 전략가입니다.\n" +
                        "현재 시장 데이터를 바탕으로 종합 브리핑을 작성하세요.\n\n" +
                        "[현재 시장 데이터]\n" + marketStatus.toString() + "\n" +
                        "다음 3가지 항목을 포함하세요: 1)글로벌 동향 2)주요 특징(변동성 큰 지표) 3)투자 전략\n\n" +
                        "작성 규칙: 개조식 작성, <br><br> 줄바꿈 사용, <strong> 강조, " +
                        "긍정/상승은 <span style=\"color:#ff4d4d;\">빨간색</span>, 부정/하락은 <span style=\"color:#00e676;\">초록색</span> 적용.\n\n" +
                        "📌 출력 포맷:\n" +
                        "<strong>🌍 글로벌 시장 동향</strong><br>\n[내용]<br><br>\n" +
                        "<strong>🔍 주요 특징</strong><br>\n[내용]<br><br>\n" +
                        "<strong>💡 투자 전략</strong><br>\n<strong>[전략]</strong>: [이유]";

        return callGeminiApi(prompt);
    }

    //실제로 제미나이에게 물어보고 답변을 받아오는 기능
    private String callGeminiApi(String prompt) {

        log.info("Gemini API 호출 시작");

        //제미나이 주소
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        //인터넷을 연결해주는 가상의 웹 브라우저 켜기
        RestTemplate restTemplate = new RestTemplate();
        //HTTP 요청을 위한 RestTemplate 객체 생성
        HttpHeaders headers = new HttpHeaders();
        //HTTP 헤더 설정
        headers.setContentType(MediaType.APPLICATION_JSON);

        //제미나이 API가 요구하는 JSON 구조를 Map으로 생성
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", prompt);
        Map<String, Object> parts = new HashMap<>();
        parts.put("parts", List.of(textPart));
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(parts));

        //HTTP 요청 엔티티 생성
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            //구글 주소로 POST 보내고 응답 올 때까지 기다려서 응답 받기
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            //받은 JSON 문자열을 Jackson JSON 트리 모델 노드로 파싱
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            //AI가 말한 답변 글자만 리턴하기
            return rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
        } catch (HttpClientErrorException.TooManyRequests e) {
            log.error("Gemini API 제한 초과", e);
            return "잠시 후 다시 시도해주세요 (API 제한 초과)";
        } catch (Exception e) {
            log.error("Gemini 분석 중 오류 발생", e);
            return "분석 중 오류가 발생했습니다." + e.getMessage();
        }
    }

}
