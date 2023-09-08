package com.foocmend.controllers.admin;

import com.foocmend.commons.*;
import com.foocmend.commons.constants.Foods;
import com.foocmend.commons.constants.Role;
import com.foocmend.controllers.member.SignUpForm;
import com.foocmend.entities.BoardData;
import com.foocmend.entities.Member;
import com.foocmend.entities.QMember;
import com.foocmend.repositories.BoardDataRepository;
import com.foocmend.repositories.MemberRepository;
import com.foocmend.services.admin.MemberSearch;
import com.foocmend.services.admin.SearchMemberList;
import com.foocmend.services.board.BoardDataNotExistsException;
import com.foocmend.services.board.InfoBoardDataService;
import com.foocmend.services.member.SaveMemberService;
import com.foocmend.services.member.SearchMemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class MemberManage {
    private String tplCommon = "admin/basic/";

    private final HttpServletRequest request;
    private final SearchMemberService infoService;
    private final SearchMemberList listService;
    private final SaveMemberService saveService;


    //회원 관리 메인
    @GetMapping
    public String index(@ModelAttribute MemberSearch search, Model model) {
        commonProcess(model, "list");
        ListData<Member> data = listService.getList(search);

        model.addAttribute("items", data.getContent());
        model.addAttribute("pagination", data.getPagination());

        return "admin/basic/member";
    }
    @PostMapping
    public String indexPs(SignUpForm form , Model model) {
        commonProcess(model, "list");

        String mode = form.getMode();
        try {
            if(mode.contains("edit")) {
                saveService.save(form);
            }
        } catch (CommonException e) {
            e.printStackTrace();
            throw new AlertException(e.getMessage()); // 자바스크립트 alert 형태로 에러 출력
        }

        String script = "parent.location.reload();";
        model.addAttribute("script", script);
        return "common/execute_script";
    }



    @GetMapping("/edit/{nickname}")
    public String edit(@PathVariable String nickname, Model model) {
        commonProcess(model, "edit");
        SignUpForm memberForm = infoService.getMemberForm(nickname);
        model.addAttribute("memberForm", memberForm);
        model.addAttribute("foods", Foods.getList());
        model.addAttribute("addCommonScript", new String[] {"address"});

        return tplCommon + "edit2";
    }

    @PostMapping("/save")
    public String save(SignUpForm signUpForm, Errors errors, Model model) {
        commonProcess(model, "edit");
        signUpForm.setMode("edit");
        saveService.save(signUpForm);

        if (errors.hasErrors()) {
            return tplCommon + "edit2";
        }

        return "redirect:/admin/member";
    }
    @GetMapping("/review/{nickname}")
    public String userReview(@PathVariable String nickname) {


        return "review";
    }

    public void commonProcess(Model model, String mode) {
        String pageTitle = "회원 목록";
        if (mode.contains("edit")) {
            pageTitle = "정보 수정";
        }

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("menuCode", "member");
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("addCss", new String[] { "member/style", "member/mypage"});

        // 서브 메뉴 처리
        String subMenuCode = Menu.getSubMenuCode(request);
        model.addAttribute("subMenuCode", subMenuCode);

        // 서브 메뉴 조회
        List<MenuDetail> submenus = Menu.gets("member");
        model.addAttribute("submenus", submenus);

        model.addAttribute("roles", Role.getList());
    }








//    @GetMapping
//    public String config(Model model) {
//        List<Member> memberList = repository.findAll();
//        model.addAttribute("memberList", memberList);
//
//        commonProcess(model);
//        return "admin/basic/member";
//
//    }
//
//    @PostMapping
//    public String save(Model model) {
//        commonProcess(model);
//
//        return "redirect:/admin/member";
//    }
//
//    private void commonProcess(Model model) {
//        model.addAttribute("menuCode", "member");
//        model.addAttribute("pageTitle", "회원관리");
//    }
}
