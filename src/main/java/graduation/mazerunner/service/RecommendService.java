package graduation.mazerunner.service;

import graduation.mazerunner.domain.Map;
import graduation.mazerunner.domain.Recommend;
import graduation.mazerunner.domain.RecommendStatus;
import graduation.mazerunner.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendRepository recommendRepository;

    // 게시물을 처음 들어갔을 때 INSERT
    public Long save(Recommend recommend) {
        return recommendRepository.save(recommend);
    }

    // Map 추천 누르거나 취소
    public Recommend recommendMap(Recommend recommend) {
        log.info("map = {}", recommend.getMap().getTitle());
        log.info("member = {}", recommend.getMember().getId());

        Recommend findRecommend = recommendRepository.findByMapAndMember(recommend);

        RecommendStatus currentStatus = findRecommend.changeStatus();

        if (currentStatus == RecommendStatus.ON) {
            findRecommend.getMap().increaseRecommend();
        } else if (currentStatus == RecommendStatus.OFF) {
            findRecommend.getMap().decreaseRecommend();
        }


        return findRecommend;
    }

    // 일간 인기 게시글 가져오기
    public List<Map> findByPopularMapList() {
        return recommendRepository.findPopularMapList();
    }
}
