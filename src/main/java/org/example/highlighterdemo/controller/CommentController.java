package org.example.highlighterdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.requestDTO.CommentRequest;
import org.example.highlighterdemo.model.responseDTO.CommentResponse;
import org.example.highlighterdemo.service.CommentService;
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

    @Operation(description = "get comments by boardId with hierarchy orderBy CreatedDate")
    @GetMapping("/{board}")
    public List<CommentResponse> getCommentsByBoardId(
            @PathVariable("board") @Parameter(description = "board_id") String boardId) {
        return commentService.getComments(boardId);
    }

    @Operation(description = "write comments")
    @PostMapping
    public CommentResponse setComments(@AuthenticationPrincipal UserDetails user,
                                       @RequestBody CommentRequest commentRequest) {
        return CommentResponse.create(commentService.setComments(user.getUsername(),commentRequest));
    }
}
