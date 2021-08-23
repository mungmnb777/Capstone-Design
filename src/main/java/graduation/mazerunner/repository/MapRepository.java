package graduation.mazerunner.repository;

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

    public List<Map> findAll() {
        return em.createQuery("select m from Map m", Map.class)
                .getResultList();
    }
}
