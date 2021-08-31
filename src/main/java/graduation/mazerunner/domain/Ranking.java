package graduation.mazerunner.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Ranking {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rank_id")
    private Long id;

    /**
     * 클리어 타임
     */
    private Integer timer;

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
}
