package com.orderup.service.mail.service.exchange;


import com.orderup.service.mail.dto.reponse.MailResponse;
import com.orderup.service.mail.dto.request.MailRequest;
import com.saintrivers.controltower.model.dto.AppUserDto;
import com.saintrivers.controltower.model.request.ActivationAttemptRequest;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@Component
public class EmailServiceImpl implements EmailService { //this class implementation stay in exchange processing
    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration config;

    private final WebClient userWebClient;

    public EmailServiceImpl(WebClient.Builder builder) {
        this.userWebClient = builder.baseUrl("http://192.168.96.7:8080").build();
    }

    // send mail in service processing
    public MailResponse sendEmail(MailRequest request, Map<String, Object> model) {
        MailResponse response = new MailResponse(); //create instance for response

        // This class represents a MIME style email message
        // Clients wanting to create new MIME style messages will instantiate an empty MimeMessage object and then
        // fill it with appropriate attributes and content.
        MimeMessage message = sender.createMimeMessage();

        try {
            // set mediaType
            // helper class for creating a MIME message. It offers support for inline elements
            // such as images, typical mail attachments and HTML text content
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            // add attachment as (png)
            helper.addAttachment("image/order.png", new ClassPathResource("image/order.png"));

            Template t = config.getTemplate("email-template.ftl"); // plain HTML file

            // text files that contain the desired output, except that they contain placeholders like ${name} , and even some ...
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model); //model (name user ,our location)

            helper.setTo(request.getTo());  // user email
            helper.setText(html, true); // user name & location
            helper.setSubject(request.getSubject()); // subject define
            helper.setFrom("kshrd.orderup@gmail.com"); //our email
            sender.send(message); // send this to


                response.setMessage("អ៊ីមែល ត្រូវបានផ្ញើទៅកាន់ : " + request.getTo()); // user email
                response.setStatus(Boolean.TRUE);


        } catch (MessagingException | IOException | TemplateException e) { // if false send
            response.setMessage("ផ្ញើអ៊ីមែល បារា៉ជ័យ : "+e.getMessage());
            response.setStatus(Boolean.FALSE);
        }

        //return it
        return response;
    }

    @Override
    public void saveVerificationCode(MailRequest request) {
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(20);
        LocalDateTime requestDate = LocalDateTime.now();
        Long otpCode = Long.valueOf(request.getOtpCode());
        String email = request.getTo();
//        System.out.println(getUserByEmail("1dae16f3-7353-4da6-a0fa-810c3d2b40fc"));
        UUID userId = getUserByEmail(email).getAuthId();
        ActivationAttemptRequest activationAttemptRequest = new ActivationAttemptRequest(userId, otpCode, requestDate, expirationDate);
        //save to activationattempt db
        insertActivationAttempt(activationAttemptRequest);
    }

    public AppUserDto getUserByEmail(String email){
        return userWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/users")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(AppUserDto.class).block();
    }
    public AppUserDto insertActivationAttempt(ActivationAttemptRequest activationAttemptRequest){
        System.out.println("actReq = "+activationAttemptRequest);
        return userWebClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/users/activation-attempt")
                        .build())
                .header("Accept", "application/json, text/plain")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(activationAttemptRequest))
                .retrieve()
                .bodyToMono(AppUserDto.class).block();
    }


}


