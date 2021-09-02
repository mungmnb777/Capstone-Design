package graduation.mazerunner.service;

import graduation.mazerunner.domain.*;
import graduation.mazerunner.repository.MapRepository;
import graduation.mazerunner.repository.MemberRepository;
import graduation.mazerunner.repository.PostRepository;
import graduation.mazerunner.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {

    private final MapRepository mapRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final RecommendRepository recommendRepository;

    // 게시물을 처음 들어갔을 때 INSERT
    public Long save(Recommend recommend) {
        return recommendRepository.save(recommend);
    }

    public Recommend findByMapAndMember(Long mapId, String memberId) {

        Map findMap = mapRepository.findOne(mapId);
        Member findMember = memberRepository.findOne(memberId);

        if (findMap == null || findMember == null) {
            return null;
        }

        Recommend findRecommend = recommendRepository.findByMapAndMember(findMap, findMember);

        if (findRecommend == null) {
            Recommend newRecommend = Recommend.builder()
                    .map(findMap)
                    .member(findMember)
                    .status(RecommendStatus.OFF)
                    .cdate(LocalDateTime.now())
                    .udate(LocalDateTime.now())
                    .build();

            recommendRepository.save(newRecommend);
            return newRecommend;
        }

        return findRecommend;
    }

    // Map 추천 누르거나 취소
    public Recommend recommendMap(Long mapId, String memberId) {

        Map findMap = mapRepository.findOne(mapId);
        Member findMember = memberRepository.findOne(memberId);

        if (findMap == null || findMember == null) {
            return null;
        }

        Recommend findRecommend = recommendRepository.findByMapAndMember(findMap, findMember);

        RecommendStatus currentStatus = findRecommend.changeStatus();

        if (currentStatus == RecommendStatus.ON) {
            findRecommend.getMap().increaseRecommend();
        } else if (currentStatus == RecommendStatus.OFF) {
            findRecommend.getMap().decreaseRecommend();
        }

        return findRecommend;
    }

    public Recommend findByPostAndMember(Long postId, String memberId) {

        Post findPost = postRepository.findOne(postId);
        Member findMember = memberRepository.findOne(memberId);

        if (findPost == null || findMember == null) {
            return null;
        }

        Recommend findRecommend = recommendRepository.findByPostAndMember(findPost, findMember);

        if (findRecommend == null) {
            Recommend newRecommend = Recommend.builder()
                    .post(findPost)
                    .member(findMember)
                    .status(RecommendStatus.OFF)
                    .cdate(LocalDateTime.now())
                    .udate(LocalDateTime.now())
                    .build();

            recommendRepository.save(newRecommend);
            return newRecommend;
        }

        return findRecommend;
    }

    // Post 추천 누르거나 취소
    public Recommend recommendPost(Long postId, String memberId) {
        Post findPost = postRepository.findOne(postId);
        Member findMember = memberRepository.findOne(memberId);

        if (findPost == null || findMember == null) {
            return null;
        }

        Recommend findRecommend = recommendRepository.findByPostAndMember(findPost, findMember);

        RecommendStatus currentStatus = findRecommend.changeStatus();

        if (currentStatus == RecommendStatus.ON) {
            findRecommend.getPost().increaseRecommend();
        } else if (currentStatus == RecommendStatus.OFF) {
            findRecommend.getPost().decreaseRecommend();
        }

        return findRecommend;
    }

    // 일간 인기 게시글 가져오기
    public List<Map> findByPopularMapList() {
        return recommendRepository.findPopularMapList();
    }
}
