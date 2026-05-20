package stock.cpastonedesign.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;


@Service
@RequiredArgsConstructor //final이 붙은 변수들의 생성자 자동으로 만들어줌
@Slf4j
public class StockService {

    //외부 API 서버에 HTTP 요청을 보내는 객체
    private final RestTemplate restTemplate;

    //Alpha Vantage API 키
    private final String API_KEY = "PYF0T8PBMFWMUK42";

    //현재 주가 가져오는 함수
    public double getCurrentPrice(String ticker) {

        try {
            //AAPL이면,
            //https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=AAPL&apikey=PYF0T8PBMFWMUK42
            String url = "https://www.alphavantage.co/query" +
                            "?function=GLOBAL_QUOTE" +
                            "&symbol=" + ticker +
                            "&apikey=" + API_KEY;

            //해당 url로 GET 요청, 응답 데이터 문자열(JSON)로 받아옴
            String response = restTemplate.getForObject(url, String.class);
            log.info("API 응답 데이터 : {}", response);

            //문자열을 자바 객체 형태로 반환
            JSONObject json = new JSONObject(response);

            if (!json.has("Global Quote")) {
                log.error("API 제한 발생 또는 응답 실패");
                return -1;
            }

            //실제 가격 데이터 꺼내기
            JSONObject quote = json.getJSONObject("Global Quote");
            //가격 문자열 가져오기
            double price = Double.parseDouble(quote.getString("05. price"));
            log.info("{} 현재 주가 : {}", ticker, price);
            return price;

        } catch (Exception e) {
            log.error("주가 조회 중 오류 발생", e);
            return -1;
        }
    }
}

/*{
        "Global Quote": {
        "01. symbol": "AAPL",
        "05. price": "210.15"
        }
        } */