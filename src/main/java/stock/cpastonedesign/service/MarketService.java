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
            //국내 지수 (코스피, 코스닥)
            Document siseDoc = Jsoup.connect("https://finance.naver.com/sise/").get(); //연결된 페이지의 HTML 소스 코드를 다 가져옴
            indices.add(extractSiseIndex(siseDoc, "#KOSPI_now", "#KOSPI_change", "코스피", "국내 · 한국")); //# : KOSPI_now가 ID인것을 알려주기 위해서
            indices.add(extractSiseIndex(siseDoc, "#KOSDAQ_now", "#KOSDAQ_change", "코스닥", "국내 · 한국"));

            //해외 지수 (다우, 나스닥, S&P500)
            indices.add(extractYahooIndex("%5EDJI", "다우 산업", "미국 · 지수"));
            indices.add(extractYahooIndex("%5EIXIC", "나스닥 종합", "미국 · 지수"));
            indices.add(extractYahooIndex("%5EGSPC", "S&P 500", "미국 · 지수"));


            indices.add(extractYahooIndex("%5EN225", "니케이 225", "일본 · 지수"));
            indices.add(extractYahooIndex("%5EGDAXI", "DAX", "독일 · 지수"));
            indices.add(extractYahooIndex("%5EFCHI", "CAC 40", "프랑스 · 지수"));
            indices.add(extractYahooIndex("%5EHSI", "항셍지수", "홍콩 · 지수"));
            indices.add(extractYahooIndex("000001.SS", "상하이 종합", "중국 · 지수"));

            //시장 지표 (환율, 유가, 금)
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
    //1. 국내 지수 전용 (코스피, 코스닥)
    private Map<String, Object> extractSiseIndex(Document doc, String valId, String changeId, String name, String label) {
        Map<String, Object> map = new HashMap<>();
        String value = doc.select(valId).isEmpty()
                ? "N/A"
                : doc.select(valId).first().text();
        String change = doc.select(changeId).isEmpty()
                ? "0%"
                : doc.select(changeId).first().text();

        java.util.regex.Matcher matcher =
                java.util.regex.Pattern.compile("([+-]?[0-9.]+%)").matcher(change);

        String percentChange = matcher.find()
                ? matcher.group(1)
                : "0.00%";

        map.put("name", name);
        map.put("label", label);
        map.put("value", value);
        map.put("change", percentChange);

        map.put("isUp", percentChange.startsWith("+"));

        return map;
    }

    //2. 해외 지수 전용
    /*private Map<String, Object> extractWorldIndex(Document doc, String targetName, String label) {
        Map<String, Object> map = new HashMap<>();

        Elements items = doc.select("ul.data_lst li");

        for (Element li : items) {

            String name = li.select("dt span.blind").text().replace(" ", "");
            String cleanTarget = targetName.replace(" ", "");

            if (name.contains(cleanTarget)) {
                map.put("name", targetName);
                map.put("label", label);

                // 현재 지수 값
                map.put("value", li.select("dd.point_status strong").text());

                // 변동값 (숫자)
                map.put("changeValue", li.select("dd.point_status em").text());

                // 등락률 (+0.34%)
                String sign = li.select("dd.point_status span > span").first().text();
                String percent = li.select("dd.point_status span").first().ownText();
                map.put("change", sign + percent);

                // 상승 여부
                map.put("isUp", li.select("dl").hasClass("point_up"));

                return map;
            }
        }

        return createErrorMap(targetName, label);
    }*/


    //3. 시장 지표 전용
    private Map<String, Object> extractMarketIndex(Document doc, String targetName, String label) {
        Map<String, Object> map = new HashMap<>();

        Elements items = doc.select("ul.data_lst li");

        for (Element li : items) {
            String name = li.select("h3.h_lst span.blind").text();
            if (name.equals(targetName)) {
                map.put("name", targetName);
                map.put("label", label);
                map.put("value", li.select("span.value").text());

                String change = li.select("span.change").text();

                // 숫자만 퍼센트 형태로 변환
                if (!change.contains("%")) {
                    change = change.replaceAll("[^0-9.-]", "");
                    if (!change.startsWith("-")) {
                        change="+"+change;
                    }
                    change+="%";
                }

                map.put("change", change);
                map.put("isUp", change.startsWith("+"));

                return map;
            }
        }

        return createErrorMap(targetName, label);
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

    //symbol:야후에서 사용하는 종목코드
    //name:화면에 보여줄 이름(다우산업)
    //label:분류 이름(미국 · 지수)
    private Map<String, Object> extractYahooIndex(String symbol, String name, String label) {
                Map<String, Object> map = new HashMap<>();

                try {
                    //야후 파이낸스 API 주소
                    String url = "https://query1.finance.yahoo.com/v8/finance/chart/" + symbol;

                    String json = Jsoup.connect(url) //url에 연결 준비
                            .ignoreContentType(true) //Jsoup는 크롤릴용이지만 HTML이 아니어도 가져와라
                            .execute() //서버에 Get 요청 보냄
                            .body(); //서버가 응답한 내용중 body만 꺼냄

                //regularMarketPrice 값 찾기
                    // java.util.regex.Pattern:정규식 패턴을 만드는 클래스
                    //compile: 찾을 규칙을 등록하는 것
                    //[0-9.] 이 안에 있는 문자 중 하나(0~9 숫자, 점 .), + 한 문자 이상 반복
                    //matcher(json):이 규칙을 json에 적용
                java.util.regex.Matcher priceMatcher = java.util.regex.Pattern.compile("\"regularMarketPrice\":([0-9.]+)").matcher(json);

                //previousClose 값 찾기
                java.util.regex.Matcher prevMatcher = java.util.regex.Pattern.compile("\"previousClose\":([0-9.]+)").matcher(json);

                if (priceMatcher.find() && prevMatcher.find()) {
                    //현재 가격
                    //group - ([0-9.]+)
                    //첫번째 괄호 안의 값
                    double current = Double.parseDouble(priceMatcher.group(1));
                    //전일 종가
                    double previous = Double.parseDouble(prevMatcher.group(1));

                    //퍼센트 계산
                    double percent = ((current - previous) / previous) * 100;

                    String change = String.format("%+.2f%%", percent);

                    map.put("name", name);
                    map.put("label", label);
                    map.put("value", String.format("%,.2f", current));
                    map.put("change", change);
                    map.put("isUp", percent >= 0);

                    return map;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        return createErrorMap(name, label);
    }

}