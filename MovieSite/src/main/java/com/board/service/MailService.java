package com.board.service;


import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface MailService {
	
	// 메일 내용 작성
	MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException;
	
	// 메일 발송
	String sendEmail(String email) throws MessagingException, UnsupportedEncodingException;
	
	// 랜덤 인증 코드 랜덤 생성
	public void createCode();
}
