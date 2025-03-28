package org.example.highlighterdemo.feign;

import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.feign.config.FeignConfig;
import org.example.highlighterdemo.feign.dto.LeagueEntryDTO;
import org.example.highlighterdemo.feign.dto.SummonerDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient(name = "riot-service", url = "https://kr.api.riotgames.com", configuration = FeignConfig.class,
fallbackFactory = RiotClient.RiotFallbackFactory.class)
public interface RiotClient {
    @GetMapping("/lol/summoner/v4/summoners/by-puuid/{encryptedPUUID}")
    ResponseEntity<SummonerDTO> getSummoner(@PathVariable("encryptedPUUID") String puuid);

    @GetMapping("/lol/league/v4/entries/by-summoner/{encryptedSummonerId}")
    ResponseEntity<Set<LeagueEntryDTO>> getLeagueEntry(@PathVariable("encryptedSummonerId") String encryptedSummonerId);

    @Component
    class RiotFallbackFactory implements FallbackFactory<FallbackWithFactory> {

        @Override
        public FallbackWithFactory create(Throwable cause) {
            return new FallbackWithFactory();
        }
    }
    class FallbackWithFactory implements RiotClient {
        @Override
        public ResponseEntity<SummonerDTO> getSummoner(String puuid) {
           throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Feign Fallback : getSummoner failed");
        }

        @Override
        public ResponseEntity<Set<LeagueEntryDTO>> getLeagueEntry(String encryptedSummonerId) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Feign Fallback : getLeagueEntry failed");
        }
    }
}
