package org.zerock.crawlingdemo;

import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Element;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Log4j2
@SpringBootApplication
public class Crawling1108Application { //정적

	public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, IOException {

		TrustManager[] trustAllCertificates = new TrustManager[]{
				new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					public void checkClientTrusted(X509Certificate[] certs, String authType) {
					}

					public void checkServerTrusted(X509Certificate[] certs, String authType) {
					}
				}
		};

		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, trustAllCertificates, new java.security.SecureRandom());
		SSLContext.setDefault(sc); // 여기까진 고정

		String url = "https://comic.naver.com/webtoon/detail?titleId=826422&no=23&week=fri"; // 원하는 주소 넣기

		Document doc = Jsoup.connect(url).get(); // jsoup를 통해 doc 정보 가져오기

		Elements viewer = doc.select(".wt_viewer img"); // url의 html 코드를 분석해서 img가 포함되어있는 class를 넣고 그 이후 img 적기

		log.info(viewer);

		for(Element element : viewer) { // 많은 viewer를 for문으로 가져오기
			String imgSrcResult = element.attr("src");

//            String imgSrcResult = "https://www.orionworld.com/"+imgSrc; // 이건 사진에 따라서 주소창이 안붙을때 별도로 만들어주기. ex) 오리온

			log.info(imgSrcResult);

			String fileName = imgSrcResult.substring(imgSrcResult.lastIndexOf("/")+1); //

			log.info(fileName);

			URLConnection urlConnection = new URL(imgSrcResult).openConnection();

//            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Mobile Safari/537.36");

			InputStream inputStream = urlConnection.getInputStream();

			OutputStream outputStream = new FileOutputStream("C:\\snack\\w\\"+fileName); // 이미지 부분만 잘라서 가져오기

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