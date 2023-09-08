package com.foocmend.restcontrollers.board;

import com.foocmend.commons.MemberUtil;
import com.foocmend.commons.Utils;
import com.foocmend.commons.rests.JSONData;
import com.foocmend.commons.validators.BoardFormValidator;
import com.foocmend.controllers.board.BoardForm;
import com.foocmend.entities.Board;
import com.foocmend.services.board.*;
import com.foocmend.services.board.config.InfoBoardConfigService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ResourceBundle;

@Slf4j
@RestController
@RequestMapping("/ajax/board")
@RequiredArgsConstructor
public class AjaxBoard {
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

    @PostMapping("/save")
    public ResponseEntity<JSONData<Object>> save(@Valid BoardForm boardForm, Errors errors, Model model) {
        formValidator.validate(boardForm, errors);

        if (errors.hasErrors()) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages.validations");
            List<String> codes = errors.getAllErrors().stream().map(o -> bundle.getString(o.getCode())).toList();

        }

        saveService.save(boardForm);
        JSONData<Object> jsonData = JSONData.builder()
                .success(true)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(jsonData);
    }
}
