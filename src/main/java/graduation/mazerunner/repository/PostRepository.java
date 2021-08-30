package graduation.mazerunner.repository;

import graduation.mazerunner.Paging;
import graduation.mazerunner.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public Long save(Post post){
        em.persist(post);
        return post.getId();
    }

    public Post findOne(Long id){ return em.find(Post.class, id); }

    public Long findPostsCount() {
        return em.createQuery("select count(p) from Post p", Long.class)
                .getSingleResult();
    }

    public List<Post> findPerPage(Paging paging) {
        return em.createQuery("select p from Post p order by p.cdate desc", Post.class)
                .setFirstResult(paging.getStartIndex())
                .setMaxResults(paging.getPostPerPage())
                .getResultList();
    }

    public void delete(Long id) {
        Post post = em.find(Post.class, id);
        em.remove(post);
    }
}
