package com.foocmend.services.board.comment;

import com.foocmend.commons.MemberUtil;
import com.foocmend.controllers.board.CommentForm;
import com.foocmend.entities.BoardComment;
import com.foocmend.entities.BoardData;
import com.foocmend.repositories.BoardCommentRepository;
import com.foocmend.repositories.BoardDataRepository;
import com.foocmend.services.board.BoardDataNotExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 댓글 저장 및 수정
 *
 */
@Service
@RequiredArgsConstructor
public class SaveCommentService {

    private final BoardCommentRepository commentRepository;
    private final BoardDataRepository boardDataRepository;
    private final PasswordEncoder encoder;
    private final HttpServletRequest request;
    private final MemberUtil memberUtil;
    public void save(CommentForm form) {
        Long id = form.getId();
        Long boardDataId = form.getBoardDataId();
        BoardData data = boardDataRepository.findById(boardDataId).orElseThrow(BoardDataNotExistsException::new);

        BoardComment comment = null;
        if (id != null) { // 댓글 수정시
            comment = commentRepository.findById(id).orElseThrow(CommentNotExistsException::new);
        } else { // 댓글 추가시
            comment = BoardComment.builder()
                    .member(memberUtil.getEntity())
                    .boardData(data)
                    .ip(request.getRemoteAddr())
                    .ua(request.getHeader("User-Agent"))
                    .build();
        }

        comment.setCommenter(form.getCommenter());
        comment.setContent(form.getContent());

        String guestPw = comment.getGuestPw();


    }
}
