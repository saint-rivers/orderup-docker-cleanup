package com.orderup.service.mail.service;

import com.orderup.service.mail.dto.request.MailRequest;
import com.orderup.service.mail.service.exchange.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserRegistrationListener {  // stay in consumer processing
    private final RabbitTemplate rabbitTemplate;

    public UserRegistrationListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    EmailService emailService;   // use to call the method with mail and send it

    @RabbitListener(queues = {"q.user-registration"})
    public void onUserRegistration(MailRequest request) throws MessagingException { // user success request
        log.info("User Registration Event Received: {}", request);

        Map<String, Object> model = new HashMap<>();   //create to assign and get to template
        model.put("Name", request.getOtpCode()); //value that assign to template (name user)
        model.put("location", "ToulKouk,Cambodia"); // static our website (location)

        emailService.sendEmail(request,model);  // model (user name & location website)

        rabbitTemplate.convertAndSend("x.post-registration","", request);
    }

}
