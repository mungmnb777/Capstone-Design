package graduation.mazerunner.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Map {

    @Id @GeneratedValue
    @Column(name = "map_id")
    private Long id;

    /**
     * 제작 게임 제목
     */
    @Column(name = "map_title")
    private String title;

    /**
     * 게임 맵 정보
     */
    @Column(name = "map_content")
    private String content;

    /**
     * 게임 생성 일자
     */
    @Column(name = "map_cdate")
    private LocalDateTime cdate;

    /**
     * 게임 수정 일자
     */
    @Column(name = "map_udate")
    private LocalDateTime udate;

    /**
     * 게임 맵의 높이
     */
    private int height;

    /**
     * 게임 맵의 너비
     */
    private int width;

    /**
     * 부수기 횟수
     */
    private int breakCount;
    /**
     * 게임 제작자 ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 게임 랭크 리스트
     */
    @OneToMany(mappedBy = "map", cascade = CascadeType.ALL)
    private List<Ranking> rankings = new ArrayList<>();


    @Builder
    public Map(Long id, String title, String content, LocalDateTime cdate,
               int height, int width, int breakCount, Member member, List<Ranking> rankings) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.cdate = cdate;
        this.height = height;
        this.width = width;
        this.breakCount = breakCount;
        this.member = member;
        this.rankings = rankings;
    }
}
