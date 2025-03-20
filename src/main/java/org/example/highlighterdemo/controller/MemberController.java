package org.example.highlighterdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.feign.RiotClient;
import org.example.highlighterdemo.feign.RiotClientAsia;
import org.example.highlighterdemo.feign.dto.LeagueEntryDTO;
import org.example.highlighterdemo.feign.dto.SummonerDTO;
import org.example.highlighterdemo.model.entity.GameInfo;
import org.example.highlighterdemo.model.requestDTO.GameInfoRequest;
import org.example.highlighterdemo.model.requestDTO.MemberRequest;
import org.example.highlighterdemo.model.responseDTO.MemberResponse;
import org.example.highlighterdemo.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Tag(name = "MemberController", description = "회원 API")
public class MemberController {
    @Value("${riot.api}")
    private String riotApi;

    private final MemberService memberService;
    private final RiotClient riotClient;
    private final RiotClientAsia riotClientAsia;

    @Operation(description = "회원가입 - 사용자를 생성한다.")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse signup(@RequestBody MemberRequest req) {
        return MemberResponse.create(memberService.signup(req));
    }

    @Operation(description = "회원 정보 수정 - 라이엇 계정 아이디와 티어를 연동한다.")
    @PatchMapping("/lol")
    public MemberResponse patchGameId(@RequestBody GameInfoRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String puuid = Objects.requireNonNull(riotClientAsia.getPUuid(request.gameName(), request.tagLine(), riotApi).getBody()).puuid();
        SummonerDTO summonerDTO = riotClient.getSummoner(puuid, riotApi).getBody();
        Set<LeagueEntryDTO> league = riotClient.getLeagueEntry(Objects.requireNonNull(summonerDTO).id(), riotApi).getBody();

        if (Objects.requireNonNull(league).isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, request.gameName() + "#" + request.tagLine() + " has no league entry this season..");
        }
        GameInfo gameInfo = GameInfo.create(request, Objects.requireNonNull(league), summonerDTO.profileIconId());
        return MemberResponse.create(memberService.setGameInfo(userDetails.getUsername(), gameInfo));
    }

    @Operation(description = "전체 회원 목록 조회")
    @GetMapping
    public List<MemberResponse> getUsers(@RequestParam(value = "isActive", required = false)
                                         @Parameter(description = "true/false - 활성화/비활성화")
                                         String isActive) {
        return memberService.getUsers(isActive).stream()
                .map(MemberResponse::create)
                .collect(Collectors.toList());
    }

    @Operation(description = "회원 ID로 회원 조회")
    @GetMapping("/{userId}")
    public MemberResponse getUsersByUserId(@PathVariable String userId) {
        return MemberResponse.create(memberService.getUsersByUserId(userId));
    }
}
