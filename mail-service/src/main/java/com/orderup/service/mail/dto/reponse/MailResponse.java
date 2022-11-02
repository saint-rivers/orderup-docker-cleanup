package com.orderup.service.mail.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailResponse implements Serializable {  //response mail (must be implements Serializable)

	private String message;

	private boolean status;

}
