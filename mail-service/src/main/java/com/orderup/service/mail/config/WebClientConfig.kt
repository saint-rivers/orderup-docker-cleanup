package com.orderup.service.mail.config

import com.netflix.discovery.EurekaClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
//    val eurekaClient: EurekaClient
) {

    @Bean("UserWebClient")
    @LoadBalanced
    fun userWebClient(): WebClient = WebClient
        .builder()
        .baseUrl("http://192.168.96.7:8080")
        .build()
}