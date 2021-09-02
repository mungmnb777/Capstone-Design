package graduation.mazerunner.repository;

import graduation.mazerunner.domain.Map;
import graduation.mazerunner.domain.Member;
import graduation.mazerunner.domain.Post;
import graduation.mazerunner.domain.Recommend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public Recommend findByMapAndMember(Map map, Member member) {
        try {
            return em.createQuery("select r from Recommend r where r.map = :map and r.member = :member", Recommend.class)
                    .setParameter("map", map)
                    .setParameter("member", member)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Recommend findByPostAndMember(Post post, Member member) {
        try {
            return em.createQuery("select r from Recommend r where r.post = :post and r.member = :member", Recommend.class)
                    .setParameter("post", post)
                    .setParameter("member", member)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Map> findPopularMapList() {
        // 인기 게임에 등록될 수 있는 최소 추천 수
        final int MIN_RECOMMEND = 1;

        // 혹시나 여기서 에러뜨면 order 앞에 띄어쓰기 붙여주세요
        return em.createQuery("select r.map " +
                        "from Recommend r inner join r.map m " +
                        "where r.status = 'ON' " +
                        "and r.udate > :yesterday " +
                        "and r.udate < :today " +
                        "group by r.map " +
                        "having count(r) >= " + MIN_RECOMMEND +
                        "order by count(r) desc, m.hit desc", Map.class)
                .setParameter("yesterday", LocalDateTime.now().minusDays(1))
                .setParameter("today", LocalDateTime.now())
                .getResultList();
    }
}
