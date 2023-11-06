package com.movie.service;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.movie.data.vo.TelegramRequestVO;

@Service
public class TelegramMessageService extends TelegramLongPollingBot{
  
	@Value("${telegram.bot.access.token}")
    private String botToken;
	
	@Value("${telegram.bot.name}")
	private String botName;
	
	@Value("${telegram.bot.api.enable}")
	private boolean isApiEnable;
	
	@Value("${telegram.api.url}")
	private String apiUrl;
	
	@Value("${telegram.bot.chat.id}")
	private long chatId;
	
	private Logger logger = LogManager.getLogger(TelegramMessageService.class);
	
	@Override
	public void onUpdateReceived(Update arg0) {
		// TODO Auto-generated method stub
		 Message message = arg0.getMessage();
		 logger.info(message.toString());
	        if (arg0.hasMessage() && arg0.getMessage().hasText()) {
	            SendMessage sendMessage = new SendMessage();
	            sendMessage.setChatId(arg0.getMessage().getChatId().toString());
	            sendMessage.setText("자동 응답으로 보낼 메시지");
	            try {
					execute(sendMessage);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
	        }
	}

	@Override
	public String getBotUsername() {
		return botName;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}
	public void getTelegramChatId() throws Exception {
		String sndnUrl = apiUrl + botToken + "getUpdates";
		if(isApiEnable) {
			logger.debug("===================Telegram get ChatId start==========================");
	    	String returnStr = this.getUpdatePostRequest(sndnUrl);
	    	
		    logger.debug("returnStr : {}",returnStr);
		    logger.debug("===================Telegram get ChatId end==========================");
		}
	}	
	
	public void sndnTelegram(String replaceStr) throws Exception {
		String sndnUrl = apiUrl + botToken + "sendMessage";
		TelegramRequestVO vo = new TelegramRequestVO();
		vo.setText(replaceStr);
		if(isApiEnable) {
	    	logger.debug("Chat_Id : {}",chatId);
	    	vo.setChat_id(chatId);
	    	this.sendPostRequest(vo,sndnUrl);
	    	
		    logger.debug("===================Telegram Admin sndn end==========================");
		}
	}
	
	public String getUpdatePostRequest(String url) throws RuntimeException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<TelegramRequestVO> requestEntity = new HttpEntity<>(headers);

		int status = 0;
		long start = System.currentTimeMillis();
		String requestId = "[" + new SimpleDateFormat("yyyyMMddHHmmss").format(start) + "]";

		logger.info("{} start url={}", requestId, url);

		String result = "";

		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

			status = response.getStatusCode().value();
			result = response.getBody();

		} catch (final HttpClientErrorException e) {
			logger.error("{} statusCode={}, responseBody={}", requestId, e.getStatusCode(), e.getResponseBodyAsString());
			logger.error("{} message={}", requestId, "sendMessageTelegram");
			throw new RuntimeException("Telegram Noti Send Error.");
		} finally {
			logger.info("{} end in {} millisec. STATUS {}", requestId, System.currentTimeMillis() - start, status);
		}
		return result;
	}
	public void sendPostRequest(TelegramRequestVO body, String url) throws RuntimeException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		HttpEntity<TelegramRequestVO> requestEntity = new HttpEntity<>(body, headers);
		
		int status = 0;
		long start = System.currentTimeMillis();
		String requestId = "[" + new SimpleDateFormat("yyyyMMddHHmmss").format(start) + "]";
		
		logger.info("{} start url={}", requestId, url);
		
		
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			
			status = response.getStatusCode().value();
			
		} catch (final HttpClientErrorException e) {
			logger.error("{} statusCode={}, responseBody={}", requestId, e.getStatusCode(), e.getResponseBodyAsString());
			logger.error("{} message={}", requestId, body.toString());
			throw new RuntimeException("Telegram Noti Send Error.");
		} finally {
			logger.info("{} end in {} millisec. STATUS {}", requestId, System.currentTimeMillis() - start, status);
		}
	}
}


























