package graduation.mazerunner.service;

import graduation.mazerunner.domain.Map;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class MapServiceTest {

    @Autowired
    MapService mapService;

    @Test
    public void 맵_저장() throws Exception {

        // given
        Map map = Map.builder()
                .content("000123000")
                .height(3)
                .width(3)
                .build();

        // when
        Long id = mapService.save(map);

        // then
        assertThat(map.getId()).isEqualTo(id);
    }

    @Test
    public void 시작점미포함() throws Exception {

        // given
        Map map = Map.builder()
                .content("000103000")
                .height(3)
                .width(3)
                .build();
        // when

        // then
        assertThrows(IllegalArgumentException.class, () -> mapService.test(map));
    }

    @Test
    public void 도착점미포함() throws Exception {

        // given
        Map map = Map.builder()
                .content("000102000")
                .height(3)
                .width(3)
                .build();
        // when

        // then
        assertThrows(IllegalArgumentException.class, () -> mapService.test(map));
    }

    @Test
    public void 게임실행() throws Exception {

        // given
        Map savedMap = Map.builder()
                .content("000132000")
                .height(3)
                .width(3)
                .build();

        Long savedId = mapService.save(savedMap);

        // when
        Map findMap = mapService.load(savedId);

        // then
        assertThat(savedId).isEqualTo(findMap.getId());
        assertThat(savedMap.getContent()).isEqualTo(findMap.getContent());
        assertThat(savedMap.getHeight()).isEqualTo(findMap.getHeight());
        assertThat(savedMap.getWidth()).isEqualTo(findMap.getWidth());
    }

    @Test
    public void invalidLoadMap() throws Exception {

        // given
        Map savedMap = Map.builder()
                .content("000132000")
                .height(3)
                .width(3)
                .build();
        // when

        // then
        assertThrows(RuntimeException.class, () -> mapService.load(204L));
    }
}