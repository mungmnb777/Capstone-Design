package graduation.mazerunner.controller;

import graduation.mazerunner.Paging;
import graduation.mazerunner.constant.SessionConst;
import graduation.mazerunner.controller.form.RelationResultForm;
import graduation.mazerunner.controller.form.RequestForm;
import graduation.mazerunner.controller.form.RequestResultForm;
import graduation.mazerunner.controller.form.SearchForm;
import graduation.mazerunner.domain.*;
import graduation.mazerunner.service.MemberService;
import graduation.mazerunner.service.RelationService;
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
    private final RelationService relationService;

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

            return "redirect:/relations/send/request";

        } catch (IllegalStateException e) {
            List<Member> findMemberList = memberService.findByIdOrNicknameContaining(searchForm.getType(), searchForm.getInputValue());

            model.addAttribute("memberList", findMemberList);
            model.addAttribute("searchForm", searchForm);
            model.addAttribute("requestForm", requestForm);

            log.info("message = {}", e.getMessage());
            bindingResult.reject("IllegalState", e.getMessage());
            return "relations/searchResult";
        }
    }

    @GetMapping("/receive/request")
    public String receiveRequestForm(Model model, @RequestParam(defaultValue = "1") int page, HttpSession session) {
        Member currentMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Long totalCnt = requestService.findReceiveRequestsCount(currentMember.getId());
        Paging paging = new Paging(totalCnt.intValue(), page);

        List<FriendRequest> findRequests = requestService.getReceiveRequestList(currentMember.getId(), paging);

        model.addAttribute("requestList", findRequests);
        model.addAttribute("paging", paging);
        model.addAttribute("requestForm", new RequestForm());
        model.addAttribute("result", new RequestResultForm());

        return "relations/receiveRequest";
    }

    @GetMapping("/send/request")
    public String sendRequestForm(Model model, @RequestParam(defaultValue = "1") int page, HttpSession session) {
        Member currentMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Long totalCnt = requestService.findSendRequestsCount(currentMember.getId());
        Paging paging = new Paging(totalCnt.intValue(), page);

        List<FriendRequest> findRequests = requestService.getSendRequestList(currentMember.getId(), paging);

        model.addAttribute("requestList", findRequests);
        model.addAttribute("paging", paging);
        model.addAttribute("requestForm", new RequestForm());
        model.addAttribute("result", new RequestResultForm());


        return "relations/sendRequest";
    }

    @PostMapping("/accept/request")
    public String acceptRequest(@ModelAttribute("result") RequestResultForm form) {
        requestService.acceptRequest(form.getId());

        return "redirect:/relations/list";
    }

    @PostMapping("/reject/request")
    public String rejectRequest(@ModelAttribute("result") RequestResultForm form) {
        requestService.rejectRequest(form.getId());

        return "redirect:/relations/receive/request";
    }

    @PostMapping("/cancel/request")
    public String cancelRequest(@ModelAttribute("result") RequestResultForm form) {
        requestService.cancelRequest(form.getId());

        return "redirect:/relations/send/request";
    }

    @GetMapping("/list")
    public String getFriendList(Model model, @RequestParam(defaultValue = "1") int page, HttpSession session) {

        Member findMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        int totalCnt = relationService.findRelationshipsCount(findMember.getId()).intValue();

        Paging paging = new Paging(totalCnt, page);

        List<Relationship> findFriendList = relationService.getFriends(findMember.getId(), paging);

        model.addAttribute("relationList", findFriendList);
        model.addAttribute("paging", paging);
        model.addAttribute("result", new RelationResultForm());

        return "relations/list";
    }

    @PostMapping("/delete")
    public String deleteRelation(@ModelAttribute("result") RelationResultForm form) {
        relationService.delete(form.getId());

        return "redirect:/relations/list";
    }

    @PostMapping("/block")
    public String blockRelation(@ModelAttribute("result") RelationResultForm form) {
        relationService.block(form.getId());

        return "redirect:/relations/list";
    }

    @PostMapping("/unblock")
    public String unblockRelation(@ModelAttribute("result") RelationResultForm form) {
        relationService.unblock(form.getId());

        return "redirect:/relations/list";
    }

    @GetMapping("/list/block")
    public  String getBlockList(Model model, @RequestParam(defaultValue = "1") int page, HttpSession session){
        Member findMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        int totalCnt = relationService.findBlockRelationshipsCount(findMember.getId()).intValue();

        Paging paging = new Paging(totalCnt, page);

        List<Relationship> findFriendList = relationService.getBlockedFriends(findMember.getId(), paging);

        model.addAttribute("relationList", findFriendList);
        model.addAttribute("paging", paging);
        model.addAttribute("result", new RelationResultForm());

        return "relations/blockList";
    }
}
