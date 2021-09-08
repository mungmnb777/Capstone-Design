package graduation.mazerunner.repository;

import graduation.mazerunner.domain.Member;
import graduation.mazerunner.domain.Post;
import graduation.mazerunner.domain.Reply;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired PostRepository postRepository;

    @Test
    public void 게시글저장() throws Exception {

        // given
        Post post = Post.builder()
                .title("안녕")
                .build();

        Reply reply = Reply.builder()
                .content("하세요")
                .build();

        reply.setPost(post);

        // when
        Long savedId = postRepository.save(post);

        // then
        assertThat(savedId).isEqualTo(post.getId());
    }

}