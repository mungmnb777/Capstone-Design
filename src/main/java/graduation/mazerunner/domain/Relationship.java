package graduation.mazerunner.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Relationship {

    @Id
    @GeneratedValue
    @Column(name = "relation_id")
    private Long id;

    /**
     * 로그인 한 본인 ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 본인 기준 친구 ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private Member friend;

    /**
     * 친구 관계 일자
     */
    @Column(name = "relation_date")
    private LocalDateTime date;

    /**
     * 친구 상태
     */
    @Enumerated(EnumType.STRING)
    private RelationStatus status;

    @Builder
    public Relationship(Long id, Member member, Member friend, LocalDateTime date, RelationStatus status) {
        this.id = id;
        this.member = member;
        this.friend = friend;
        this.date = date;
        this.status = status;
    }
}
