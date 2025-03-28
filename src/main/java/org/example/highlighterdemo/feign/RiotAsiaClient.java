package org.example.highlighterdemo.feign;

import lombok.extern.slf4j.Slf4j;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.feign.config.FeignConfig;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "riot-asia-service", url = "https://asia.api.riotgames.com",
        configuration = FeignConfig.class, fallbackFactory = RiotAsiaClient.PuuidFallbackFactory.class)
public interface RiotAsiaClient {
    @GetMapping("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}")
    ResponseEntity<Map<String, String>> getPUuid(@PathVariable("gameName") String gameName,
                                                 @PathVariable("tagLine") String tagLine);

    @Component
    class PuuidFallbackFactory implements FallbackFactory<FallbackWithFactory> {

        @Override
        public FallbackWithFactory create(Throwable cause) {
            return new FallbackWithFactory();
        }
    }
    class FallbackWithFactory implements RiotAsiaClient {

        @Override
        public ResponseEntity<Map<String, String>> getPUuid(String gameName, String tagLine) {
            throw new CustomException(ErrorCode.SERVICE_UNAVAILABLE, "Feign Fallback : getPuuid failed");
        }
    }
}
