package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.model.entity.Board;
import org.example.highlighterdemo.model.entity.Comment;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.model.requestDTO.CommentRequest;
import org.example.highlighterdemo.model.responseDTO.CommentResponse;
import org.example.highlighterdemo.repository.board.BoardRepository;
import org.example.highlighterdemo.repository.comment.CommentRepository;
import org.example.highlighterdemo.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public int getCommentCnt(String boardId) {
        return commentRepository.findAllByBoardId(boardId).size();
    }

    public Comment setComments(String username, CommentRequest commentRequest) {
        Member member = memberRepository.findById(username).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "Cannot find member"));
        Board board = boardRepository.findById(commentRequest.boardId()).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "Cannot find board"));

        Comment comment = Comment.create(member, board, commentRequest);
        return commentRepository.save(comment);
    }
}
