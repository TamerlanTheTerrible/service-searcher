package me.timur.servicesearchtelegrambot.bot.provider.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Created by Temurbek Ismoilov on 19/03/23.
 */

@Slf4j
@Component
public class WebClientHelper {

    private WebClient client = WebClient.create("http://localhost:8080");

    public void send() {
        Mono<String> employeeMono = client.get()
                .uri("/employees/{id}", "1")
                .retrieve()
                .bodyToMono(String.class);}
}


