package org.example.highlighterdemo.feign.config;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;

import java.util.Date;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        log.error("Feign error : {} - Http : {}", s, response.status());

        if (response.status() == 404) {
            log.warn("Feign sample error : cannot find user");
            return new RetryableException(
                    response.status(),
                    "Retrying due to Http 404",
                    response.request().httpMethod(),
                    new Date(),
                    response.request()
            );
        }
        if (response.status() >= 500) {
            log.warn("Feign server error : retrying... ");
            return new RetryableException(
                    response.status(),
                    "Retrying due to Http 5xx",
                    response.request().httpMethod(),
                    new Date(),
                    response.request()
            );
        }
        return new CustomException(ErrorCode.SERVICE_UNAVAILABLE, response.reason());
    }
}
