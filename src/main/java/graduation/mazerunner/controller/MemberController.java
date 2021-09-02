package graduation.mazerunner.controller;

import graduation.mazerunner.constant.SessionConst;
import graduation.mazerunner.controller.form.LoginForm;
import graduation.mazerunner.controller.form.SignupForm;
import graduation.mazerunner.domain.Member;
import graduation.mazerunner.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("form", new LoginForm());
        return "members/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("form") LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
        try {
            if (bindingResult.hasErrors()) {
                return "members/login";
            }

            Member member = Member.builder()
                    .id(form.getId())
                    .password(form.getPassword())
                    .build();

            Member loginMember = memberService.login(member);

            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

            return "redirect:/";

        } catch (IllegalArgumentException e) {

            bindingResult.reject("IllegalArgument", e.getMessage());
            return "members/login";
        }
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("form", new SignupForm());
        return "members/signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("form") SignupForm form, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return "members/signup";
            }

            Member member = Member.builder()
                    .id(form.getId())
                    .password(form.getPassword())
                    .passwordCheck(form.getPasswordCheck())
                    .nickname(form.getNickname())
                    .cdate(LocalDateTime.now())
                    .udate(LocalDateTime.now())
                    .build();

            memberService.join(member);

            return "redirect:/";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("IllegalArgument", e.getMessage());
            return "members/signup";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
