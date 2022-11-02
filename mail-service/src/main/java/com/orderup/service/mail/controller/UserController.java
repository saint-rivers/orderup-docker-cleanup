package com.orderup.service.mail.controller;

import com.orderup.service.mail.dto.reponse.MailResponse;
import com.orderup.service.mail.dto.request.MailRequest;
import com.orderup.service.mail.service.exchange.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SecurityRequirement(name = "OrderUpTowerOAuth")
@Slf4j
@RestController
@RequestMapping(value = "/api/v1",
        produces = MediaType.APPLICATION_JSON_VALUE,   // produce message
        consumes = MediaType.APPLICATION_JSON_VALUE)   // consume message
public class UserController {
    private final RabbitTemplate rabbitTemplate;  // exchange object that given and send to the broken (provide storage data produced)
    private final EmailService emailService;


    // assign value in constructor
    public UserController(RabbitTemplate rabbitTemplate, EmailService emailService) {
        this.rabbitTemplate = rabbitTemplate;
        this.emailService = emailService;
    }


    @PostMapping("/mails")    //user register call when user request in account service
    @Operation(summary = "Send mail after registration")
    public MailResponse createUser(@RequestBody MailRequest request, HttpServletRequest httpServletRequest) throws MessagingException {
        log.info("Received request to create user: {}", request); // log message that produce
        //generate otpcode
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        var optCode = String.format("%06d", number);
        request.setOtpCode(optCode);

        //save otpcode
        emailService.saveVerificationCode(request);


        rabbitTemplate.convertAndSend("", "q.user-registration", request); // get the request ==> convert as (json) ==> send to broken

        var response = new MailResponse(); //create instance to assign value response


        Object status = httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);



        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // handle HTTP 404 Not Found error
                response.setStatus(Boolean.FALSE);
                response.setMessage("fail uploaded file ,the file can be jpg or png");

            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                // handle HTTP 403 Forbidden error
                response.setStatus(Boolean.FALSE);
                response.setMessage("you can't access this endpoint");

            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                // handle HTTP 500 Internal Server error
                response.setStatus(Boolean.FALSE);
                response.setMessage("internal server error");

            }

        }


        response.setMessage("អ៊ីមែល ត្រូវបានផ្ញើទៅកាន់ : " + request.getTo()); //user email
        response.setStatus(Boolean.TRUE);


        if(validate(request.getTo())==false){
            return new MailResponse("អ៊ីមែល របស់លោកអ្នកមិនត្រឺមត្រូវទេ", Boolean.FALSE);
        }else
        {
            /**
             *  {
             *     "message": "mail send to : dyvanrith@gmail.com",
             *     "status": true
             *  }
             */
            return new MailResponse(response.getMessage(), response.isStatus());
        }

    }



    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }



}
