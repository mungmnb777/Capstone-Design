package graduation.mazerunner.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "req_cdate")
    private LocalDateTime cdate;

    /**
     * 요청 상태 변경 일자
     */
    @Column(name = "req_udate")
    private LocalDateTime udate;

    /**
     * 요청 상태
     */
    @Column(name = "req_status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

}
