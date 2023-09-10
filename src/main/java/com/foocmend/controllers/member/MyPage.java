package com.foocmend.controllers.member;

import com.foocmend.commons.ListData;
import com.foocmend.commons.Pagination;
import com.foocmend.commons.Utils;
import com.foocmend.commons.constants.Foods;
import com.foocmend.commons.validators.EditInfoValidator;
import com.foocmend.entities.BoardData;
import com.foocmend.entities.Member;
import com.foocmend.entities.Restaurant;
import com.foocmend.repositories.MemberRepository;
import com.foocmend.services.board.InfoBoardDataService;
import com.foocmend.services.member.SaveMemberService;
import com.foocmend.services.restaurant.SearchRestaurantService;
import com.foocmend.services.wishlist.SearchWishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/member/mypage")
@RequiredArgsConstructor
public class MyPage {

    private final MemberRepository repository;
    private final SaveMemberService saveMemberService;
    private final EditInfoValidator editInfoValidator;
    private final SearchWishListService wishListService;
    private final InfoBoardDataService boardDataService;
    private final SearchRestaurantService restaurantService;
    private final Utils utils;
    @GetMapping
    public String myPageView(String type, Integer page, @ModelAttribute SignUpForm signUpForm,  Model model) {
        commonProcess(model);

        // 리뷰가 기본 탭으로 선택되도록
        type = Objects.requireNonNullElse(type, "review");
        model.addAttribute("type", type);

        if (type.equals("review")) {
            page = Objects.requireNonNullElse(page, 1);
            page = Utils.getNumber(page, 1);
            ListData<BoardData> data = boardDataService.getMine("review", page, 10);
            List<BoardData> items = data.getContent();
            Pagination pagination = data.getPagination();

            items.stream().forEach(this::addRestaurantInfo);

            model.addAttribute("items", items);
            model.addAttribute("pagination", pagination);
        } else if (type.equals("wishlist")) {
            List<Restaurant> items = wishListService.getRestaurants();
            model.addAttribute("items", items);
            items.stream().forEach(System.out::println);
        } else if (type.equals("myinfo")) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                Object principal = auth.getPrincipal();
                if (principal instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) principal;
                    String Email = userDetails.getUsername();

                    Member loggedInMember = repository.findByEmail(Email);

                    signUpForm.setMode("edit");
                    signUpForm.setEmail(loggedInMember.getEmail());
                    signUpForm.setNickname(loggedInMember.getNickname());
                    signUpForm.setGender(loggedInMember.getGender().toString());
                    signUpForm.setZipcode(loggedInMember.getZipcode());
                    signUpForm.setAddress(loggedInMember.getAddress());
                    signUpForm.setBirthDate(loggedInMember.getBirthDate());
                    signUpForm.setMobile(loggedInMember.getMobile());
                }
            }
        } else if (type.equals("withdraw")) {

        }

        return utils.view("member/mypage");
    }

    @GetMapping("/edit")
    public String editInfo(@ModelAttribute SignUpForm signUpForm, Model model) {
        commonProcess(model);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String Email = userDetails.getUsername();

                Member loggedInMember = repository.findByEmail(Email);

                signUpForm.setMode("edit");
                signUpForm.setEmail(loggedInMember.getEmail());
                signUpForm.setNickname(loggedInMember.getNickname());
                signUpForm.setGender(loggedInMember.getGender().toString());
                signUpForm.setZipcode(loggedInMember.getZipcode());
                signUpForm.setAddress(loggedInMember.getAddress());
                signUpForm.setBirthDate(loggedInMember.getBirthDate());
                signUpForm.setMobile(loggedInMember.getMobile());
            }
        }
        return utils.view("member/editInfo");
    }


    @PostMapping("/edit")
    public String editInfoPs(SignUpForm signUpForm, Errors errors, Model model) {
        commonProcess(model);

        editInfoValidator.validate(signUpForm, errors);

        if(errors.hasErrors()) {
            return "front/member/editInfo";
        }
        signUpForm.setMode("edit");
        saveMemberService.save(signUpForm);

        return "redirect:/member/mypage";
    }

    private void commonProcess(Model model) {
        model.addAttribute("pageTitle", "마이페이지");
        model.addAttribute("foods", Foods.getList());
        model.addAttribute("addCss", new String[] { "member/style", "member/mypage"});
        model.addAttribute("addCommonScript", new String[] {"address"});
    }

    private void addRestaurantInfo(BoardData item) {
        Long id = item.getExtraLong1();
        if (id != null) {
            item.setExtra(restaurantService.get(id));
        }
    }
}
