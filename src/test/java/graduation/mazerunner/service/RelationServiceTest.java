package graduation.mazerunner.service;

import graduation.mazerunner.Paging;
import graduation.mazerunner.domain.Member;
import graduation.mazerunner.domain.RelationStatus;
import graduation.mazerunner.domain.Relationship;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class RelationServiceTest {

    @Autowired MemberService memberService;
    @Autowired RelationService relationService;
    
    @Test
    public void 친구목록조회() throws Exception {
        
        // given
        Member member = Member.builder()
                .id("mungmnb1234")
                .password("123")
                .passwordCheck("123")
                .nickname("나")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        Member friend = Member.builder()
                .id("mungmnb2345")
                .password("123")
                .passwordCheck("123")
                .nickname("친구")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        memberService.join(member);
        memberService.join(friend);

        Relationship relationship = Relationship.builder()
                .member(member)
                .friend(friend)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .status(RelationStatus.FRIEND)
                .build();

        Long savedId = relationService.addFriend(relationship);

        Paging paging = new Paging(relationService.findRelationshipsCount(member.getId()).intValue(), 1);

        // when
        List<Relationship> findFriendList = relationService.getFriends(relationship.getMember().getId(), paging);

        // then
        assertThat(findFriendList.get(0).getId()).isEqualTo(savedId);
        assertThat(findFriendList.get(0).getStatus()).isEqualTo(relationship.getStatus());
        assertThat(findFriendList.get(0).getStatus()).isEqualTo(RelationStatus.FRIEND);
    }

    @Test
    public void 차단한친구목록조회() throws Exception {

        // given
        Member member = Member.builder()
                .id("mungmnb1234")
                .password("123")
                .passwordCheck("123")
                .nickname("나")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        Member friend = Member.builder()
                .id("mungmnb2345")
                .password("123")
                .passwordCheck("123")
                .nickname("친구")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        memberService.join(member);
        memberService.join(friend);

        Relationship relationship = Relationship.builder()
                .member(member)
                .friend(friend)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .status(RelationStatus.FRIEND)
                .build();

        Long savedId = relationService.addFriend(relationship);

        relationService.block(savedId);

        Paging paging = new Paging(relationService.findRelationshipsCount(member.getId()).intValue(), 1);

        // when
        List<Relationship> blockedFriends = relationService.getBlockedFriends(relationship.getMember().getId(), paging);

        // then
        assertThat(blockedFriends.get(0).getId()).isEqualTo(relationship.getId());
        assertThat(blockedFriends.get(0).getStatus()).isEqualTo(relationship.getStatus());
        assertThat(blockedFriends.get(0).getStatus()).isEqualTo(RelationStatus.BLOCK);
    }
}