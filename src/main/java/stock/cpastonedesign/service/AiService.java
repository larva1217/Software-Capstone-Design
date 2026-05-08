package stock.cpastonedesign.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
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

    @SuppressWarnings("unchecked")
    public String getAiAnalysis(String ticker) {  //ticker:사용자가 조회하고자 하는 주식 종목 이름(예:APPL)

        //구글 Gemini AI 서버 주소
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        //Spring에서 HTTP 요청 보내는 객체
        RestTemplate restTemplate = new RestTemplate();

        //HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();

        //서버에게 JSON으로 보낸다고 알려주는 것
        headers.setContentType(MediaType.APPLICATION_JSON);

        //제미나이에게 보낼 프롬프트
        String prompt =
                "당신은 월스트리트 트레이딩 데스크에서 10년 이상 근무한 수석 주식 애널리스트입니다.\n" +
                        "사용자가 '" + ticker + "' 종목에 대한 실시간 퀵 분석을 요청했습니다.\n\n" +

                        "🚨 다음 3가지 항목을 반드시 포함하여 분석하세요:\n\n" +

                        "1) 📊 기술적 분석: 현재 가격 추세와 단기 핵심 지지선/저항선을 1~2문장으로 요약\n" +
                        "2) 📰 핵심 뉴스: 현재 주가 변동을 이끄는 가장 중요한 촉매제 뉴스 1~2개\n" +
                        "3) 💡 투자 판단: '매수', '관망', '주의(매도)' 중 하나를 명확히 선택하고, 핵심 이유 1줄 제시\n\n" +

                        "🔥 작성 규칙 (엄격히 준수할 것):\n" +
                        "- 설명하는 어투(~습니다, ~합니다)를 피하고, 개조식(명사형 종결)으로 짧고 강렬하게 끊어 쓰세요.\n" +
                        "- 항목 간 구분이 확실하게 되도록 반드시 줄바꿈 태그(<br><br>)를 사용하세요.\n" +
                        "- 중요한 키워드는 <strong> 태그로 강조하세요.\n" +
                        "- 상승/긍정적인 단어는 <span style=\"color:#ff4d4d;\">텍스트</span> (빨간색),\n" +
                        "  하락/부정적/변동성 단어는 <span style=\"color:#00e676;\">텍스트</span> (초록색)으로 강조하세요.\n\n" +

                        "📌 출력 포맷 (아래 구조를 그대로 복사해서 내용만 채울 것):\n" +
                        "<strong>📊 기술적 분석</strong><br>\n" +
                        "[내용 작성]<br><br>\n" +
                        "<strong>📰 핵심 뉴스</strong><br>\n" +
                        "[내용 작성]<br><br>\n" +
                        "<strong>💡 투자 판단</strong><br>\n" +
                        "<strong>[매수/관망/주의]</strong>: [이유 작성]";

        //{"text": "프롬프트"}
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", prompt);

        //{"parts": [{"text": "프롬프트"}]}
        Map<String, Object> parts = new HashMap<>();
        parts.put("parts", List.of(textPart));

        //{"contents":[{"parts":[{"text": "프롬프트"}]}]}, Gemini API가 요구하는 기본 포맷
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(parts));


        //HTTP 요청, 보낼 데이터 + 헤더
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);


        try {
            //POST요청으로 제미나이 서버에 보냄
            //String.class → 응답 JSON을 String으로 받겠다는 뜻
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            String responseBodyString = response.getBody();

            //JSON->트리구조로 파싱
            JsonNode rootNode = objectMapper.readTree(responseBodyString);

            String resultText = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            return resultText;

        } catch (HttpClientErrorException.TooManyRequests e) {
            System.out.println("API 호출 한도 초과");
            System.out.println("잠시 후 다시 시도하세요");

            return "잠시 후 다시 시도해주세요 (API 제한 초과)";
        }
    }
}
