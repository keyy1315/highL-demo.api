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

import java.util.List;
import java.util.Objects;

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
        if (sort == null && category == null) {
            if (desc) return boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
            return boardRepository.findAll(Sort.by(Sort.Direction.ASC, "createdDate"));
        }
        if (category == null) {
            if (desc) return boardRepository.findAll(Sort.by(Sort.Direction.DESC, sort));
            return boardRepository.findAll(Sort.by(Sort.Direction.ASC, sort));
        }
        if (!"mastery".equals(category) && !"judgement".equals(category) && !"issues".equals(category)) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "category is invalid : " + category);
        }
        if (sort == null) {
            if (desc)
                return boardRepository.findAllByCategory(Category.getCategory(category), Sort.by(Sort.Direction.DESC, "createdDate"));
            return boardRepository.findAllByCategory(Category.getCategory(category), Sort.by(Sort.Direction.ASC, "createdDate"));
        }
        if (!"createdDate".equals(sort) && !"views".equals(sort) && !"likes".equals(sort) && !"comments".equals(sort)) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "sort is invalid : " + sort);
        }

        if ("comments".equals(sort)) {
            return boardRepository.orderByCommentCnt(category, desc);
        } else if (desc)
            return boardRepository.findAllByCategory(Category.getCategory(category), Sort.by(Sort.Direction.DESC, sort));
        else
            return boardRepository.findAllByCategory(Category.getCategory(category), Sort.by(Sort.Direction.ASC, sort));
    }

    @Transactional
    public Board getBoards(String id) {
        Board board = findBoardById(id);
        board.addView();
        return boardRepository.save(board);
    }

    @Transactional
    public void updateBoard(String username, String id, String file, BoardRequest boardRequest) {
        Board board = findBoardById(id);

        if (!Objects.equals(board.getMember().getId(), username))
            throw new CustomException(ErrorCode.FORBIDDEN, "you do not have permission to update this board");

        List<Tag> tagList = tagService.setTags(boardRequest.tags());

        board.updateBoard(file, boardRequest, tagList);
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

    @Transactional
    public void unlikeBoard(String id) {
        Board board = findBoardById(id);
        board.unlikeBoard();
    }

    private Board findBoardById(String id) {
        return boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find board"));
    }

    public List<Board> getBoardsByFollow(UserDetails user, String category, String sort, boolean desc) {

        if (user == null)
            throw new CustomException(ErrorCode.UNAUTHORIZED, "To Read Your following Member's video, Login First");
        Member member = memberRepository.findById(user.getUsername()).orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find user"));
        List<Member> myFollowList = member.getFollowings();

        List<Board> filteringList = getBoardList(category, sort, desc);

        return filteringList.stream()
                .filter(board -> myFollowList.contains(board.getMember()))
                .toList();
    }

    public Board getBoardById(String id) {
        return boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find board"));
    }
}
