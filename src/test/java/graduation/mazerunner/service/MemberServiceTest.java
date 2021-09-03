package graduation.mazerunner.service;

import graduation.mazerunner.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;

    @Test
    public void 회원가입() throws Exception {

        // given
        Member member = Member.builder()
                .id("mungmnb777")
                .password("123")
                .passwordCheck("123")
                .build();

        // when
        String savedId = memberService.join(member);
        // then
        assertThat(savedId).isEqualTo(member.getId());
    }

    @Test
    public void 회원가입_중복아이디검증() throws Exception {

        // given
        Member member1 = Member.builder()
                .id("mungmnb777")
                .password("asd")
                .passwordCheck("asd")
                .build();

        Member member2 = Member.builder()
                .id("mungmnb777")
                .password("asd")
                .passwordCheck("asd")
                .build();

        // when
        memberService.join(member1);

        // then
        assertThrows(IllegalArgumentException.class, () -> memberService.join(member2));
    }
    
    @Test
    public void 회원가입_비밀번호일치여부검증() throws Exception {
        
        // given
        Member member = Member.builder()
                .id("mungmnb777")
                .password("qwer123")
                .passwordCheck("qwer124")
                .build();

        // when

        // then
        assertThrows(IllegalArgumentException.class, () -> memberService.join(member));
    }

    @Test
    public void 로그인() throws Exception {

        // given
        Member join = Member.builder()
                .id("mungmnb777")
                .password("qwer123")
                .passwordCheck("qwer123")
                .build();

        memberService.join(join);

        Member login = Member.builder()
                .id("mungmnb777")
                .password("qwer123")
                .build();

        // when
        Member loginMember = memberService.login(login);

        // then
        assertThat(loginMember.getId()).isEqualTo(login.getId());
        assertThat(loginMember.getPassword()).isEqualTo(login.getPassword());
    }

    @Test
    public void 아이디조회실패() throws Exception {

        // given
        Member join = Member.builder()
                .id("mungmnb777")
                .password("qwer123")
                .passwordCheck("qwer123")
                .build();

        memberService.join(join);


        Member login = Member.builder()
                .id("mungmnb77")
                .password("qwer123")
                .build();
        // when

        // then
        assertThrows(IllegalArgumentException.class, () -> memberService.login(login));
    }

    @Test
    public void 비밀번호검증() throws Exception {

        // given
        Member join = Member.builder()
                .id("mungmnb777")
                .password("qwer123")
                .passwordCheck("qwer123")
                .build();

        memberService.join(join);

        Member login = Member.builder()
                .id("mungmnb777")
                .password("qwer1234")
                .build();
        // when

        // then
        assertThrows(IllegalArgumentException.class, () -> memberService.login(login));
    }
}