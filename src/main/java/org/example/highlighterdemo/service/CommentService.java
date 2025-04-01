package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.model.entity.Board;
import org.example.highlighterdemo.model.entity.Comment;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.model.entity.enums.MemberRole;
import org.example.highlighterdemo.model.requestDTO.CommentRequest;
import org.example.highlighterdemo.model.responseDTO.CommentResponse;
import org.example.highlighterdemo.repository.board.BoardRepository;
import org.example.highlighterdemo.repository.comment.CommentRepository;
import org.example.highlighterdemo.repository.member.MemberRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public int getCommentCnt(String boardId) {
        return commentRepository.findAllByBoardId(boardId).size();
    }

    @Transactional
    public void setComments(String username, CommentRequest commentRequest) {
        Member member = memberRepository.findById(username).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "Cannot find member"));
        Board board = boardRepository.findById(commentRequest.boardId()).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "Cannot find board"));
        Comment parentComment = Optional.ofNullable(commentRequest.parentId())
                .flatMap(commentRepository::findById)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "Cannot find comment parent"));

        Comment comment = Comment.create(member, board, commentRequest.content(), parentComment);
        commentRepository.save(comment);
    }

    private List<Comment> getCommentsWithOption(String boardId, String sort, boolean asc) {
        if (sort == null) throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "sort is null");
        if (!"createdDate".equals(sort) && !"likes".equals(sort)) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "sort is invalid");
        }
        if (asc) return commentRepository.findAllByBoardId(boardId, Sort.by(Sort.Direction.ASC, sort));
        else return commentRepository.findAllByBoardId(boardId, Sort.by(Sort.Direction.DESC, sort));
    }

    public List<CommentResponse> getCommentList(String boardId, String sort, boolean asc) {
        List<Comment> comments = getCommentsWithOption(boardId, sort, asc);

        List<Comment> parentComments = comments.stream().filter(c -> c.getParentComment().getId() == null).toList();

        return parentComments.stream().map(p -> buildHierarchy(p, comments)).toList();
    }

    private CommentResponse buildHierarchy(Comment p, List<Comment> comments) {
        List<CommentResponse> childComments = comments.stream()
                .filter(c -> Objects.equals(c.getParentComment().getId(), p.getId()))
                .map(child -> buildHierarchy(child, comments))
                .toList();
        return CommentResponse.fromChild(p, childComments);
    }

    @Transactional
    public void updateComment(String memberId, String id, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "Cannot find comment"));

        if (Objects.equals(memberId, comment.getMember().getId())) {
            comment.updateComment(commentRequest.content());
        } else {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "you don't have a permission to update this comment");
        }
    }

    @Transactional
    public void deleteComment(UserDetails user, String id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "Cannot find comment"));

        if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(MemberRole.ADMIN.getValue())) ||
                comment.getMember().getId().equals(user.getUsername())) {
            commentRepository.delete(comment);
        }
    }
}
