package graduation.mazerunner.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    /**
     * 댓글 내용
     */
    @Column(name = "reply_content")
    private String content;

    /**
     * 댓글 작성 일자
     */
    @Column(name = "reply_cdate")
    private LocalDateTime cdate;

    /**
     * 댓글 수정 일자
     */
    @Column(name = "reply_udate")
    private LocalDateTime udate;

    /**
     * 댓글이 작성된 게시글 ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * 댓글 작성자 ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * Post와의 양방향 매핑
     */
    public void setPost(Post post){
        this.post = post;
        post.getReplyList().add(this);
    }

    public void updateContent(String content) {
        this.content = content;
        this.udate = LocalDateTime.now();
    }
}
