package graduation.mazerunner.repository;

import graduation.mazerunner.domain.Map;
import graduation.mazerunner.domain.Recommend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RecommendRepository {

    private final EntityManager em;

    public Long save(Recommend recommend) {
        em.persist(recommend);
        return recommend.getId();
    }

    public Recommend findByMapAndMember(Recommend recommend) {
        log.info("map = {}", recommend.getMap().getTitle());
        log.info("member = {}", recommend.getMember().getId());

        return em.createQuery("select r from Recommend r where r.map = :map and r.member = :member", Recommend.class)
                .setParameter("map", recommend.getMap())
                .setParameter("member", recommend.getMember())
                .getSingleResult();
    }

    public List<Map> findPopularMapList() {
        return em.createQuery("select r.map " +
                        "from Recommend r inner join r.map m " +
                        "where r.status = 'ON' " +
                        "and r.udate > :yesterday " +
                        "and r.udate < :today " +
                        "group by r.map " +
                        "having count(r) > 0 " +
                        "order by count(r) desc, m.hit desc", Map.class)
                .setParameter("yesterday", LocalDateTime.now().minusDays(1))
                .setParameter("today", LocalDateTime.now())
                .getResultList();
    }
}
