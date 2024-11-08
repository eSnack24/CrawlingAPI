package org.zerock.crawling1108;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;

@Log4j2
public class CrawlingDynamic {
    public static void main(String[] args) throws Exception {


        String path = "http://gs25.gsretail.com/gscvs/ko/products/event-goods";


        Document doc = Jsoup.connect(path).get();

        //CSRFTOKEN
        //System.out.println(doc);


        String token = doc.select("#CSRFForm").get(0).select("input[name='CSRFToken']").get(0).attr("value");

        log.info(token);

        //POST방식으로 행사상품 조회

        HashMap<String, String> params = new HashMap<>();
        params.put("pageNum", "1");
        params.put("pageSize", "8");
        params.put("parameterList", "ONE_TO_ONE");

        String target = "http://gs25.gsretail.com/gscvs/ko/products/event-goods-search?CSRFToken=" + token;

        log.info(target);

        Connection.Response response = Jsoup.connect(target)
                .method(Connection.Method.GET)
                .data(params)
                .header("Origin", "http://gs25.gsretail.com")
                .header("Referer", "http://gs25.gsretail.com/gscvs/ko/products/event-goods")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36") // 헤더 설정
                .execute();

        Document document = response.parse();

        log.info(document.body().text());

    }//end main
}//end class

