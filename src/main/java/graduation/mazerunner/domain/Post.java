package graduation.mazerunner.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    /**
     * 게시글 제목
     */
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
    private int hit;

    /**
     * 추천수
     */
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
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Reply> replyList = new ArrayList<>();

    /**
     * cascade를 위해 입력
     */
    @OneToMany(mappedBy = "map", cascade = CascadeType.ALL)
    private List<Recommend> recommends = new ArrayList<>();

    public void increaseHit() {
        this.hit++;
    }

    public void increaseRecommend() {
        this.recommend++;
    }

    public void decreaseRecommend() {
        this.recommend--;
    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
        this.udate = LocalDateTime.now();
    }
}
