package com.orderup.service.mail.service;

import com.orderup.service.mail.dto.request.MailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendSmsService { // stay in consumer processing

    //if mail was success it will display (subject/sms) that producer produce
    @RabbitListener(queues = "q.send-sms")
    public void sendSms(MailRequest request) {
        log.info("Sending sms to {} ", request.getSubject());
    }
}
