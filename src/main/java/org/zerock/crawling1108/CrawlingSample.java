package org.zerock.crawling1108;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class CrawlingSample { private static WebDriver driver;
    private static String url = "https://brand.nongshim.com/all_product/index?catCd=B00";

    public static void main(String[] args) throws Exception {
        // 크롬 드라이버 경로 설정 (필요한 경우 경로를 설정)
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");

        driver = new ChromeDriver();

        try {
            getImageList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 드라이버 종료
        driver.quit();
    }

    private static void  getImageList() throws InterruptedException, IOException {

        driver.get(url);    // 웹사이트로 이동
        Thread.sleep(3000);  // 페이지 로딩 완료 대기 (자바스크립트 동적 로딩 시간 확보)

        // 이미지 URL을 담을 리스트 생성
        List<String> imageUrls = new ArrayList<>();

        // 페이지에서 모든 이미지 태그를 찾기
        List<WebElement> images = driver.findElements(By.cssSelector(".contlist img"));


        for (WebElement image : images) {
            String imgSrc = image.getAttribute("src");  // 이미지의 src 속성 값 (URL) 추출

            if (imgSrc != null && !imgSrc.isEmpty()) {
                log.info("Found image URL: " + imgSrc);
                imageUrls.add(imgSrc);
            }

            // 최종적으로 크롤링한 이미지 URL 출력
            log.info("Total images found: " + imageUrls.size());

            String fileName = imgSrc.substring(imgSrc.lastIndexOf("/")+1);

            log.info(fileName);

            URLConnection urlConnection = new URL(imgSrc).openConnection();

            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 8.0.0; SM-G955U Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36");

            InputStream inputStream = urlConnection.getInputStream();

            OutputStream outputStream = new FileOutputStream("C:\\snack\\nongshim\\"+fileName); // 이미지 부분만 잘라서 가져오기

            byte[] buffer = new byte[1024*8]; // buffer를 통해 가져오기

            while (true) {
                int count = inputStream.read(buffer);
                if(count == -1) break;
                outputStream.write(buffer, 0, count);
            }

            outputStream.close();
            inputStream.close();

            log.info(inputStream.toString());


        }

    }
}



