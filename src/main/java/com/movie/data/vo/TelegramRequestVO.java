package com.movie.data.vo;

import org.apache.ibatis.type.Alias;	

/**
 * @ClassName 	: SndnReqBascVO.java
 * @author		: tsis
 * @date		: 2021.03.11
 * @description : 발송요청기본 VO
 * ===================================================
 * DATE			AUTHOR			NOTE
 * ---------------------------------------------------
 * 2021.03.11 		tsis 		최초생성
 */
@Alias("TelegramRequestVO")
public class TelegramRequestVO {
	
	private long chat_id;
	private String text;
	
	public long getChat_id() {
		return chat_id;
	}
	public void setChat_id(long chat_id) {
		this.chat_id = chat_id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}