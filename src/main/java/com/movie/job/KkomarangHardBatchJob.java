package com.movie.job;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.movie.service.TelegramMessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KkomarangHardBatchJob {
	
	@Autowired
	TelegramMessageService service;
	private String url;
	
 	// 1. 드라이버 설치 경로
	@Value("${webdriver.chrome.driver}")
	private String WEB_DRIVER_ID;
	@Value("${webdriver.path}")
	private String WEB_DRIVER_PATH ;
	
	@Scheduled(cron = "${batch.scheduler.cron.movie}")
	public void execute() throws Exception{
		String targetDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		log.debug("==============================================");
		log.debug("[WEB_DRIVER_ID]: " + WEB_DRIVER_ID);
		log.debug("[WEB_DRIVER_PATH]: " + WEB_DRIVER_PATH);
		
		
		this.loading(targetDate);
		
		
		log.debug("==============================================");
	}
	
	

	public void loading (String targetDate){
		// WebDriver 경로 설정
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
				
		// 2. WebDriver 옵션 설정
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		options.addArguments("--disable-popup-blocking");
        
		WebDriver driver = new ChromeDriver(options);
		
		url = "https://www.cgv.co.kr/user/login/";
		driver.get(url);
		WebElement id = driver.findElement(By.id("txtUserId"));
		id.sendKeys("kke9307");
		WebElement pwd = driver.findElement(By.id("txtPassword"));
		pwd.sendKeys("2tlqkfshadk");
		WebElement submit = driver.findElement(By.id("submit"));
		submit.submit();
		if(!"https://www.cgv.co.kr/".equals(driver.getCurrentUrl())) {
			driver.get("http://www.cgv.co.kr/theaters/?areacode=01&theaterCode=0013&date="+targetDate);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.getMovieList(driver,targetDate);
		}
//		this.quit(driver);
	}
	
	public void quit(WebDriver driver) {
		driver.quit();
	}
	public void getMovieList(WebDriver driver,String targetDate) {
		WebElement iframe = driver.findElement(By.xpath("//*[@id=\"ifrm_movie_time_table\"]"));
		driver.switchTo().frame(iframe);
		WebElement showList = driver.findElement(By.className("sect-showtimes"));
		List<WebElement> mvList = showList.findElements(By.className("col-times"));
		String str = "오늘의 용아맥 영화일정 ["+targetDate+"]\n";
		for(WebElement mv : mvList) {
			WebElement mvNM = mv.findElement(By.className("info-movie"));
			List<WebElement> hallList  = mv.findElements(By.className("type-hall"));
			for(WebElement info : hallList) {
				WebElement time = info.findElement(By.className("info-hall"));
				List<WebElement> hall = time.findElement(By.tagName("ul")).findElements(By.tagName("li"));
				if("IMAX LASER 2D(자막)".equals(hall.get(0).getText())) {
					List<WebElement>timetable = info.findElement(By.className("info-timetable")).findElement(By.tagName("ul")).findElements(By.tagName("li"));
					WebElement mvName = mvNM.findElement(By.tagName("strong"));
					String append = "";
					for(WebElement tt : timetable) {
						log.debug("time: {}", tt.findElement(By.tagName("a")).getText());
						append += tt.findElement(By.tagName("a")).getAttribute("href")+"\n" + tt.findElement(By.tagName("a")).getText();
					}
					str += "♡"+mvName.getText()+"\n" + append + "\n";
				}
			}
		}
		
		
		try {
			service.sndnTelegram(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

