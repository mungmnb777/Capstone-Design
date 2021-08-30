package graduation.mazerunner.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {

    /**
     * 멤버 ID
     */
    @Id
    @Column(name = "member_id")
    private String id;

    /**
     * 비밀번호
     */
    private String password;

    /**
     * 비밀번호 확인
     */
    @Transient
    private String passwordCheck;

    /**
     * 멤버 별명
     */
    private String nickname;

    /**
     * 회원가입 일자
     */
    @Column(name = "member_cdate")
    private LocalDateTime cdate;

    /**
     * 멤버 정보 변경 일자
     */
    @Column(name = "member_udate")
    private LocalDateTime udate;

}
