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
public class TelegramGetChatUpdateJob {
	@Autowired
	TelegramMessageService service;
	
	
	@Scheduled(cron = "${batch.scheduler.cron.getchatid}")
	public void execute() throws Exception{
		String targetDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		log.debug("==============================================");
		
		
		service.getTelegramChatId();
		
		
		log.debug("==============================================");
	}

}

