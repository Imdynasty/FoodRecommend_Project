package com.foocmend.controllers.admin;

import com.foocmend.entities.Member;
import com.foocmend.repositories.MemberRepository;
import com.foocmend.services.member.SearchMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("adminBasicMemberManage")
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class MemberManage {

    private final MemberRepository repository;

    @GetMapping
    public String config(Model model) {
        List<Member> memberList = repository.findAll();
        model.addAttribute("memberList", memberList);

        commonProcess(model);
        return "admin/basic/member";

    }

    @PostMapping
    public String save(Model model) {
        commonProcess(model);

        return "redirect:/admin/member";
    }

    private void commonProcess(Model model) {
        model.addAttribute("menuCode", "member");
        model.addAttribute("pageTitle", "회원관리");
    }
}
