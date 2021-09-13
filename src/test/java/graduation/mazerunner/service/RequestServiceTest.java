package graduation.mazerunner.service;

import graduation.mazerunner.Paging;
import graduation.mazerunner.domain.*;
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
class RequestServiceTest {

    @Autowired RequestService requestService;
    @Autowired MemberService memberService;

    @Test
    public void 친구요청() throws Exception {

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

        FriendRequest request = FriendRequest.builder()
                .member(member)
                .friend(friend)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .status(RequestStatus.REQUEST)
                .build();

        // when
        Long savedId = requestService.requestFriend(request);
        FriendRequest findRequest = requestService.findOne(savedId);

        // then
        assertThat(savedId).isEqualTo(request.getId());
        assertThat(findRequest.getMember()).isEqualTo(request.getMember());
        assertThat(findRequest.getFriend()).isEqualTo(request.getFriend());
        assertThat(findRequest.getStatus()).isEqualTo(RequestStatus.REQUEST);
    }

    @Test
    public void 친구보낸요청조회() throws Exception {

        // given
        Member member = Member.builder()
                .id("mungmnb1234")
                .password("123")
                .passwordCheck("123")
                .nickname("나")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        Member friend1 = Member.builder()
                .id("mungmnb2345")
                .password("123")
                .passwordCheck("123")
                .nickname("친구1")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        Member friend2 = Member.builder()
                .id("mungmnb3456")
                .password("123")
                .passwordCheck("123")
                .nickname("친구2")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        memberService.join(member);
        memberService.join(friend1);
        memberService.join(friend2);

        FriendRequest request1 = FriendRequest.builder()
                .member(member)
                .friend(friend1)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .status(RequestStatus.REQUEST)
                .build();

        FriendRequest request2 = FriendRequest.builder()
                .member(member)
                .friend(friend2)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .status(RequestStatus.REQUEST)
                .build();

        requestService.requestFriend(request1);
        requestService.requestFriend(request2);

        Paging paging = new Paging(requestService.findSendRequestsCount(member.getId()).intValue(), 1);

        // when
        List<FriendRequest> sendRequestList = requestService.getSendRequestList(member.getId(), paging);

        // then
        assertThat(sendRequestList.size()).isEqualTo(2);
    }

    @Test
    public void 받은친구요청조회() throws Exception {

        // given
        Member member = Member.builder()
                .id("mungmnb1234")
                .password("123")
                .passwordCheck("123")
                .nickname("나")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        Member friend1 = Member.builder()
                .id("mungmnb2345")
                .password("123")
                .passwordCheck("123")
                .nickname("친구1")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        Member friend2 = Member.builder()
                .id("mungmnb3456")
                .password("123")
                .passwordCheck("123")
                .nickname("친구2")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        memberService.join(member);
        memberService.join(friend1);
        memberService.join(friend2);

        FriendRequest request1 = FriendRequest.builder()
                .member(member)
                .friend(friend1)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .status(RequestStatus.REQUEST)
                .build();

        FriendRequest request2 = FriendRequest.builder()
                .member(member)
                .friend(friend2)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .status(RequestStatus.REQUEST)
                .build();

        requestService.requestFriend(request1);
        requestService.requestFriend(request2);

        Paging paging1 = new Paging(requestService.findReceiveRequestsCount(friend1.getId()).intValue(), 1);
        Paging paging2 = new Paging(requestService.findReceiveRequestsCount(friend2.getId()).intValue(), 1);

        // when
        List<FriendRequest> receiveRequestList1 = requestService.getReceiveRequestList(friend1.getId(), paging1);
        List<FriendRequest> receiveRequestList2 = requestService.getReceiveRequestList(friend2.getId(), paging2);

        // then
        assertThat(receiveRequestList1.size()).isEqualTo(1);
        assertThat(receiveRequestList2.size()).isEqualTo(1);
        assertThat(receiveRequestList1.get(0).getFriend().getId()).isEqualTo(friend1.getId());
        assertThat(receiveRequestList2.get(0).getFriend().getId()).isEqualTo(friend2.getId());
    }

    @Test
    public void 친구요청수락() throws Exception {

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

        FriendRequest request = FriendRequest.builder()
                .member(member)
                .friend(friend)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .status(RequestStatus.REQUEST)
                .build();

        Long savedId = requestService.requestFriend(request);


        // when
        FriendRequest acceptRequest = requestService.acceptRequest(savedId);

        // then
        assertThat(acceptRequest.getStatus()).isEqualTo(RequestStatus.ACCEPT);
        assertThat(acceptRequest.getUdate()).isNotEqualTo(acceptRequest.getCdate());
    }

    @Test
    public void 친구요청거절() throws Exception {

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

        FriendRequest request = FriendRequest.builder()
                .member(member)
                .friend(friend)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .status(RequestStatus.REQUEST)
                .build();

        Long savedId = requestService.requestFriend(request);

        // when
        FriendRequest acceptRequest = requestService.rejectRequest(savedId);

        // then
        assertThat(acceptRequest.getStatus()).isEqualTo(RequestStatus.REJECT);
        assertThat(acceptRequest.getUdate()).isNotEqualTo(acceptRequest.getCdate());
    }

    @Test
    public void 친구추가요청취소() throws Exception {

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

        FriendRequest request = FriendRequest.builder()
                .member(member)
                .friend(friend)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .status(RequestStatus.REQUEST)
                .build();

        Long savedId = requestService.requestFriend(request);

        // when
        requestService.cancelRequest(savedId);

        // then
        assertThat(requestService.findOne(savedId)).isNull();
    }

    @Test
    public void 중복요청확인() throws Exception {

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

        FriendRequest request1 = FriendRequest.builder()
                .member(member)
                .friend(friend)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .status(RequestStatus.REQUEST)
                .build();

        FriendRequest request2 = FriendRequest.builder()
                .member(member)
                .friend(friend)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .status(RequestStatus.REQUEST)
                .build();

        requestService.requestFriend(request1);

        // when

        // then
        assertThrows(IllegalStateException.class, () -> requestService.requestFriend(request2));
    }
}