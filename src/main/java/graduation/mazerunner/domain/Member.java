package graduation.mazerunner.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Member(String id, String password, String passwordCheck,
                  String nickname, LocalDateTime cdate) {
        this.id = id;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.nickname = nickname;
        this.cdate = cdate;
    }
}
