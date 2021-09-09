package graduation.mazerunner.controller;

import graduation.mazerunner.constant.SessionConst;
import graduation.mazerunner.controller.form.RequestForm;
import graduation.mazerunner.controller.form.SearchForm;
import graduation.mazerunner.domain.FriendRequest;
import graduation.mazerunner.domain.Member;
import graduation.mazerunner.domain.RequestStatus;
import graduation.mazerunner.service.MemberService;
import graduation.mazerunner.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/relations")
@RequiredArgsConstructor
public class RelationController {

    private final MemberService memberService;
    private final RequestService requestService;

    @GetMapping("/search")
    public String getSearchMemberForm(Model model) {
        model.addAttribute("searchForm", new SearchForm());

        return "relations/search";
    }

    @GetMapping("/search/result")
    public String searchMember(@ModelAttribute("searchForm") SearchForm form, Model model) {
        List<Member> findMemberList = memberService.findByIdOrNicknameContaining(form.getType(), form.getInputValue());

        model.addAttribute("memberList", findMemberList);
        model.addAttribute("searchForm", form);
        model.addAttribute("requestForm", new RequestForm());
        return "relations/searchResult";
    }

    @PostMapping("/search/result")
    public String requestMember(
            @Validated @ModelAttribute("searchForm") SearchForm searchForm, BindingResult br1,
            @Validated @ModelAttribute("requestForm") RequestForm requestForm, BindingResult bindingResult,
            HttpSession session, Model model) {
        try {
            Member findMember = memberService.findOne(requestForm.getId());

            FriendRequest paramRequest = FriendRequest.builder()
                    .member((Member) session.getAttribute(SessionConst.LOGIN_MEMBER))
                    .friend(findMember)
                    .cdate(LocalDateTime.now())
                    .udate(LocalDateTime.now())
                    .status(RequestStatus.REQUEST)
                    .build();

            requestService.requestFriend(paramRequest);

            return "redirect:/";

        } catch (IllegalStateException e) {
            List<Member> findMemberList = memberService.findByIdOrNicknameContaining(searchForm.getType(), searchForm.getInputValue());

            model.addAttribute("memberList", findMemberList);
            model.addAttribute("searchForm", searchForm);
            model.addAttribute("requestForm", new RequestForm());

            log.info("message = {}", e.getMessage());
            bindingResult.reject("IllegalState", e.getMessage());
            return "relations/searchResult";
        }
    }
}
