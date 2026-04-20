package stock.cpastonedesign.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;    // org.jsoup 패키지
import org.jsoup.nodes.Element;     // org.jsoup 패키지
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsService {

    public List<Map<String, String>> getHeadlines() {

        //List->뉴스 여러개
        //Map->뉴스 제목, 링크, 언론사 등
        List<Map<String, String>> headlines = new ArrayList<>();

        //네이버 뉴스 경제
        String url = "https://news.naver.com/section/101";

        try {
            //네이버는 봇 거를 수도 있어서 브라우저라고 속이는것이 필요
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();

            //<li class="sa_item">
            Elements items = doc.select(".sa_item");

            for (int i = 0; i < 10; i++) { // 상위 10개만 추출
                Element item = items.get(i);
                Map<String, String> data = new HashMap<>();

                //class="sa_text_title _NLOG_IMPRESSION"
                Element titleAnchor = item.selectFirst(".sa_text_title"); //제목, 링크

                //<div class="sa_text_lede">
                Element ledeElement = item.selectFirst(".sa_text_lede"); //요약문

                //<div class="sa_text_press">
                Element pressElement = item.selectFirst(".sa_text_press"); //언론사

                if (titleAnchor != null) {
                    data.put("title", titleAnchor.text()); // 뉴스 제목
                    data.put("link", titleAnchor.attr("href")); // 뉴스의 네이버 주소, .attr("href"): href 속성에 적힌 네이버 뉴스 주소만 가져옵니다.
                    data.put("description", ledeElement != null ? ledeElement.text() : "");
                    data.put("press", pressElement != null ? pressElement.text() : "경제뉴스"); //언론사
                    headlines.add(data); //리스트에 추가
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return headlines;
    }
}
