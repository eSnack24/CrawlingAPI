package org.zerock.crawling1108;


import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class CrawlingSelenium  { //해태 보지마삼

    private static WebDriver driver;
    private static String url = "https://www.ht.co.kr/product/list?category=C00000&categoryNm=%EA%BB%8C";

    public static void main(String[] args) throws Exception {
        // 크롬 드라이버 경로 설정 (필요한 경우 경로를 설정)
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");

        driver = new ChromeDriver();

        try {
            getImageList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Thread.sleep(10000);
        // 드라이버 종료
        driver.quit();
    }

    private static void getImageList() throws InterruptedException, IOException {

        driver.get(url);    // 웹사이트로 이동

        Thread.sleep(3000);  // 페이지 로딩 완료 대기 (자바스크립트 동적 로딩 시간 확보)

        // 이미지 URL을 담을 리스트 생성
        List<String> imageUrls = new ArrayList<>();

        // 페이지에서 모든 이미지 태그를 찾기
        WebElement imagesDiv = driver.findElement(By.cssSelector("#productList_divContent"));

        log.info(imagesDiv.getAttribute("innerHTML"));


        List<WebElement> images = imagesDiv.findElements(By.cssSelector("img"));

        log.info("--------------------------1");
        log.info(images.size());

        for (WebElement image : images) {

            log.info("---------------------------2");

            String src = image.getDomAttribute("src");

            log.info(src);

            driver.get(src);

            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // 다른 이름으로 저장 (예: custom_screenshot.png)
            File destinationFile = new File("custom_screenshot.png");
            FileCopyUtils.copy(screenshotFile, destinationFile);



            break;

        }

        Thread.sleep(3000);




    }


}



