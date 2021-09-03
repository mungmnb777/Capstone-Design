package graduation.mazerunner.service;

import graduation.mazerunner.domain.Member;
import graduation.mazerunner.domain.Post;
import graduation.mazerunner.domain.Reply;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ReplyServiceTest {

    @Autowired ReplyService replyService;
    @Autowired MemberService memberService;
    @Autowired PostService postService;

    @Test
    public void 댓글생성() throws Exception {

        // given
        Member member = Member.builder()
                .id("mungmnb777")
                .password("asd")
                .passwordCheck("asd")
                .nickname("명범")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        String memberId = memberService.join(member);

        Post post = Post.builder()
                .title("1번")
                .content("안녕하세요")
                .member(member)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        Long postId = postService.save(post);

        Reply reply = Reply.builder()
                .content("댓글")
                .post(post)
                .member(member)
                .build();

        // when
        Reply findReply = replyService.insertReply(reply);
        // then

        //== 저장된 값 확인 ==//
        assertThat(findReply.getId()).isEqualTo(1L);
        assertThat(findReply.getContent()).isEqualTo(reply.getContent());
        assertThat(findReply.getPost()).isEqualTo(reply.getPost());
        assertThat(findReply.getMember()).isEqualTo(reply.getMember());
    }

    @Test
    public void 댓글수정() throws Exception {

        // given
        Member member = Member.builder()
                .id("mungmnb777")
                .password("asd")
                .passwordCheck("asd")
                .nickname("명범")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        String memberId = memberService.join(member);

        Post post = Post.builder()
                .title("1번")
                .content("안녕하세요")
                .member(member)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        Long postId = postService.save(post);

        Reply reply = Reply.builder()
                .content("댓글")
                .post(post)
                .member(member)
                .build();

        Reply savedReply = replyService.insertReply(reply);

        Reply updatedReply = Reply.builder()
                .id(reply.getId())
                .content("수정된 댓글")
                .build();

        // when
        Reply result = replyService.updateReply(updatedReply);

        // then
        assertThat(result.getContent()).isEqualTo("수정된 댓글");
        assertThat(result.getUdate()).isNotEqualTo(result.getCdate());
    }

    @Test
    public void 댓글삭제() throws Exception {

        // given
        Member member = Member.builder()
                .id("mungmnb777")
                .password("asd")
                .passwordCheck("asd")
                .nickname("명범")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        String memberId = memberService.join(member);

        Post post = Post.builder()
                .title("1번")
                .content("안녕하세요")
                .member(member)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        Long postId = postService.save(post);

        Reply reply = Reply.builder()
                .content("댓글")
                .post(post)
                .member(member)
                .build();

        Reply savedReply = replyService.insertReply(reply);

        // when
        replyService.deleteReply(savedReply);

        // then
        assertThat(replyService.findOne(savedReply)).isEqualTo(null);
    }
}