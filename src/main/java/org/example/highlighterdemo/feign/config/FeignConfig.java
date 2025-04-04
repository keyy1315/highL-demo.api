package org.example.highlighterdemo.feign.config;

import feign.Client;
import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
public class FeignConfig {
    @Bean
    public Client feignClient() {
        return new Client.Default(null, null);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    @Bean
    public RiotFeignInterceptor riotFeignInterceptor() {
        return RiotFeignInterceptor.of();
    }
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(
                100, SECONDS.toMillis(2),3
        );
    }
    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder();
    }
}
