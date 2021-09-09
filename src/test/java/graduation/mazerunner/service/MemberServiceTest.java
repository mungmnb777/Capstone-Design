package graduation.mazerunner.service;

import graduation.mazerunner.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    @Test
    public void 멤버검색() throws Exception {

        // given
        Member member = Member.builder()
                .id("mungmnb1234")
                .password("123")
                .passwordCheck("123")
                .nickname("나")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        memberService.join(member);
        // when
        List<Member> findMember1 = memberService.findByIdOrNicknameContaining("id","mnb");
        List<Member> findMember2 = memberService.findByIdOrNicknameContaining("id","gmnb");
        List<Member> findMember3 = memberService.findByIdOrNicknameContaining("id","mungmnb");
        // then
        assertThat(findMember1.get(0).getId()).isEqualTo(member.getId());
        assertThat(findMember2.get(0).getId()).isEqualTo(member.getId());
        assertThat(findMember3.get(0).getId()).isEqualTo(member.getId());
    }

    @Test
    public void 닉네임검색() throws Exception {

        // given
        Member member = Member.builder()
                .id("mungmnb1234")
                .password("123")
                .passwordCheck("123")
                .nickname("어려운닉네임1234")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        memberService.join(member);

        // when
        List<Member> findMember1 = memberService.findByIdOrNicknameContaining("nickname","려운");
        List<Member> findMember2 = memberService.findByIdOrNicknameContaining("nickname","운닉네");
        List<Member> findMember3 = memberService.findByIdOrNicknameContaining("nickname","어려운닉네임");

        // then
        assertThat(findMember1.get(0).getNickname()).isEqualTo(member.getNickname());
        assertThat(findMember2.get(0).getNickname()).isEqualTo(member.getNickname());
        assertThat(findMember3.get(0).getNickname()).isEqualTo(member.getNickname());
    }
}