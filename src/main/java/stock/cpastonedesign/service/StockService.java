package stock.cpastonedesign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;


@Service
@RequiredArgsConstructor
public class StockService {

    private final RestTemplate restTemplate;

    // 본인 API KEY 넣기
    private final String API_KEY = "5VBPMFA5PKFHF0CY";

    public double getCurrentPrice(String ticker) {

        try {
            String url = "https://www.alphavantage.co/query" +
                            "?function=GLOBAL_QUOTE" +
                            "&symbol=" + ticker +
                            "&apikey=" + API_KEY;

            String response = restTemplate.getForObject(url, String.class);

            System.out.println(response);

            JSONObject json = new JSONObject(response);

            if (!json.has("Global Quote")) {
                System.out.println("API 제한 발생 또는 응답 실패");
                return -1;
            }

            JSONObject quote = json.getJSONObject("Global Quote");

            return Double.parseDouble(
                    quote.getString("05. price")
            );

        } catch (Exception e) {
            e.printStackTrace();

            // 실패 시 기본값
            return -1;
        }
    }
}