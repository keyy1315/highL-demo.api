package org.example.highlighterdemo.feign;

import org.example.highlighterdemo.feign.dto.LeagueEntryDTO;
import org.example.highlighterdemo.feign.dto.SummonerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(name = "riot-service", url = "https://kr.api.riotgames.com")
public interface RiotClient {
    @GetMapping("/lol/summoner/v4/summoners/by-puuid/{encryptedPUUID}")
    ResponseEntity<SummonerDTO> getSummoner(@PathVariable("encryptedPUUID") String puuid,
                                            @RequestParam("api_key") String apiKey);

    @GetMapping("/lol/league/v4/entries/by-summoner/{encryptedSummonerId}")
    ResponseEntity<Set<LeagueEntryDTO>> getLeagueEntry(@PathVariable("encryptedSummonerId") String encryptedSummonerId,
                                                       @RequestParam("api_key") String apiKey);
}
