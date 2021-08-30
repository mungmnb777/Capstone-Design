package graduation.mazerunner.service;

import graduation.mazerunner.Paging;
import graduation.mazerunner.domain.Post;
import graduation.mazerunner.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Long save(Post post) {
        return postRepository.save(post);
    }

    public Post load(Long id) {
        Post findPost = postRepository.findOne(id);

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

    public Long update(Post post) {
        Post findPost = postRepository.findOne(post.getId());

        findPost.changeTitle(post.getTitle());
        findPost.changeContent(post.getContent());

        return findPost.getId();
    }

    public void delete(Long id) {
        postRepository.delete(id);
    }
}
