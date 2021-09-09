package graduation.mazerunner.repository;

import graduation.mazerunner.Paging;
import graduation.mazerunner.domain.RelationStatus;
import graduation.mazerunner.domain.Relationship;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RelationRepository {

    private final EntityManager em;

    public Long save(Relationship relationship) {
        em.persist(relationship);
        return relationship.getId();
    }

    public Relationship findOne(Long id) {
        return em.find(Relationship.class, id);
    }

    public List<Relationship> findFriends(String memberId, Paging paging) {
        return em.createQuery("select r from Relationship r " +
                        "where r.member.id = :memberId and r.status = :status order by r.cdate")
                .setParameter("memberId", memberId)
                .setParameter("status", RelationStatus.FRIEND)
                .setFirstResult(paging.getStartIndex())
                .setMaxResults(paging.getPostPerPage())
                .getResultList();
    }

    public List<Relationship> findBlockFriends(String memberId, Paging paging) {
        return em.createQuery("select r from Relationship r " +
                        "where r.member.id = :memberId and r.status = :status order by r.cdate")
                .setParameter("memberId", memberId)
                .setParameter("status", RelationStatus.BLOCK)
                .setFirstResult(paging.getStartIndex())
                .setMaxResults(paging.getPostPerPage())
                .getResultList();
    }

    public List<Relationship> relatedFriend(String memberId, String friendId) {
        return em.createQuery("select r from Relationship r " +
                        "where r.member.id = :memberId and r.friend.id = :friendId")
                .setParameter("memberId", memberId)
                .setParameter("friendId", friendId)
                .getResultList();
    }

    // 페이징을 위한 현재 DB 총 RELATIONSHIP 레코드 수 조회
    public Long findRelationshipsCount() {
        return em.createQuery("select count(r) from Relationship r", Long.class)
                .getSingleResult();
    }

    public void delete(Long id) {
        Relationship findRelationship = em.find(Relationship.class, id);
        em.remove(findRelationship);
    }
}
