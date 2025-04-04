package org.example.highlighterdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.config.swagger.annotation.SwaggerBody;
import org.example.highlighterdemo.model.requestDTO.BoardRequest;
import org.example.highlighterdemo.model.responseDTO.BoardResponse;
import org.example.highlighterdemo.service.BoardService;
import org.example.highlighterdemo.service.CommentService;
import org.example.highlighterdemo.service.S3Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
@Tag(name = "BoardController", description = "게시글 API")
public class BoardController {
    private final BoardService boardService;
    private final S3Service s3Service;
    private final CommentService commentService;

    @Operation(description = "게시글 생성")
    @SwaggerBody(content = @Content(
            encoding = @Encoding(name = "dto", contentType = MediaType.APPLICATION_JSON_VALUE)
    ))
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void setBoard(@AuthenticationPrincipal UserDetails user,
                         @RequestPart(value = "file", required = false) @Parameter(description = "static file") MultipartFile file,
                         @RequestPart("dto") @Parameter(description = "category:judgement/issues/mastery") BoardRequest boardRequest) throws IOException {
        if (user == null) throw new CustomException(ErrorCode.UNAUTHORIZED, "To post your board, please login first");
        String fileUrl = s3Service.uploadFile(file);
        boardService.setBoard(boardRequest, user.getUsername(), fileUrl);
    }

    @Operation(description = "게시글 목록 조회 <br> Default : createdDate desc")
    @GetMapping
    public List<BoardResponse> getBoards(@RequestParam(value = "category", required = false) @Parameter(description = "category : mastery, issues, judgement") String category,
                                         @RequestParam(value = "sort", required = false) @Parameter(description = "sort : createdDate, views, likes") String sort,
                                         @RequestParam(value = "desc") @Parameter(description = "true = desc, false = asc") boolean desc) {

        return boardService.getBoardList(category, sort, desc).stream()
                .map(board -> BoardResponse.create(board, commentService.getCommentCnt(board.getId()))).collect(Collectors.toList());
    }

    @Operation(description = "팔로우 하는 Member 의 게시글 목록 조회")
    @GetMapping("/follow")
    public List<BoardResponse> getBoardsByFollow(@AuthenticationPrincipal UserDetails user,
                                                 @RequestParam(value = "category", required = false) @Parameter(description = "category : mastery, issues, judgement") String category,
                                                 @RequestParam(value = "sort", required = false) @Parameter(description = "sort : createdDate, views, likes") String sort,
                                                 @RequestParam(value = "desc") @Parameter(description = "true = desc, false = asc") boolean desc
    ) {
        return boardService.getBoardsByFollow(user, category, sort, desc).stream()
                .map(board -> BoardResponse.create(board, commentService.getCommentCnt(board.getId()))).collect(Collectors.toList());
    }

    @Operation(description = "게시글 비디오 조회")
    @GetMapping("/video")
    public ResponseEntity<StreamingResponseBody> getVideos(@RequestParam("key") String key) {
        String fileName = key.split("/")[1];
        StreamingResponseBody stream = outputStream ->
                s3Service.getVideos(outputStream, key);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(stream);
    }

    @Operation(description = "게시글 조회")
    @GetMapping("/{id}")
    public BoardResponse getBoard(@PathVariable String id) {
        return BoardResponse.create(boardService.getBoards(id), commentService.getCommentCnt(id));
    }

    @Operation(description = "게시글 수정")
    @SwaggerBody(content = @Content(
            encoding = @Encoding(name = "dto", contentType = MediaType.APPLICATION_JSON_VALUE)
    ))
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateBoard(@AuthenticationPrincipal UserDetails user, @PathVariable String id,
                            @RequestPart(value = "file", required = false) MultipartFile file,
                            @RequestPart("dto") BoardRequest boardRequest) throws IOException {
        String fileUrl = s3Service.uploadFile(file);
        boardService.updateBoard(user.getUsername(), id, fileUrl, boardRequest);
    }

    @Operation(description = "게시글 좋아요")
    @PatchMapping("/like/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void likeBoard(@PathVariable String id, @AuthenticationPrincipal UserDetails user) {
        if (user == null) throw new CustomException(ErrorCode.UNAUTHORIZED, "like this video, please login first");
        boardService.likeBoard(id);
    }

    @Operation(description = "게시글 좋아요 취소")
    @PatchMapping("/unlike/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void unlikeBoard(@PathVariable String id, @AuthenticationPrincipal UserDetails user) {
        if (user == null) throw new CustomException(ErrorCode.UNAUTHORIZED, "unlike this video, please login first");
        boardService.unlikeBoard(id);
    }

    @Operation(description = "게시글 삭제")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBoard(@AuthenticationPrincipal UserDetails user, @PathVariable String id) {
        boardService.deleteBoard(user, id);
    }
}
