package graduation.mazerunner.repository;

import graduation.mazerunner.domain.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ReplyRepository {

    private final EntityManager em;

    public Reply save(Reply reply) {
        em.persist(reply);

        return em.find(Reply.class, reply.getId());
    }

    public Reply findOne(Long id) {
        return em.find(Reply.class, id);
    }

    public void delete(Long id) {
        Reply reply = em.find(Reply.class, id);
        em.remove(reply);
    }
}
