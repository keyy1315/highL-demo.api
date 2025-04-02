package org.example.highlighterdemo.model.responseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.model.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "commentResponse")
public record CommentResponse(
        @Schema(description = "comment pk") String id,
        @Schema(description = "content") String content,
        @Schema(description = "like count") int likes,
        @Schema(description = "member's id") MemberResponse member,
        @Schema(description = "date") LocalDateTime createdDate,
        @Schema(description = "parent Comment Id") String parentCommentId,
        @Schema(description = "child comments") List<CommentResponse> childComments
) {
    public static CommentResponse fromChild(Comment parent, List<CommentResponse> children) {
        if (parent == null && children == null) return null;
        if (parent != null) {
            String parentCommentId = (parent.getParentComment() != null)
                    ? parent.getParentComment().getId()
                    : null;

            return new CommentResponse(parent.getId(), parent.getContent(), parent.getLikes(), MemberResponse.create(parent.getMember()),
                    parent.getCreatedDate(), parentCommentId, children);
        } else {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "parent is null");
        }
    }
}
