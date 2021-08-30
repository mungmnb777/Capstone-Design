package graduation.mazerunner.repository;

import graduation.mazerunner.Paging;
import graduation.mazerunner.domain.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MapRepository {

    private final EntityManager em;

    public Long save(Map map){
        em.persist(map);
        return map.getId();
    }

    public Map findOne(Long id){
        return em.find(Map.class, id);
    }

    public Long findMapsCount() {
        return em.createQuery("select count(m) from Map m", Long.class)
                .getSingleResult();
    }

    public List<Map> findPerPage(Paging paging) {
        return em.createQuery("select m from Map m order by m.cdate desc", Map.class)
                .setFirstResult(paging.getStartIndex())
                .setMaxResults(paging.getPostPerPage())
                .getResultList();
    }

    public void delete(Long id) {
        Map map = em.find(Map.class, id);
        em.remove(map);
    }
}
