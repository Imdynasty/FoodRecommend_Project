package com.foocmend.controllers.admin;

import com.foocmend.commons.*;
import com.foocmend.entities.Board;
import com.foocmend.entities.BoardData;
import com.foocmend.services.board.DeleteBoardDataService;
import com.foocmend.services.board.InfoBoardDataService;
import com.foocmend.services.board.config.DeleteBoardConfigService;
import com.foocmend.services.board.config.InfoBoardConfigService;
import com.foocmend.services.board.config.ListBoardConfigService;
import com.foocmend.services.board.config.SaveBoardConfigService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller("adminBoard")
@RequestMapping("/admin/board")
@RequiredArgsConstructor
public class BoardAdmin implements ScriptExceptionProcess {
    private final HttpServletRequest request;
    private final SaveBoardConfigService configSaveService;
    private final InfoBoardConfigService boardConfigInfoService;
    private final ListBoardConfigService boardConfigListService;
    private final DeleteBoardConfigService deleteBoardConfigService;

    private final InfoBoardDataService boardDataService;
    private final DeleteBoardDataService deleteBoardDataService;
    /**
     * 게시판 목록
     *
     * @return
     */
    @GetMapping
    public String index(@ModelAttribute BoardSearch boardSearch, Model model) {
        commonProcess(model, "게시판 목록");

        Page<Board> data = boardConfigListService.gets(boardSearch);
        model.addAttribute("items", data.getContent());

        return "admin/board/index";
    }

    /**
     * 게시판 등록
     * @return
     */
    @GetMapping("/register")
    public String register(@ModelAttribute BoardForm boardForm, Model model) {
        commonProcess(model, "게시판 등록");

        return "admin/board/config";
    }

    @GetMapping("/{bId}/update")
    public String update(@PathVariable String bId, Model model) {
        commonProcess(model, "게시판 수정");

        Board board = boardConfigInfoService.get(bId, true);
        BoardForm boardForm = new ModelMapper().map(board, BoardForm.class);
        boardForm.setMode("update");
        boardForm.setListAccessRole(board.getListAccessRole().toString());
        boardForm.setViewAccessRole(board.getViewAccessRole().toString());
        boardForm.setWriteAccessRole(board.getWriteAccessRole().toString());
        boardForm.setReplyAccessRole(board.getReplyAccessRole().toString());
        boardForm.setCommentAccessRole(board.getCommentAccessRole().toString());

        model.addAttribute("boardForm", boardForm);

        return "admin/board/config";
    }

    @PostMapping("/save")
    public String save(@Valid BoardForm boardForm, Errors errors, Model model) {
        String mode = boardForm.getMode();
        commonProcess(model, mode != null && mode.equals("update") ? "게시판 수정" : "게시판 등록");

        try {
            configSaveService.save(boardForm, errors);
        } catch (CommonException e) {
            errors.reject("BoardConfigError", e.getMessage());
        }

        if (errors.hasErrors()) {
            return "admin/board/config";
        }


        return "redirect:/admin/board"; // 게시판 목록
    }

    /**
     * 게시글 관리
     *
     * @return
     */
    @GetMapping("/posts")
    public String posts(@ModelAttribute BoardSearchFront search, Model model) {
        commonProcess(model, "게시글 관리");
        String bId = search.getBId();
        List<Board> boards = boardConfigListService.getAll();
        model.addAttribute("boards", boards);

        ListData<BoardData> data = bId == null || bId.isBlank() ? null : boardDataService.getList(bId, search);
        if (data != null) {
            model.addAttribute("items", data.getContent());
            model.addAttribute("pagination", data.getPagination());
        }



        return "admin/board/posts";
    }

    @PostMapping("/posts")
    public String postsPs(@RequestParam(value = "id", required = false) Long[] ids, Model model) {

        try {
            if (ids == null || ids.length == 0) {
                throw new AlertException("삭제할 게시글을 선택하세요.");
            }

            for (Long id : ids) {
                deleteBoardDataService.delete(id, true);
            }
        } catch (CommonException e) {
            e.printStackTrace();
            throw new AlertException(e.getMessage());
        }

        model.addAttribute("script", "parent.location.reload()");
        return "commons/execute_script";
    }

    private void commonProcess(Model model, String title) {
        String URI = request.getRequestURI();
        title = Objects.requireNonNullElse(title, "list");

        // 서브 메뉴 처리
        String subMenuCode = Menu.getSubMenuCode(request);

        subMenuCode = title.equals("게시판 수정") ? "register" : subMenuCode;

        model.addAttribute("subMenuCode", subMenuCode);

        List<MenuDetail> submenus = Menu.gets("board");
        model.addAttribute("submenus", submenus);

        model.addAttribute("pageTitle", title);
    }

    @PostMapping
    public String indexPs(String[] bId, Model model) {
        commonProcess(model, "list");

        deleteBoardConfigService.delete(bId);

        model.addAttribute("script", "parent.location.reload();");
        return "commons/execute_script";
    }
}
