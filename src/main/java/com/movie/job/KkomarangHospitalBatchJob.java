package com.movie.job;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.movie.service.TelegramMessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KkomarangHospitalBatchJob {
	@Autowired
	TelegramMessageService service;
	private String url;
	
 	// 1. 드라이버 설치 경로
	@Value("${webdriver.chrome.driver}")
	private String WEB_DRIVER_ID;
	@Value("${webdriver.path}")
	private String WEB_DRIVER_PATH ;
	
	@Scheduled(cron = "${batch.scheduler.cron.hospital}")
	public void execute() throws Exception{
		
		log.debug("==============================================");
		log.debug("[WEB_DRIVER_ID]: " + WEB_DRIVER_ID);
		log.debug("[WEB_DRIVER_PATH]: " + WEB_DRIVER_PATH);
		
		this.loading();
		
		log.debug("==============================================");
	}
	
	
	public void loading (){
		// WebDriver 경로 설정
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		
		// 2. WebDriver 옵션 설정
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
//		options.addArguments("--disable-popup-blocking");
		
		WebDriver driver = new ChromeDriver(options);
		url = "http://sangmoohospital.co.kr/reservation/schedule_list.php?co_id=8010&year=2023&month=12";
		driver.get(url);
		String targetDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd"));
		///html/body/div/div/div[2]/div[2]/div[1]/table/tbody/tr[2]/td[6]/span/font 12/8
//		driver.findElements(By.xpath("/html/body/div/div/div[2]/div[2]/div[1]/table/tbody/tr[2]/td[3]/span/font"));
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[2]/div[1]/table/tbody/tr[2]/td[3]/span/a[1]")).click();;
//		this.inputDetail(driver);
		this.refresh(driver);
		
	}
	
	public void quit(WebDriver driver) {
		driver.quit();
	}
	public void inputDetail(WebDriver driver) {
		if(driver.getCurrentUrl().equals(url+"#")) {
			log.debug("currentUrl :{}",driver.getCurrentUrl());
			this.refresh(driver);
		}
		driver.findElement(By.id("reservation_name")).sendKeys("박소연");
		driver.findElement(By.id("reservation_patient_name")).sendKeys("박소연");
		driver.findElement(By.id("patient_same_check")).click();
		driver.findElement(By.id("reservation_phone1")).sendKeys("010");;
		driver.findElement(By.id("reservation_phone2")).sendKeys("4244");;
		driver.findElement(By.id("reservation_phone3")).sendKeys("4404");
		driver.findElement(By.id("reservation_password")).sendKeys("4404");
		driver.findElement(By.id("reservation_password_re")).sendKeys("4404");
		driver.findElement(By.id("agree1")).click();
		driver.findElement(By.id("agree2")).click();
		driver.findElement(By.className("btn_blue")).click();
//		this.clickOnAlert(driver);
	}
	public void refresh(WebDriver driver) {
		driver.navigate().refresh();
		this.inputDetail(driver);
	}
	
	public void clickOnAlert(WebDriver driver) {
        System.out.println("In click");
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }
}

