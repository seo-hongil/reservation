package project.toy.reservation.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.toy.reservation.member.dto.SignupRequest;
import project.toy.reservation.member.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @GetMapping("/member/signup")
    public String signupForm(Model model) {
        model.addAttribute("signUpRequest", new SignupRequest());
        return "member/signup";
    }

    @PostMapping("/member/signup")
    public String signup(@Valid @ModelAttribute SignupRequest signUpRequest,
                               BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "member/signup";
        }

        try {
            memberService.register(signUpRequest);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/signup";
        }

        return "redirect:/member/login";
    }

    // 로그인
    @GetMapping("/member/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model,
                            @CookieValue(value = "rememberId", required = false) String rememberedId) {

        if (error != null) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 잘못되었습니다.");
        }

        model.addAttribute("rememberedId", rememberedId);

        return "member/login";
    }


}
