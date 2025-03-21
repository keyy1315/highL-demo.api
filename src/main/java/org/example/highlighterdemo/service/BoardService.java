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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final TagService tagService;

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


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

    ///  sort : "createdDate", "view", "likes", "comments"
    public List<Board> getBoards(String sort, boolean desc) {
        if(!"createdDate".equals(sort) && !"view".equals(sort) && !"likes".equals(sort) && !"comments".equals(sort)) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "sort is invalid");
        }
        if("comments".equals(sort)) {
            return boardRepository.orderByCommentCnt(desc);
        }
        if(desc) return boardRepository.findAll(Sort.by(Sort.Direction.DESC, sort));
        return boardRepository.findAll(Sort.by(Sort.Direction.ASC, sort));
    }
}
