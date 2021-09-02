package graduation.mazerunner.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Recommend {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_id")
    private Long id;

    /**
     * 좋아요 누른 멤버 ID
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 좋아요 눌러진 MAP ID
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id")
    private Map map;

    /**
     * 좋아요 눌러진 POST ID
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Post post;

    /**
     * 좋아요 상태
     */
    @Column(name = "recommend_status")
    @Enumerated(EnumType.STRING)
    private RecommendStatus status;

    /**
     * 생성 일자
     */
    @Column(name = "recommend_cdate")
    private LocalDateTime cdate;

    /**
     * 수정 일자
     */
    @Column(name = "recommend_udate")
    private LocalDateTime udate;

    public RecommendStatus changeStatus() {
        this.udate = LocalDateTime.now();
        return this.status = (this.status == RecommendStatus.OFF) ? RecommendStatus.ON : RecommendStatus.OFF;
    }
}
