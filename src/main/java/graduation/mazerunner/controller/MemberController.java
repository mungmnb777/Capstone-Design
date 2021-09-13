package graduation.mazerunner.controller;

import graduation.mazerunner.constant.SessionConst;
import graduation.mazerunner.controller.form.LoginForm;
import graduation.mazerunner.controller.form.SignupForm;
import graduation.mazerunner.domain.Map;
import graduation.mazerunner.domain.Member;
import graduation.mazerunner.domain.Post;
import graduation.mazerunner.domain.Reply;
import graduation.mazerunner.service.MapService;
import graduation.mazerunner.service.MemberService;
import graduation.mazerunner.service.PostService;
import graduation.mazerunner.service.ReplyService;
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
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MapService mapService;
    private final PostService postService;
    private final ReplyService replyService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("form", new LoginForm());
        return "members/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("form") LoginForm form, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {
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

            return "redirect:" + redirectURL;

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

    @GetMapping("/mypage")
    public String myPage(Model model, HttpSession session) {
        String memberId = ((Member) session.getAttribute(SessionConst.LOGIN_MEMBER)).getId();

        List<Map> recentMaps = mapService.findRecentMaps(memberId);
        List<Post> recentPosts = postService.findRecentPosts(memberId);
        List<Reply> recentReplies = replyService.findRecentReplies(memberId);

        model.addAttribute("recentMaps", recentMaps);
        model.addAttribute("recentPosts", recentPosts);
        model.addAttribute("recentReplies", recentReplies);

        return "members/mypage";
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
