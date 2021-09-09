package graduation.mazerunner.service;

import graduation.mazerunner.Paging;
import graduation.mazerunner.domain.FriendRequest;
import graduation.mazerunner.domain.RelationStatus;
import graduation.mazerunner.domain.Relationship;
import graduation.mazerunner.domain.RequestStatus;
import graduation.mazerunner.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final RelationService relationService;

    // 친구 요청
    public Long requestFriend(FriendRequest request) {
        // 로그인 하지 않고 친구 요청하는 경우
        if (request.getMember() == null) {
            throw new IllegalStateException("로그인 후 가능합니다!");
        }

        String memberId = request.getMember().getId();
        String friendId = request.getFriend().getId();

        // 본인에게 친구 추가 하는 경우
        if (memberId.equals(friendId)) {
            throw new IllegalStateException("본인에게 친구 요청은 할 수 없습니다!");
        }

        // 중복되는 요청이 있으면
        if (requestRepository.duplicateRequest(memberId, friendId).size() != 0) {
            throw new IllegalStateException("이미 요청하신 사용자입니다!");
        }

        // 이미 친구이거나 차단된 경우 (관계가 있는 경우)
        if (relationService.isRelated(memberId, friendId)) {
            throw new IllegalStateException("이미 친구이거나 차단 상태입니다!");
        }

        return requestRepository.save(request);
    }

    public FriendRequest findOne(Long id) {
        return requestRepository.findOne(id);
    }

    // 친구 요청 취소
    public void cancelRequest(Long id) {
        requestRepository.delete(id);
    }

    // 친구 요청 수락 => relationService에서 레코드 추가 해줘야 함.
    public FriendRequest acceptRequest(Long id) {
        FriendRequest findRequest = requestRepository.findOne(id);
        findRequest.updateStatus(RequestStatus.ACCEPT);

        Relationship relationship = Relationship.builder()
                .member(findRequest.getMember())
                .friend(findRequest.getFriend())
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .status(RelationStatus.FRIEND)
                .build();

        relationService.addFriend(relationship);
        return findRequest;
    }

    // 친구 요청 거절
    public FriendRequest rejectRequest(Long id) {
        FriendRequest findRequest = requestRepository.findOne(id);
        findRequest.updateStatus(RequestStatus.REJECT);
        return findRequest;
    }

    // 받은 요청 조회
    public List<FriendRequest> getReceiveRequestList(String memberId, Paging paging) {
        return requestRepository.findReceiveRequestList(memberId, paging);
    }

    // 보낸 요청 조회
    public List<FriendRequest> getSendRequestList(String memberId, Paging paging) {
        return requestRepository.findSendRequestList(memberId, paging);
    }

    // 페이징을 위한 DB 내 Request 레코드 수 조회
    public Long findPostsCount() {
        return requestRepository.findRequestsCount();
    }
}
