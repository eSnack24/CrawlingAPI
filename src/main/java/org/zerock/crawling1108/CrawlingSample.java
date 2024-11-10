package org.zerock.crawling1108;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
public class CrawlingSample {
    private static WebDriver driver;
    private static String url = "https://brand.nongshim.com/all_product/index?catCd=B00";
    private static BufferedWriter writer;

    public static void main(String[] args) throws Exception {
        // 크롬 드라이버 경로 설정
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");

        driver = new ChromeDriver();

        // 메모장 파일을 열고 BufferedWriter 초기화
        File file = new File("C:\\snack\\nongshim\\product_inserts.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        writer = new BufferedWriter(new FileWriter(file));

        try {
            getImageList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 파일 작성 완료 후 닫기
        writer.close();

        // 드라이버 종료
        driver.quit();
    }

    private static void getImageList() throws InterruptedException, IOException {
        driver.get(url);    // 웹사이트로 이동
        Thread.sleep(3000);  // 페이지 로딩 완료 대기 (자바스크립트 동적 로딩 시간 확보)

        // 이미지 URL을 담을 리스트 생성
        List<String> imageUrls = new ArrayList<>();

        // 페이지에서 모든 이미지 태그를 찾기
        List<WebElement> images = driver.findElements(By.cssSelector(".contlist img"));

        // 각 이미지의 정보를 처리
        for (WebElement image : images) {
            String imgSrc = image.getAttribute("src");  // 이미지의 src 속성 값 (URL) 추출
            String imgalt = image.getAttribute("alt");  // 이미지의 alt 속성 값 (제품명)

            // SQL 구문을 메모장에 기록


            // 이미지 URL을 출력
            if (imgSrc != null && !imgSrc.isEmpty()) {
                log.info("Found image URL: " + imgSrc);
                imageUrls.add(imgSrc);

                // 이미지 파일 이름 추출
                String fileName = UUID.randomUUID().toString() + "_" + imgSrc.substring(imgSrc.lastIndexOf("/") + 1);
                log.info("Saving image with filename: " + fileName);

                if (imgalt != null && !imgalt.isEmpty()) {
                    String insertSQL = "insert into product (ptitle, pcontent, price, pfilename) values ('" + imgalt + "', 'pcontent', 4000, '" +fileName+"');";
                    writer.write(insertSQL);
                    writer.newLine();  // 줄바꿈 추가
                }

                // 이미지 다운로드
                URLConnection urlConnection = new URL(imgSrc).openConnection();
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 8.0.0; SM-G955U Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36");

                InputStream inputStream = urlConnection.getInputStream();
                OutputStream outputStream = new FileOutputStream("C:\\snack\\nongshim\\" + fileName); // 이미지 파일 저장

                byte[] buffer = new byte[1024 * 8]; // buffer를 통해 가져오기
                while (true) {
                    int count = inputStream.read(buffer);
                    if (count == -1) break;
                    outputStream.write(buffer, 0, count);
                }

                outputStream.close();
                inputStream.close();
            }
        }

        // 최종적으로 크롤링한 이미지 URL 개수 출력
        log.info("Total images found: " + imageUrls.size());
    }
}
