package org.example.highlighterdemo.feign.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor(staticName = "of")
public class RiotFeignInterceptor implements RequestInterceptor {
    @Value("${riot.api}")
    private String api;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if("GET".equals(requestTemplate.method())) {
            requestTemplate.query("api_key",api);
        }
    }
}
