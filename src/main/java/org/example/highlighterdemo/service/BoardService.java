package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.model.entity.Board;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.model.entity.Tag;
import org.example.highlighterdemo.model.requestDTO.BoardRequest;
import org.example.highlighterdemo.repository.BoardRepository;
import org.example.highlighterdemo.repository.MemberRepository;
import org.example.highlighterdemo.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final TagService tagService;

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;

    @Transactional
    public Board setBoard(BoardRequest boardRequest, String userName, String fileUrl) {
        Member member = memberRepository.findById(userName).orElseThrow(() ->
                new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find user")
        );
        List<Tag> tagList = tagService.getTags(boardRequest.tags());

        Board board = Board.create(boardRequest, tagList, fileUrl, member);
        boardRepository.save(board);
        return board;
    }
}
