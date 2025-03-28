package org.example.highlighterdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.config.swagger.annotation.SwaggerBody;
import org.example.highlighterdemo.model.entity.Board;
import org.example.highlighterdemo.model.requestDTO.BoardRequest;
import org.example.highlighterdemo.model.responseDTO.BoardResponse;
import org.example.highlighterdemo.service.BoardService;
import org.example.highlighterdemo.service.CommentService;
import org.example.highlighterdemo.service.S3Service;
import org.springframework.http.HttpHeaders;
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
    public BoardResponse setBoard(@AuthenticationPrincipal UserDetails user,
                                  @RequestPart(value = "file", required = false) @Parameter(description = "static file") MultipartFile file,
                                  @RequestPart("dto") @Parameter(description = "board create dto") BoardRequest boardRequest) throws IOException {
        String fileUrl = s3Service.uploadFile(file);
        Board board = boardService.setBoard(boardRequest, user.getUsername(), fileUrl);
        int comments = commentService.getCommentCnt(board.getId());
        return BoardResponse.create(board, comments);
    }

    @Operation(description = "게시글 목록 조회")
    @GetMapping
    public List<BoardResponse> getBoards(@RequestParam("sort") @Parameter(description = "sort : createdDate, view, like") String sort,
                                         @RequestPart("desc") @Parameter(description = "true = desc, false = asc") boolean desc) {
        return boardService.getBoards(sort, desc).stream()
                .map(board -> {
                    return BoardResponse.create(board, commentService.getCommentCnt(board.getId()));
                }).collect(Collectors.toList());
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
}
