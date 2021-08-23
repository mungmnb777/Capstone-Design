package graduation.mazerunner.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ranking {

    @Id @GeneratedValue
    @Column(name = "rank_id")
    private Long id;

    /**
     * 클리어 타임
     */
    private int timer;

    /**
     * 클리어 멤버
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 클리어한 맵
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id")
    private Map map;

    /**
     * Map과의 양방향 매핑
     */
    public void setMap(Map map){
        this.map = map;
        map.getRankings().add(this);
    }

    @Builder
    public Ranking(Long id, int timer, Member member, Map map) {
        this.id = id;
        this.timer = timer;
        this.member = member;
        this.map = map;
    }
}
