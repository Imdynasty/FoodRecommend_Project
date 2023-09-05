package com.foocmend.controllers.admin;

import com.foocmend.entities.EntityReview;
import com.foocmend.entities.Member;
import com.foocmend.services.admin.ReviewInfoService;
import com.foocmend.services.admin.ReviewSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("adminBasicReviewWrite")
@RequestMapping("/admin/review/write")
@RequiredArgsConstructor
public class WriteReview {

    private final ReviewInfoService infoService;
    private final ReviewSaveService saveService;
    private String code = "reviewWrite";

    @GetMapping
    public String write(Model model) {
        commonProcess(model);
      // WriteReviewForm writeReviewForm = infoService.get(code, WriteReviewForm.class);
       // model.addAttribute("configForm", writeReviewForm == null ? new WriteReviewForm() : writeReviewForm);

        return "admin/basic/write_review";
    }

    @PostMapping
    public String save(WriteReviewForm writeReviewForm, Model model) {
        commonProcess(model);
      //  saveService.save(code, writeReviewForm);
        model.addAttribute("message", "설정이 저장되었습니다.");
        return "redirect:/admin/basic/write_review";
    }


    private void commonProcess(Model model) {
        String title = "리뷰 작성";
        String menuCode = "write_review";
        model.addAttribute("menuCode", menuCode);
        model.addAttribute("pageTitle", title);
        model.addAttribute("title", title);
    }
}
