package graduation.mazerunner.repository;

import graduation.mazerunner.domain.Map;
import graduation.mazerunner.domain.Ranking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RankRepository {
    private final EntityManager em;

    public List<Ranking> findByMap(Map map){
        return em.createQuery("select r from Ranking r where r.map.id = " + map.getId() + " order by r.timer", Ranking.class)
                .getResultList();
    }
}
