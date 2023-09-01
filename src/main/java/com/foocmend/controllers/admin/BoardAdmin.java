package com.foocmend.controllers.admin;

import com.foocmend.commons.CommonException;
import com.foocmend.commons.Menu;
import com.foocmend.commons.MenuDetail;
import com.foocmend.entities.Board;
import com.foocmend.services.board.DeleteBoardDataService;
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

@Controller("adminBoard")
@RequestMapping("/admin/board")
@RequiredArgsConstructor
public class BoardAdmin {
    private final HttpServletRequest request;
    private final SaveBoardConfigService configSaveService;
    private final InfoBoardConfigService boardConfigInfoService;
    private final ListBoardConfigService boardConfigListService;
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

    private void commonProcess(Model model, String title) {
        String URI = request.getRequestURI();

        // 서브 메뉴 처리
        String subMenuCode = Menu.getSubMenuCode(request);

        subMenuCode = title.equals("게시판 수정") ? "register" : subMenuCode;

        model.addAttribute("subMenuCode", subMenuCode);

        List<MenuDetail> submenus = Menu.gets("board");
        model.addAttribute("submenus", submenus);

        model.addAttribute("pageTitle", title);
    }

    @PostMapping
    public String indexPs(BoardForm form, Model model) {
        commonProcess(model, "list");

        deleteBoardDataService.delete(form);

        model.addAttribute("script", "parent.location.reload();");
        return "commons/execute_script";
    }
}
