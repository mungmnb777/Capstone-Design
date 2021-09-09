package graduation.mazerunner.repository;

import graduation.mazerunner.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public String save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member findOne(String id){
        return em.find(Member.class, id);
    }

    public List<Member> findByIdContaining(String id) {
        return em.createQuery("select m from Member m where m.id like :id")
                .setParameter("id", "%" + id + "%")
                .getResultList();
    }

    public List<Member> findByNicknameContaining(String nickname) {
        return em.createQuery("select m from Member m where m.nickname like :nickname")
                .setParameter("nickname", "%" + nickname + "%")
                .getResultList();
    }
}
