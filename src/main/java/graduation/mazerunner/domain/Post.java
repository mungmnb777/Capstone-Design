package graduation.mazerunner.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    /**
     * 게시글 제목
     */
    @Setter
    @Column(name = "post_title")
    private String title;

    /**
     * 게시글 내용
     */
    @Setter
    @Column(name = "post_content")
    private String content;

    /**
     * 게시글 생성 일자
     */
    @Column(name = "post_cdate")
    private LocalDateTime cdate;

    /**
     * 게시글 수정 일자
     */
    @Column(name = "post_udate")
    private LocalDateTime udate;

    /**
     * 조회수
     */
    @Setter
    private int hit;

    /**
     * 추천수
     */
    @Setter
    private int recommend;

    /**
     * 게시글 작성자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 댓글 목록
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Reply> replyList = new ArrayList<>();

    @Builder
    public Post(Long id, String title, String content,
                LocalDateTime cdate, int hit, int recommend, Member member, List<Reply> replyList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.cdate = cdate;
        this.hit = hit;
        this.recommend = recommend;
        this.member = member;
        this.replyList = replyList;
    }
}
