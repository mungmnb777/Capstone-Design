package graduation.mazerunner.controller;

import graduation.mazerunner.constant.SessionConst;
import graduation.mazerunner.controller.form.ReplyForm;
import graduation.mazerunner.domain.Member;
import graduation.mazerunner.domain.Post;
import graduation.mazerunner.domain.Reply;
import graduation.mazerunner.service.PostService;
import graduation.mazerunner.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/board/list/{postId}")
@RequiredArgsConstructor
public class ReplyController {

    private final PostService postService;
    private final ReplyService replyService;

    @PostMapping("/reply/insert")
    public ReplyForm insertReply(@RequestBody ReplyForm form,
                                 @PathVariable(value = "postId") Long postId,
                                 HttpSession session) {

        Post findPost = postService.findOne(postId);

        if (findPost == null) {
            throw new RuntimeException();
        }
        if (session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new RuntimeException();
        }

        Reply newReply = Reply.builder()
                .content(form.getContent())
                .post(findPost)
                .member((Member) session.getAttribute(SessionConst.LOGIN_MEMBER))
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        Reply savedReply = replyService.insertReply(newReply);

        // DTO 생성
        ReplyForm replyForm = new ReplyForm();
        replyForm.setId(savedReply.getId());
        replyForm.setContent(savedReply.getContent());
        replyForm.setMember(savedReply.getMember());

        return replyForm;
    }

    @PostMapping("/reply/list/{replyId}/update")
    public void updateReply(@RequestBody ReplyForm form,
                            @PathVariable Long replyId,
                            HttpSession session) {

        // 로그인 된 회원이 수정을 요청했는지 검증
        if (!replyService.findOne(replyId).getMember().getId()
                .equals(((Member)session.getAttribute(SessionConst.LOGIN_MEMBER)).getId())) {
            throw new RuntimeException();
        }

        Reply paramReply = Reply.builder()
                .id(replyId)
                .content(form.getContent())
                .build();

        // 수정 요청
        Reply updatedReply = replyService.updateReply(paramReply);

        // DTO 생성
        ReplyForm replyForm = new ReplyForm();
        replyForm.setContent(updatedReply.getContent());
    }

    @PostMapping("/reply/list/{replyId}/delete")
    public boolean deleteReply(@PathVariable Long replyId,
                               HttpSession session) {

        // 로그인 된 회원이 수정을 요청했는지 검증
        if (!replyService.findOne(replyId).getMember().getId()
                .equals(((Member)session.getAttribute(SessionConst.LOGIN_MEMBER)).getId())) {
            return false;
        }

        Reply paramReply = Reply.builder()
                .id(replyId)
                .build();

        // 삭제 요청
        replyService.deleteReply(paramReply);

        return true;
    }
}
