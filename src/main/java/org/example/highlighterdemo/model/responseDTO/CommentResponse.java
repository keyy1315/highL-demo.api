package org.example.highlighterdemo.model.responseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.highlighterdemo.model.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "commentResponse")
public record CommentResponse(
        @Schema(description = "comment pk") String id,
        @Schema(description = "parent's id") String parentId,
        @Schema(description = "content") String content,
        @Schema(description = "like count") int likes,
        @Schema(description = "member's id") MemberResponse member,
        @Schema(description = "date") LocalDateTime createdDate,
        @Schema(description = "child comments") List<CommentResponse> childComments
) {
    public static CommentResponse from(Comment parent, List<CommentResponse> childComments) {
        return new CommentResponse(
                parent.getId(), parent.getParentId(), parent.getContent(),
                parent.getLikes(), MemberResponse.create(parent.getMember()), parent.getCreatedDate(),
                childComments);
    }
    public static CommentResponse create(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getParentId(),
                comment.getContent(), comment.getLikes(), MemberResponse.create(comment.getMember()),
                comment.getCreatedDate(), null);
    }
}
