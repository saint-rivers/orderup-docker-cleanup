package com.orderup.service.mail.service.exchange;


import com.orderup.service.mail.dto.reponse.MailResponse;
import com.orderup.service.mail.dto.request.MailRequest;


import java.util.Map;
import java.util.UUID;

public interface EmailService {

    MailResponse sendEmail(MailRequest request, Map<String, Object> model);

    void saveVerificationCode(MailRequest request);


}


