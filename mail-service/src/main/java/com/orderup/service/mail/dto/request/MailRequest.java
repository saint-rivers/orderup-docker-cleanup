package com.orderup.service.mail.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MailRequest {  //request


	private String otpCode;

	private String to;

	private String from;

	private String subject;

}
