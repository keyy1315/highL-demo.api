package org.example.highlighterdemo.feign;

import org.example.highlighterdemo.feign.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "riot-api-asia", url = "https://asia.api.riotgames.com")
public interface RiotClientAsia {
    @GetMapping("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}")
    ResponseEntity<AccountDTO> getPUuid(@PathVariable("gameName") String gameName,
                                        @PathVariable("tagLine") String tagLine,
                                        @RequestParam("api_key") String apiKey);
}
