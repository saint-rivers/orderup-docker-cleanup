package com.orderup.service.mail.service;

import com.orderup.service.mail.dto.request.MailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FallBackRegistrationService {  // stay in queue processing


    //fail with rabbitmq (if processor error) false to produce
    @RabbitListener(queues = {"q.fall-back-registration"})
    public void onRegistrationFailure(MailRequest failedRegistration){
        log.info("Executing fallback for failed registration {}", failedRegistration);
    }

}
