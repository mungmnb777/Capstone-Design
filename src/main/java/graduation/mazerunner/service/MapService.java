package graduation.mazerunner.service;

import graduation.mazerunner.Paging;
import graduation.mazerunner.constant.ModeConst;
import graduation.mazerunner.domain.Map;
import graduation.mazerunner.domain.Ranking;
import graduation.mazerunner.repository.MapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MapService {

    private final MapRepository mapRepository;

    public void test(Map map) {

        // 스타트 지점이 입력되지 않았을 경우
        if (map.getContent().indexOf(String.valueOf(ModeConst.MODE_START)) == -1) {
            throw new IllegalArgumentException("스타트 지점을 입력해야 합니다.");
        }

        // 도착 지점이 입력되지 않았을 경우
        if (map.getContent().indexOf(String.valueOf(ModeConst.MODE_GOAL)) == -1) {
            throw new IllegalArgumentException("도착 지점을 입력해야 합니다.");
        }
    }

    public Long save(Map map) {
        return mapRepository.save(map);
    }

    public Map load(Long id) {
        Map findMap = mapRepository.findOne(id);

        if (findMap == null) {
            throw new RuntimeException("맵 정보가 존재하지 않습니다.");
        }

        findMap.setHit(findMap.getHit() + 1);

        return findMap;
    }

    public Map findOne(Long id) {
        return mapRepository.findOne(id);
    }

    public Long findMapsCount() {
        return mapRepository.findMapsCount();
    }

    // 게임 클리어
    public void clear(Long id, Ranking ranking) {
        Map findMap = mapRepository.findOne(id);
        findMap.getRankings().add(ranking);
    }

    public List<Map> findPerPage(Paging paging) {
        return mapRepository.findPerPage(paging);
    }
}
