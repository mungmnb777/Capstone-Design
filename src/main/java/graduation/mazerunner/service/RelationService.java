package graduation.mazerunner.service;

import graduation.mazerunner.Paging;
import graduation.mazerunner.domain.RelationStatus;
import graduation.mazerunner.domain.Relationship;
import graduation.mazerunner.repository.RelationRepository;
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
public class RelationService {

    private final RelationRepository relationRepository;

    /**
     * 친구추가
     */
    public Long addFriend(Relationship userRelationship) {
        Relationship friendRelationship = Relationship.builder()
                .member(userRelationship.getFriend())
                .friend(userRelationship.getMember())
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .status(RelationStatus.FRIEND)
                .build();

        relationRepository.save(friendRelationship);

        return relationRepository.save(userRelationship);
    }

    /**
     * 친구 목록 조회
     */
    public List<Relationship> getFriends(String memberId, Paging paging) {
        return relationRepository.findFriends(memberId, paging);
    }

    /**
     * 차단한 친구 목록 조회
     */
    public List<Relationship> getBlockedFriends(String memberId, Paging paging) {
        return relationRepository.findBlockFriends(memberId, paging);
    }

    /**
     * 차단
     */
    public void block(Long id) {
        Relationship findRelationship = relationRepository.findOne(id);
        findRelationship.updateStatus(RelationStatus.BLOCK);
    }

    // 페이징을 위한 DB 내 Request 레코드 수 조회
    public Long findRelationshipsCount() {
        return relationRepository.findRelationshipsCount();
    }

    public boolean isRelated(String memberId, String friendId) {
        List<Relationship> relationships = relationRepository.relatedFriend(memberId, friendId);

        if (relationships.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 차단 해제 혹은 친구 삭제
     */
    public void delete(Long id) {
        relationRepository.delete(id);
    }
}
