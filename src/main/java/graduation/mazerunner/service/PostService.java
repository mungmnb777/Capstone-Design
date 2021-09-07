package graduation.mazerunner.service;

import graduation.mazerunner.Paging;
import graduation.mazerunner.domain.Member;
import graduation.mazerunner.domain.Post;
import graduation.mazerunner.domain.Recommend;
import graduation.mazerunner.domain.RecommendStatus;
import graduation.mazerunner.repository.MemberRepository;
import graduation.mazerunner.repository.PostRepository;
import graduation.mazerunner.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final RecommendRepository recommendRepository;

    public Long save(Post post) {
        return postRepository.save(post);
    }

    public Post load(Long postId) {
        Post findPost = postRepository.findOne(postId);

        if (findPost == null) {
            throw new RuntimeException("게시글 정보가 존재하지 않습니다.");
        }

        findPost.increaseHit();

        return findPost;
    }

    public Post findOne(Long id) {
        return postRepository.findOne(id);
    }

    public Long findPostsCount() {
        return postRepository.findPostsCount();
    }

    public List<Post> findPerPage(Paging paging) {
        return postRepository.findPerPage(paging);
    }

    public List<Post> findRecentPosts(String memberId) {
        return postRepository.findRecentPosts(memberId);
    }

    public Long update(Post post) {
        Post findPost = postRepository.findOne(post.getId());

        findPost.updatePost(post.getTitle(), post.getContent());

        return findPost.getId();
    }

    public void delete(Long id) {
        postRepository.delete(id);
    }
}
