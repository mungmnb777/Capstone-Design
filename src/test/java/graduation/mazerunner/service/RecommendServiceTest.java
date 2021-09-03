package graduation.mazerunner.service;

import graduation.mazerunner.domain.Map;
import graduation.mazerunner.domain.Member;
import graduation.mazerunner.domain.Recommend;
import graduation.mazerunner.domain.RecommendStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class RecommendServiceTest {

    @Autowired RecommendService recommendService;
    @Autowired MemberService memberService;
    @Autowired MapService mapService;

    @Test
    public void 추천저장() throws Exception {

        // given
        Member member = Member.builder()
                .id("mungmnb777")
                .password("asd")
                .passwordCheck("asd")
                .nickname("ㅋㅋ")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        memberService.join(member);

        Map map = Map.builder()
                .title("123")
                .content("2300")
                .height(2)
                .width(2)
                .breakCount(0)
                .member(member)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        mapService.save(map);

        Recommend recommend = Recommend.builder()
                .member(member)
                .map(map)
                .status(RecommendStatus.OFF)
                .build();

        // when
        Long savedId = recommendService.save(recommend);


        // then
        assertThat(recommend.getId()).isEqualTo(savedId);
    }

    @Test
    public void 맵추천() throws Exception {

        // given
        Member member = Member.builder()
                .id("mungmnb777")
                .password("asd")
                .passwordCheck("asd")
                .nickname("ㅋㅋ")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        String memberId = memberService.join(member);

        Map map = Map.builder()
                .title("123")
                .content("2300")
                .height(2)
                .width(2)
                .breakCount(0)
                .hit(0)
                .recommend(0)
                .member(member)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        Long mapId = mapService.save(map);

        Recommend recommend = Recommend.builder()
                .member(member)
                .map(map)
                .status(RecommendStatus.OFF)
                .build();

        recommendService.save(recommend);
        // when
        Recommend recommended = recommendService.recommendMap(mapId, memberId);

        // then
        assertThat(recommended.getStatus()).isEqualTo(RecommendStatus.ON);
        assertThat(recommended.getMap().getRecommend()).isEqualTo(1);
    }

    @Test
    public void 맵추천취소() throws Exception {

        // given
        Member member = Member.builder()
                .id("mungmnb777")
                .password("asd")
                .passwordCheck("asd")
                .nickname("ㅋㅋ")
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        String memberId = memberService.join(member);

        Map map = Map.builder()
                .title("123")
                .content("2300")
                .height(2)
                .width(2)
                .breakCount(0)
                .hit(0)
                .recommend(1)
                .member(member)
                .cdate(LocalDateTime.now())
                .udate(LocalDateTime.now())
                .build();

        Long mapId = mapService.save(map);

        Recommend recommend = Recommend.builder()
                .member(member)
                .map(map)
                .status(RecommendStatus.ON)
                .build();

        recommendService.save(recommend);
        // when
        Recommend recommended = recommendService.recommendMap(mapId, memberId);

        // then
        assertThat(recommended.getStatus()).isEqualTo(RecommendStatus.OFF);
        assertThat(recommended.getMap().getRecommend()).isEqualTo(0);
    }

    @Test
    @Rollback(false)
    public void 인기게임목록() throws Exception {

        // given
        Member member1 = Member.builder().id("member1").password("asd").passwordCheck("asd").build();
        Member member2 = Member.builder().id("member2").password("asd").passwordCheck("asd").build();
        Member member3 = Member.builder().id("member3").password("asd").passwordCheck("asd").build();
        Member member4 = Member.builder().id("member4").password("asd").passwordCheck("asd").build();
        Member member5 = Member.builder().id("member5").password("asd").passwordCheck("asd").build();

        memberService.join(member1);
        memberService.join(member2);
        memberService.join(member3);
        memberService.join(member4);
        memberService.join(member5);

        List<Member> members = new ArrayList<>();

        members.add(member1);
        members.add(member2);
        members.add(member3);
        members.add(member4);
        members.add(member5);

        Map map1 = Map.builder().title("1234").content("2300").recommend(1).member(member1).build();
        Map map2 = Map.builder().title("1234").content("2300").recommend(2).member(member2).build();
        Map map3 = Map.builder().title("1234").content("2300").recommend(3).member(member3).build();
        Map map4 = Map.builder().title("1234").content("2300").recommend(4).member(member4).build();
        Map map5 = Map.builder().title("1234").content("2300").recommend(5).member(member5).build();

        mapService.save(map1);
        mapService.save(map2);
        mapService.save(map3);
        mapService.save(map4);
        mapService.save(map5);

        List<Map> maps = new ArrayList<>();

        maps.add(map1);
        maps.add(map2);
        maps.add(map3);
        maps.add(map4);
        maps.add(map5);

        int index = 1;
        for (Map map : maps) {
            int count = index;
            for (Member member : members) {
                Recommend recommend;
                if (count == 0) {
                    recommend = Recommend.builder()
                            .member(member)
                            .map(map)
                            .status(RecommendStatus.OFF)
                            .cdate(LocalDateTime.now().minusHours(1))
                            .udate(LocalDateTime.now().minusHours(1))
                            .build();
                } else {
                    recommend = Recommend.builder()
                            .member(member)
                            .map(map)
                            .status(RecommendStatus.ON)
                            .cdate(LocalDateTime.now().minusHours(1))
                            .udate(LocalDateTime.now().minusHours(1))
                            .build();
                    count--;
                }
                recommendService.save(recommend);
            }
            index++;
        }

        // when
        List<Map> findMaps = recommendService.findByPopularMapList();

        // then
        for (Map findMap : findMaps) {
            log.info("mapId={}", findMap.getId());
        }
    }
}