package stock.cpastonedesign.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import org.jsoup.Jsoup;           // Jsoup 클래스 불러오기
import org.jsoup.nodes.Document;  // Jsoup 전용 Document 불러오기
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarketService {

    //코스피,나스닥,환율 등 여러 정보를 리스트로 묶어서 반환
    public List<Map<String, Object>> getLiveIndices() {
        List<Map<String, Object>> indices = new ArrayList<>();

        try {
            // 1. 국내 지수 (코스피, 코스닥)
            Document siseDoc = Jsoup.connect("https://finance.naver.com/sise/").get(); //연결된 페이지의 HTML 소스 코드를 다 가져옴
            indices.add(extractSiseIndex(siseDoc, "#KOSPI_now", "#KOSPI_change", "코스피", "국내 · 한국")); //# : KOSPI_now가 ID인것을 알려주기 위해서
            indices.add(extractSiseIndex(siseDoc, "#KOSDAQ_now", "#KOSDAQ_change", "코스닥", "국내 · 한국"));

            // 2. 해외 지수 (다우, 나스닥, S&P500)
            Document worldDoc = Jsoup.connect("https://finance.naver.com/world/").get();
            indices.add(extractWorldIndex(worldDoc, "다우 산업", "미국 · 지수"));
            indices.add(extractWorldIndex(worldDoc, "나스닥 종합", "미국 · 지수"));
            indices.add(extractWorldIndex(worldDoc, "S&P 500", "미국 · 지수"));

            // 3. 시장 지표 (환율, 유가, 금)
            Document marketDoc = Jsoup.connect("https://finance.naver.com/marketindex/").get();
            indices.add(extractMarketIndex(marketDoc, "미국 USD", "환율 · 시장"));
            indices.add(extractMarketIndex(marketDoc, "WTI", "에너지 · 원자재"));
            indices.add(extractMarketIndex(marketDoc, "국제 금", "금속 · 원자재"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return indices;
    }

    //1. 국내 지수 전용 (코스피, 코스닥)
    //Document doc : Jsoup.connect로 긁어온 전체 페이지 데이터
    //valId, changeId : 찾고 싶은 값의 주소
    //name, label : 나중에 알아보기 쉽게 붙여준 이름
    private Map<String, Object> extractSiseIndex(Document doc, String valId, String changeId, String name, String label) {
        Map<String, Object> map = new HashMap<>();
        String value = doc.select(valId).isEmpty() ? "N/A" : doc.select(valId).first().text(); //.first(): 찾은 태그 중에서 첫번째 것을 고른다, .text() : 글자만 읽는다
        String change = doc.select(changeId).isEmpty() ? "0" : doc.select(changeId).first().text();

        map.put("name", name);
        map.put("label", label);
        map.put("value", value);
        map.put("change", change);
        //change라는 글자 안에 하락 포함X or 상승 포함 or +기호 포함 -> true
        map.put("isUp", !change.contains("하락") && (change.contains("상승") || change.contains("+")));
        return map;
    }

    //2. 해외 지수 전용
    private Map<String, Object> extractWorldIndex(Document doc, String targetName, String label) {
        Map<String, Object> map = new HashMap<>();

        //Elements : Jsoup에서 제공하는 “여러 개의 HTML 태그 묶음” 타입
        //ul class="data_lst" 안에 있는 모든 li 태그 선택
        Elements items = doc.select("ul.data_lst li");

        for (Element li : items) {

            //li태그 안에 있는 dt태그에 class="blind"인 span 찾기
            String name = li.select("dt span.blind").text().replace(" ", ""); //공백 제거
            String cleanTarget = targetName.replace(" ", ""); // 찾으려는 이름도 공백 제거

            if (name.contains(cleanTarget)) {
                map.put("name", targetName);
                map.put("label", label);
                //li태그 안에 <dd class="point_status"> 안에 strong 태그 글자 가져오기
                map.put("value", li.select("dd.point_status strong").text());
                String change = li.select("dd.point_status span").first().text();
                map.put("change", change);
                //dl태그 안에 class="point_up" 있으면 상승 -> true
                map.put("isUp", li.select("dl").hasClass("point_up"));
                return map;
            }
        }
        return createErrorMap(targetName, label); //못 찾으면 기본값 반환
    }


    //3. 시장 지표 전용
    private Map<String, Object> extractMarketIndex(Document doc, String targetName, String label) {
        Map<String, Object> map = new HashMap<>();

        //ul class="data_lst" 안에 있는 li 전부 가져오기
        Elements items = doc.select("ul.data_lst li");

        for (Element li : items) {

            //<h3 class="h_lst">
            //    <span class="blind">미국 USD</span>
            //</h3>
            String name = li.select("h3.h_lst span.blind").text();

            if (name.equals(targetName)) {
                map.put("name", targetName);
                map.put("label", label);
                map.put("value", li.select("span.value").text());
                map.put("change", li.select("span.change").text());
                //<div class="head_info point_up"> or <div class="head_info point_dn">
                //point_up 있으면 true 상승
                map.put("isUp", li.select("div.head_info").hasClass("point_up"));
                return map;
            }
        }
        return createErrorMap(targetName, label); //못 찾으면 기본값 반환
    }

    private Map<String, Object> createErrorMap(String name, String label) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("label", label);
        map.put("value", "N/A");
        map.put("change", "0");
        map.put("isUp", true);
        return map;
    }

    public double getRealTimePrice(String symbol) {

        //symbol에 ":" 있으면 뒤쪽만 사용
        //"NASDAQ:NVDA" -> ["NASDAQ", "NVDA"] -> "NVDA"
        String code = symbol.contains(":") ? symbol.split(":")[1] : symbol;

        try {
            // 야후 파이낸스 API 경로
            String url = "https://query1.finance.yahoo.com/v8/finance/chart/" + code;

            // Jsoup으로 JSON 데이터 통째로 가져오기
            String json = Jsoup.connect(url)
                    .ignoreContentType(true) //HTML이 아니어도 가져와라, 원래 Jsoup는 HTML 분석용
                    .execute().body(); //execute():서버에 요청 보내기, body():응답에서 바디만 꺼내기


            //"regularMarketPrice":숫자 형태를 찾기 위한 정규식 패턴 생성
            //([0-9.]+):숫자(0~9) + 점(.)이 하나 이상 반복되는 것 group(1)로 꺼내는 용도
            //json 문자열에서 검색할 수 있도록 Matcher에 연결
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\"regularMarketPrice\":([0-9.]+)").matcher(json);

            if (matcher.find()) {
                return Double.parseDouble(matcher.group(1)); //group(1) : ()안에 있는 값
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 124.58; // 실패 시 마지막 NVDA 종가
    }
}