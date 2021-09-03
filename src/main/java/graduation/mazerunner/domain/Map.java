package graduation.mazerunner.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Map {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 게임 랭크 리스트
     */
    @OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ranking> rankings = new ArrayList<>();

    /**
     * cascade
     */
    @OneToMany(mappedBy = "map", cascade = CascadeType.ALL)
    private List<Recommend> recommends = new ArrayList<>();


    public void increaseHit() {
        this.hit++;
    }

    public void increaseRecommend() {
        this.recommend++;
    }

    public void decreaseRecommend() {
        this.recommend--;
    }
}
