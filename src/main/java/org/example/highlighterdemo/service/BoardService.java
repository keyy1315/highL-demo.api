package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.model.entity.Board;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.model.entity.Tag;
import org.example.highlighterdemo.model.entity.enums.Category;
import org.example.highlighterdemo.model.entity.enums.MemberRole;
import org.example.highlighterdemo.model.requestDTO.BoardRequest;
import org.example.highlighterdemo.repository.board.BoardRepository;
import org.example.highlighterdemo.repository.member.MemberRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final TagService tagService;

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public void setBoard(BoardRequest boardRequest, String userName, String fileUrl) {
        Member member = memberRepository.findById(userName).orElseThrow(() ->
                new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find user")
        );
        List<Tag> tagList = tagService.setTags(boardRequest.tags());

        Board board = Board.create(boardRequest, tagList, fileUrl, member);
        boardRepository.save(board);
    }

    ///  sort : "createdDate", "view", "likes", "comments"
    public List<Board> getBoardList(String category, String sort, boolean desc) {
        if (sort == null) throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "sort is null");
        if (!"createdDate".equals(sort) && !"view".equals(sort) && !"likes".equals(sort) && !"comments".equals(sort)) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "sort is invalid");
        }
        if ("comments".equals(sort)) {
            return boardRepository.orderByCommentCnt(category, desc);
        } else if (desc)
            return boardRepository.findAllByCategory(Category.valueOf(category), Sort.by(Sort.Direction.DESC, sort));
        else return boardRepository.findAllByCategory(Category.valueOf(category), Sort.by(Sort.Direction.ASC, sort));
    }

    public Board getBoards(String id) {
        Board board = findBoardById(id);
        board.addView();
        return board;
    }

    @Transactional
    public void updateBoard(String username, String id, String file, BoardRequest boardRequest) {
        Board board = findBoardById(id);

        if (!Objects.equals(board.getMember().getId(), username))
            throw new CustomException(ErrorCode.FORBIDDEN, "you do not have permission to update this board");

        board.updateBoard(file, boardRequest);
    }

    @Transactional
    public void deleteBoard(UserDetails user, String id) {
        Board board = findBoardById(id);
        if (user.getAuthorities().stream().anyMatch(
                auth -> auth.getAuthority().equals(MemberRole.ADMIN.getValue()))
                || board.getMember().getId().equals(user.getUsername())) {
            boardRepository.delete(board);
        } else {
            throw new CustomException(ErrorCode.FORBIDDEN, "you do not have permission to delete this board");
        }
    }

    @Transactional
    public void likeBoard(String id) {
        Board board = findBoardById(id);
        board.likeBoard();
    }

    private Board findBoardById(String id) {
        return boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find board"));
    }

    public List<Board> getBoardsByFollow(UserDetails user) {
        if (user == null) throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "user is null");
        Member member = memberRepository.findById(user.getUsername()).orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find user"));
        List<Member> myFollowList = member.getMember();

        return myFollowList.stream()
                .flatMap(mem -> boardRepository.findAllByMember(mem).stream())
                .sorted(Comparator.comparing(Board::getCreatedDate).reversed())
                .toList();
    }
}
