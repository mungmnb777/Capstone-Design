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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MapService {

    private final MapRepository mapRepository;

    public void test(Map map) {

        // 스타트 지점이 입력되지 않았을 경우
        if (!map.getContent().contains(String.valueOf(ModeConst.MODE_START))) {
            throw new IllegalArgumentException("스타트 지점을 입력해야 합니다.");
        }

        // 도착 지점이 입력되지 않았을 경우
        if (!map.getContent().contains(String.valueOf(ModeConst.MODE_GOAL))) {
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

        findMap.increaseHit();

        return findMap;
    }

    public Map findOne(Long id) {
        return mapRepository.findOne(id);
    }

    public Long findMapsCount() {
        return mapRepository.findMapsCount();
    }

    // 게임 클리어
    public void insertRanking(Long id, Ranking ranking) {
        List<Ranking> findRankings = mapRepository.findOne(id).getRankings();
        findRankings.add(ranking);
    }

    public void cleanRank(Long id, Ranking ranking) {
        List<Ranking> findRankings = mapRepository.findOne(id).getRankings();

        // 한 명의 멤버 당 하나의 랭킹만 가질 수 있다.
        // 가장 낮은 타이머의 수치를 가진 하나의 랭킹을 제외하고 다 제거
        List<Ranking> filteredRankings = findRankings.stream()
                .filter(r -> isEquals(ranking, r))
                .sorted(Comparator.comparing(Ranking::getTimer))
                .collect(Collectors.toList());

        filteredRankings.remove(0);

        while (!filteredRankings.isEmpty()) {
            findRankings.remove(filteredRankings.remove(0));
        }

        // 하나의 맵에는 최대 10개의 랭킹을 조회할 수 있다.
        // 10개 이상이 되면 가장 높은 타이머의 수치를 제거한다.
        findRankings.sort(Comparator.comparing(Ranking::getTimer).reversed());

        while (findRankings.size() > 10) {
            findRankings.remove(0);
        }
    }
    private boolean isEquals(Ranking ranking, Ranking r) {
        return ranking.getMember().getId()
                .equals(r.getMember().getId());
    }

    public List<Map> findPerPage(Paging paging) {
        return mapRepository.findPerPage(paging);
    }

    public void delete(Long id) {
        mapRepository.delete(id);
    }
}
