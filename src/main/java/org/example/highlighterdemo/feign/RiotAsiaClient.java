package org.example.highlighterdemo.feign;

import org.example.highlighterdemo.feign.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "riot-api-asia", url = "https://asia.api.riotgames.com", configuration = FeignConfig.class)
public interface RiotAsiaClient {
    @GetMapping("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}")
    ResponseEntity<Map<String, String>> getPUuid(@PathVariable("gameName") String gameName,
                                                 @PathVariable("tagLine") String tagLine);
}
