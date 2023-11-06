package com.movie.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
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
public class KkomarangYakwaBatchJob {
	@Autowired
	TelegramMessageService service;
	private String url;
	
 	// 1. 드라이버 설치 경로
	@Value("${webdriver.chrome.driver}")
	private String WEB_DRIVER_ID;
	@Value("${webdriver.path}")
	private String WEB_DRIVER_PATH ;
	
	@Scheduled(cron = "${batch.scheduler.cron.yaketing}")
	public void execute() throws Exception{
		
		log.debug("==============================================");
		log.debug("[WEB_DRIVER_ID]: " + WEB_DRIVER_ID);
		log.debug("[WEB_DRIVER_PATH]: " + WEB_DRIVER_PATH);
		
		this.loading();
//		this.naverLoading();
		
		log.debug("==============================================");
	}
	
	

	public void naverLoading () throws InterruptedException{
		// WebDriver 경로 설정
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		
		// 2. WebDriver 옵션 설정
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
//		options.addArguments("--disable-popup-blocking");
		
		WebDriver driver = new ChromeDriver(options);
		url = "https://www.naver.com/";
		driver.get(url);
		
		Date expiry = driver.manage().getCookieNamed("NNB").getExpiry();
		String domain = driver.manage().getCookieNamed("NNB").getDomain();
		String nidAut_name = "NID_AUT";
		String nidAut_value = "1kAbluSUz+MHrCl89RM+NCXc/pwSdmpToIZZgei//d+XvpPDQRA7crFRoCu7O/Zq";
		String nidSes_name = "NID_SES";
		String nidSes_value = "AAABvin63n0IdVU38ioMZzpxAf5o9YYjG2MQCrR1ymKcvZ3sS52Z8UF2qrOQFfCb9rbhEangtRqefBVX0fztJEA5TstrRe+mfgeT1qeUxgMi4VDhUxnWTrl/pYAZt3h323JHkvZUKoci652ewsjYWPkYAny5FXA3wQmCU+Vllr534mRZhOpT2BZaz/nQ4Xz1xVhn07VImrjAbI8mzo1lRuEzynCeak2xpJxrSYNJSGQTH5leww1/lR/4vd2Fd+hELxioRI74Jrt4y992+oUH6qPrD39HqZnd3LfqD6p3LExZVPsNHRJUJRT7rGtDrCrYwDokLpBzBr2w6/+QaUw6RAvlbhl/jD1HINLg0mNVPCzwyr/Rau9pK9v7MbY+fhBFtBHXnNlTvb7xkbilWTlqnSkMSu2UY5rEvf8IYtWHNfgVCFtlFQGqa7sKzmE4WNk/Bv/TwCdZmWwjD+1e3xAeFnh8xMKzQg8ecuFdufEfbAyPr5qtgDoUoV/uvVKrYmh89Zb0mCrhoSPVNnNplAl5XLqvez/+oxHZRhbM0+t8FfvcxKeAZQNNKzbHv1yotS06e3z4Jt3fuDF4aQeVq0FNrYnTbxU=";
		String path = "/";
		
		Cookie NID_AUT = new Cookie(nidAut_name, nidAut_value, domain, path, expiry,true);
		Cookie NID_SES = new Cookie(nidSes_name,nidSes_value, domain, path, expiry,true);
		driver.manage().addCookie(NID_AUT);
		driver.manage().addCookie(NID_SES);
		
		driver.navigate().refresh();

		url = "https://m.pay.naver.com/o/products/510440774/6515739393/purchase?from=https://m.pay.naver.com/";
		driver.get(url);
		this.clickOnAlert(driver);
		
		
	}
	
	public void loading (){
		// WebDriver 경로 설정
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		
		// 2. WebDriver 옵션 설정
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
//		options.addArguments("--disable-popup-blocking");
		
		WebDriver driver = new ChromeDriver(options);
		
		url = "http://janginthe.com/member/login.html";
		driver.get(url);
		
		WebElement id = driver.findElement(By.id("member_id"));
		id.sendKeys("kke9307");
		WebElement pwd = driver.findElement(By.id("member_passwd"));
		pwd.sendKeys("2tlqkfshadk!");
		WebElement submit = driver.findElement(By.className("btn_login"));
		submit.submit();
		this.searchYakwa(driver);
		
	}
	
	public void quit(WebDriver driver) {
		driver.quit();
	}
	public void searchYakwa(WebDriver driver) {
		if(!"http://janginthe.com/myshop/wish_list.html".equals(driver.getCurrentUrl())) {
			driver.get("http://janginthe.com/myshop/wish_list.html");
		}
		boolean openYN = driver.findElement(By.className("btn_gsmall")).isDisplayed();
		if(openYN) {
			driver.findElement(By.className("btn_gsmall")).click();
			driver.findElement(By.xpath("//*[@id=\"contents\"]/div[3]/table/tbody/tr/td[9]/a[1]")).click();
		}else {
			this.refresh(driver);
		}
	}
	public void refresh(WebDriver driver) {
		driver.navigate().refresh();
		this.searchYakwa(driver);
	}
	
	public void clickOnAlert(WebDriver driver) {
        System.out.println("In click");
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }
}

