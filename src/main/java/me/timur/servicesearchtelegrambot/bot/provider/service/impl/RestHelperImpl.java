package me.timur.servicesearchtelegrambot.bot.provider.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.timur.servicesearchtelegrambot.bot.provider.service.RestHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

/**
 * Created by Temurbek Ismoilov on 27/11/22.
 */

@Slf4j
@Component
public class RestHelperImpl implements RestHelper {
    private final static String BASE_URL = "https://api.telegram.org";
    @Value("${bot.client.token}")
    private String CURRENT_BOT_TOKEN_URL;
    @Value("${bot.client.token}")
    private String SEARCH_BOT_TOKEN_URL;

    @Override
    public void sendMessage(String chatId, String text) {
        final UriComponents uriComponents = uriBuilder()
                .path("/bot" + SEARCH_BOT_TOKEN_URL)
                .path("/sendMessage")
                .queryParam("chat_id", chatId)
                .queryParam("text", text)
                .build();

        get(uriComponents);
    }

    @Override
    public String getFilePath(String chatId, String fileId) {
        final UriComponents uriComponents = uriBuilder()
                .path("/bot" + CURRENT_BOT_TOKEN_URL)
                .path("/getFile")
                .queryParam("chat_id", chatId)
                .queryParam("file_id", fileId)
                .build();

        return get(uriComponents);
    }

    @Override
    public String downloadFile(String filePath) {
        final UriComponents uriComponents = uriBuilder()
                .path("/file")
                .path("/bot" + CURRENT_BOT_TOKEN_URL)
                .path("/" + filePath)
                .build();

        return get(uriComponents);
    }

    @Override
    public byte[] downloadFile2(String filePath) {
//        return new URL("https://api.telegram.org/file/bot"+CURRENT_BOT_TOKEN_URL+"/"+filePath).openStream();
        final UriComponents uriComponents = uriBuilder()
                .path("/file")
                .path("/bot" + CURRENT_BOT_TOKEN_URL)
                .path("/" + filePath)
                .build();

        return restTemplate()
                .getForEntity(uriComponents.toUriString(), byte[].class)
                .getBody();
    }

    @Override
    public void sendDocument(String chatId, Resource resource) {
        final UriComponents uriComponents = uriBuilder()
                .path("/bot" + SEARCH_BOT_TOKEN_URL)
                .path("/sendDocument")
                .queryParam("chat_id", chatId)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("document", resource);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        post(uriComponents, requestEntity);
    }

    @Override
    public void sendPhoto(String chatId, Resource resource) {
        final UriComponents uriComponents = uriBuilder()
                .path("/bot" + SEARCH_BOT_TOKEN_URL)
                .path("/sendPhoto")
                .queryParam("chat_id", chatId)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("photo", resource);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        post(uriComponents, requestEntity);
    }

    private String get(UriComponents uriComponents) {
        log.info("REQUEST: {}", uriComponents);
        final ResponseEntity<String> response = restTemplate().getForEntity(uriComponents.toUriString(), String.class);
        if (!uriComponents.getPath().contains("/file")) {
            log.info("RESPONSE: {}", response);
        }
        return response.getBody();
    }

    private String post(UriComponents uriComponents, HttpEntity requestEntity) {
        log.info("REQUEST: {}", uriComponents);
        final ResponseEntity<String> response = restTemplate().postForEntity(uriComponents.toUriString(),requestEntity, String.class);
        log.info("RESPONSE: {}", response);
        return response.getBody();
    }

    private RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .interceptors(new PlusEncoderInterceptor())
                .build();
    }

    private UriComponentsBuilder uriBuilder() {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.telegram.org");
    }

    static class PlusEncoderInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            return execution.execute(new HttpRequestWrapper(request) {
                @Override
                public URI getURI() {
                    URI u = super.getURI();
                    String strictlyEscapedQuery = StringUtils.replace(u.getRawQuery(), "+", "%2B");
                    return UriComponentsBuilder.fromUri(u)
                            .replaceQuery(strictlyEscapedQuery)
                            .build(true).toUri();
                }
            }, body);
        }
    }
}
