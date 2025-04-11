package org.example.highlighterdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.requestDTO.CommentRequest;
import org.example.highlighterdemo.model.responseDTO.CommentResponse;
import org.example.highlighterdemo.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
@Tag(name = "CommentController", description = "댓글 API")
public class CommentController {
    private final CommentService commentService;

    @Operation(description = "댓글 생성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void setComment(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CommentRequest commentRequest) {
        commentService.setComments(userDetails, commentRequest);
    }

    @Operation(description = "댓글 목록 조회")
    @GetMapping("/{id}")
    public List<CommentResponse> getCommentList(@PathVariable("id") String boardId,
                                                @RequestParam("sort") @Parameter(description = "sort : createdDate, like") String sort,
                                                @RequestParam("desc") @Parameter(description = "true = asc, false = desc") boolean asc) {
        return commentService.getCommentList(boardId, sort, asc);
    }

    @Operation(description = "댓글 수정")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateComment(@AuthenticationPrincipal UserDetails user, @PathVariable String id, @RequestBody CommentRequest commentRequest) {
        commentService.updateComment(user, id, commentRequest);
    }

    @Operation(description = "댓글 삭제")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@AuthenticationPrincipal UserDetails user, @PathVariable String id) {
        commentService.deleteComment(user, id);
    }
}
