package graduation.mazerunner.repository;

import graduation.mazerunner.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {

        // given
        Member member = Member.builder()
                .id("mungmnb777")
                .build();
        // when
        String savedId = memberRepository.save(member);
        // then
        assertThat(savedId).isEqualTo(member.getId());
    }
}