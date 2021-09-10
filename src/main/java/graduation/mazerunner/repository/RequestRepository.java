package graduation.mazerunner.repository;

import graduation.mazerunner.Paging;
import graduation.mazerunner.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RequestRepository {

    private final EntityManager em;

    public Long save(FriendRequest request) {
        em.persist(request);
        return request.getId();
    }

    public FriendRequest findOne(Long id) {
        return em.find(FriendRequest.class, id);
    }

    // 친구 요청 중복 조회
    public List<FriendRequest> duplicateRequest(String memberId, String friendId) {
        return em.createQuery("select r from FriendRequest r " +
                "where r.member.id = :memberId and r.friend.id = :friendId and r.status = :status")
                .setParameter("memberId", memberId)
                .setParameter("friendId", friendId)
                .setParameter("status", RequestStatus.REQUEST)
                .getResultList();
    }

    // 보낸 친구 요청 조회
    public List<FriendRequest> findSendRequestList(String memberId, Paging paging) {
        return em.createQuery("select r from FriendRequest r " +
                        "where r.member.id = :memberId and r.status = :status order by r.cdate")
                .setParameter("memberId", memberId)
                .setParameter("status", RequestStatus.REQUEST)
                .setFirstResult(paging.getStartIndex())
                .setMaxResults(paging.getPostPerPage())
                .getResultList();
    }

    // 받은 친구 요청 조회
    public List<FriendRequest> findReceiveRequestList(String memberId, Paging paging) {
        return em.createQuery("select r from FriendRequest " +
                        "r where r.friend.id = :memberId and r.status = :status order by r.cdate")
                .setParameter("memberId", memberId)
                .setParameter("status", RequestStatus.REQUEST)
                .setFirstResult(paging.getStartIndex())
                .setMaxResults(paging.getPostPerPage())
                .getResultList();
    }

    public Long findSendRequestsCount(String memberId) {
        return em.createQuery("select count(r) from FriendRequest r where r.member.id = :memberId", Long.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }

    // 페이징을 위한 현재 DB 총 REQUEST 레코드 수 조회
    public Long findReceiveRequestsCount(String memberId) {
        return em.createQuery("select count(r) from FriendRequest r where r.friend.id = :memberId", Long.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }


    public void delete(Long id) {
        FriendRequest findRequest = em.find(FriendRequest.class, id);
        em.remove(findRequest);
    }


}
