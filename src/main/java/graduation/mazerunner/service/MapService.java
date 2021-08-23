package graduation.mazerunner.service;

import graduation.mazerunner.constant.ModeConst;
import graduation.mazerunner.domain.Map;
import graduation.mazerunner.repository.MapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Map load(Map map) {
        Map findMap = mapRepository.findOne(map.getId());

        if (findMap == null) {
            throw new RuntimeException("맵 정보가 존재하지 않습니다.");
        }

        return findMap;
    }
}
