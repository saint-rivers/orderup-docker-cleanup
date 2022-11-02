package com.orderup.service.mail.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
@Slf4j
public class RabbitMQConfig {


    /**
     * cache sessions and can optionally cache consumers and producers
     * cache : type of computer memory for storing information temporarily and getting to it quickly
     * 1, "store" limitations of the "HTTP session" stored in the server
     * 2, "optionally cache consumers and producers"
     */
    private final CachingConnectionFactory cachingConnectionFactory;




    // assign value in constructor
    public RabbitMQConfig(CachingConnectionFactory cachingConnectionFactory) {
        this.cachingConnectionFactory = cachingConnectionFactory;
    }




    // create queue for user registration
    @Bean
    public Queue createUserRegistrationQueue() {

        return QueueBuilder.durable("q.user-registration")  // create builder for queue with name and prevent losing queue
                .withArgument("x-dead-letter-exchange","x.registration-failure") //argument thar have
                .withArgument("x-dead-letter-routing-key","fall-back")
                .build();
    }



    // Every time a message consumption(consumer) throws an exception, the message is put back in the queue
    @Bean
    public RetryOperationsInterceptor retryInterceptor(){
        return RetryInterceptorBuilder.stateless().maxAttempts(3) //default behavior determines message identity based on messageId
                .backOffOptions(2000, 2.0, 100000)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
    }



    /**
     * 1, we need queues to be automatically declared and bounded
     * 2, "creating message listener containers by default"
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, cachingConnectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setAdviceChain(retryInterceptor());
        factory.setDefaultRequeueRejected(false);
        return factory;
    }





    // Creating multiple queues dynamically with FanoutExchange
    @Bean
    public Declarables createPostRegistrationSchema(){
        return new Declarables(
                new FanoutExchange("x.post-registration"),
                new Queue("q.send-email" ),
                new Queue("q.send-sms"),
                new Binding("q.send-email", Binding.DestinationType.QUEUE, "x.post-registration", "send-email", null),
                new Binding("q.send-sms", Binding.DestinationType.QUEUE, "x.post-registration", "send-sms", null)
        );
    }




    // Creating multiple queues dynamically with DirectExchange
    @Bean
    public Declarables createDeadLetterSchema(){
        return new Declarables(
            new DirectExchange("x.registration-failure"),
            new Queue("q.fall-back-registration"),
            new Binding("q.fall-back-registration", Binding.DestinationType.QUEUE,"x.registration-failure", "fall-back", null)
        );
    }




    //message converter : convert what we get ==> convert (as json object) ==> send  (as message)
    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }





    // exchange the object that given and send to the broker(provide storage for data produced)
    @Bean
    public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory);
        template.setMessageConverter(converter);
        return template;
    }



}
