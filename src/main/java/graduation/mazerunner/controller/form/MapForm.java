package graduation.mazerunner.controller.form;

import graduation.mazerunner.domain.Member;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
public class MapForm {

    private Long id;

    /**
     * 제작 게임 제목
     */
    @Column(name = "map_title")
    private String title;

    /**
     * 게임 생성 일자
     */
    private LocalDateTime cdate;

    /**
     * 게임 수정 일자
     */
    private LocalDateTime udate;

    /**
     * 조회수
     */
    private int hit;

    /**
     * 추천수
     */
    private int recommend;

    /**
     * 게임 제작자 ID
     */
    private Member member;

    private boolean isRelated;
}
