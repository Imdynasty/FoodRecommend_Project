package com.foocmend.controllers.board;

import com.foocmend.commons.CommonException;
import com.foocmend.commons.ListData;
import com.foocmend.commons.MemberUtil;
import com.foocmend.commons.Utils;
import com.foocmend.commons.validators.BoardFormValidator;
import com.foocmend.entities.Board;
import com.foocmend.entities.BoardData;
import com.foocmend.entities.Member;
import com.foocmend.services.board.*;
import com.foocmend.services.board.config.BoardNotAllowAccessException;
import com.foocmend.services.board.config.InfoBoardConfigService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller("frontBoard")
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardFront {
    private final InfoBoardConfigService boardConfigInfoService;
    private final InfoBoardDataService infoService;
    private final SaveBoardDataService saveService;
    private final BoardFormValidator formValidator;
    private final HttpServletResponse response;
    private final MemberUtil memberUtil;
    private final UpdateHitService updateHitService;
    private final CheckGuestPasswordService passwordCheckService;
    private final DeleteBoardDataService deleteService;
    private final HttpSession session;

    private final Utils utils;
    private Board board; // 게시판 설정

    /**
     * 게시글 목록
     * @param bId
     * @return
     */
    @GetMapping("/list/{bId}")
    public String list(@PathVariable String bId, @ModelAttribute BoardSearch search, Model model) {
        commonProcess(bId, "list", model);
        ListData<BoardData> data = infoService.getList(bId, search);
        log.info(board.toString());
        model.addAttribute("items", data.getContent());
        model.addAttribute("pagination", data.getPagination());

        return utils.view("board/list");
    }

    /**
     * 게시글 작성
     *
     * @param bId
     * @return
     */
    @GetMapping("/write/{bId}")
    public String write(@PathVariable String bId, @ModelAttribute BoardForm boardForm, Model model) {
        commonProcess(bId, "write", model);
        boardForm.setBId(bId);
        if (memberUtil.isLogin()) {
            boardForm.setPoster(memberUtil.getMember().getNickname());
        }

        return utils.view("board/write");
    }

    /**
     * 게시글 수정
     * @param id
     * @return
     */
    @GetMapping("/{id}/update")
    public String update(@PathVariable Long id, Model model) {
        BoardData boardData = infoService.get(id, "update");
        board = boardData.getBoard();
        commonProcess(board.getBId(), "update", model);

        // 수정 권한 체크
        updateDeletePossibleCheck(boardData);

        BoardForm boardForm = new ModelMapper().map(boardData, BoardForm.class);
        if (boardData.getMember() == null) {
            board.setGuest(true);
        }

        model.addAttribute("boardForm", boardForm);

        return utils.view("board/update");
    }

    @PostMapping("/save")
    public String save(@Valid BoardForm boardForm, Errors errors, Model model) {
        Long id = boardForm.getId();
        String mode = "write";
        if (id != null) {
            mode = "update";
            BoardData boardData = infoService.get(id);
            board = boardData.getBoard();
            if (boardData.getMember() == null) {
                board.setGuest(true);
            } else {
                boardForm.setUserNo(boardData.getMember().getMemNo());
            }

            updateDeletePossibleCheck(boardData);

        }

        commonProcess(boardForm.getBId(), mode, model);



        formValidator.validate(boardForm, errors);

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(System.out::println);
            return utils.view("board/" + mode);
        }
        System.out.println(boardForm);
        saveService.save(boardForm);

        // 작성후 이동 설정 - 목록, 글보기
        String location = board.getLocationAfterWriting();
        String url = "redirect:/board/";
        url += location.equals("view") ? "view/" + boardForm.getId() : "list/" + boardForm.getBId();

        return url;
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        BoardData boardData = infoService.get(id);
        board = boardData.getBoard();

        commonProcess(board.getBId(), "view", model);

        model.addAttribute("boardData", boardData);
        model.addAttribute("board", board);

        updateHitService.update(id); // 게시글 조회수 업데이트

        return utils.view("board/view");

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        BoardData boardData = infoService.get(id, "update");
        board = boardData.getBoard();
        String bid = board.getBId();
        commonProcess(bid, "update", model);

        // 삭제 권한 체크
        updateDeletePossibleCheck(boardData, "board_delete");

        // 삭제 처리
        deleteService.delete(id);

        // 삭제 완료시 게시글 목록으로 이동
        return "redirect:/board/list/" + bid;
    }

    @PostMapping("/password")
    public String password(String password) {

        String mode = (String)session.getAttribute("guestPwMode");
        Long id = (Long)session.getAttribute("guestPwId");

        // 비회원 비밀번호 검증
        passwordCheckService.check(id, password, mode);

        // 비회원 비밀번호 검증 완료 처리
        session.setAttribute(mode + "_" + id, true);

        // 비회원 비밀번호 확인 후 이동 경로
        /**
         String url = mode == "comment" ? "/board/" + id + "/comment" : "/board/" + id + "/update";
         */
        String url = "/board/" + id + "/update";
        if (mode.equals("comment")) { // 댓글 삭제
            url = "/board/" + id + "/comment";
        } else if (mode.equals("board_delete")) { // 글 삭제
            url = "/board/delete/" + id;
        }

        // 검증 완료 후 세션 제거
        session.removeAttribute("guestPwMode");
        session.removeAttribute("guestPwId");

        return "redirect:" + url;
    }

    private void commonProcess(String bId, String action, Model model) {
        /**
         * 1. bId 게시판 설정 조회
         * 2. action - write, update - 공통 스크립트, 공통 CSS
         *           - 에디터 사용 -> 에디터 스크립트 추가
         *           - 에디터 미사용 -> 에디터 스크립트 미추가
         *           - write, list, view -> 권한 체크
         *           - update - 본인이 게시글만 수정 가능
         *                    - 회원 - 회원번호
         *                    - 비회원 - 비회원비밀번호
         *                    - 관리자는 다 가능
         *
         */

        board = boardConfigInfoService.get(bId, action);
        List<String> addCss = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        List<String> addCommonScript =  new ArrayList<>();
        // 공통 스타일 CSS
        addCss.add("board/style");
        addCss.add(String.format("board/%s_style", board.getSkin()));

        // 글 작성, 수정시 필요한 자바스크립트
        if (action.equals("write") || action.equals("update")) {
            if (board.isUseEditor()) { // 에디터 사용 경우
                addCommonScript.add("ckeditor/ckeditor");
            }
            addCommonScript.add("fileManager");
            addScript.add("board/form");
        }

        // 공통 필요 속성 추가
        model.addAttribute("board", board); // 게시판 설정
        model.addAttribute("addCss", addCss); // CSS 설정
        model.addAttribute("addScript", addScript); // JS 설정
        model.addAttribute("addCommonScript", addCommonScript);

    }

    /**
     * 수정, 삭제 권한 체크
     *
     * - 회원 : 작성한 회원
     * - 비회원 : 비밀번호 검증
     * - 관리자 : 가능
     *
     * @param boardData
     */
    public void updateDeletePossibleCheck(BoardData boardData, String mode) {
        mode = mode == null ? "board":mode;
        if (memberUtil.isAdmin()) { // 관리자는 무조건 가능
            return;
        }
        Member member = boardData.getMember();

        if (member == null) { // 비회원일때는 비밀번호 검증이 되었는지 체크, 안되어 있다면 비밀번호 확인 페이지 이동
            /*
             * 세션 키 - "board_게시글 번호" 가 있으면 비회원 비밀번호 검증 완료
             */
            if (session.getAttribute(mode+"_" + boardData.getId()) == null) {
                // 1. 위치 - 게시글 : board, 삭제:  board_delete, 댓글 comment
                // 2. 게시글 번호
                session.setAttribute("guestPwMode", mode);
                session.setAttribute("guestPwId", boardData.getId());

                throw new GuestPasswordNotCheckedException(); // 비빌번호 확인 페이지 노출
            }
        } else { // 글을 작성한 회원쪽만 가능하게 통제
            if (!memberUtil.isLogin() || (memberUtil.isLogin()
                    && memberUtil.getMember().getMemNo() != boardData.getMember().getMemNo())) {
                System.out.println(memberUtil.getMember().getMemNo());
                System.out.println(boardData.getMember().getMemNo());
                throw new BoardNotAllowAccessException();
            }
        }

    }

    public void updateDeletePossibleCheck(Long id) {
        BoardData boardData = infoService.get(id, "update");
        updateDeletePossibleCheck(boardData, null);
    }

    public void updateDeletePossibleCheck(BoardData boardData) {
        updateDeletePossibleCheck(boardData, null);
    }

    @ExceptionHandler(CommonException.class)
    public String errorHandler(CommonException e, Model model) {
        e.printStackTrace();

        String message = e.getMessage();
        HttpStatus status = e.getStatus();
        response.setStatus(status.value());

        if (e instanceof GuestPasswordNotCheckedException) { // 비회원 비밀번호 검증 관련 예외
            return utils.view("board/password");
        }

        String script = String.format("alert('%s');history.back();", message);
        model.addAttribute("script", script);
        return "commons/execute_script";
    }
}
