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
public class FriendRequest {

    @Id
    @GeneratedValue
    @Column(name = "req_id")
    private Long id;

    /**
     * 친구 요청한 멤버 ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 요청받은 멤버 ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private Member friend;

    /**
     * 요청 시점 일자
     */
    @Column(name = "req_date")
    private LocalDateTime date;

    /**
     * 요청 상태
     */
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Builder
    public FriendRequest(Long id, Member member, Member friend, LocalDateTime date, RequestStatus status) {
        this.id = id;
        this.member = member;
        this.friend = friend;
        this.date = date;
        this.status = status;
    }
}
