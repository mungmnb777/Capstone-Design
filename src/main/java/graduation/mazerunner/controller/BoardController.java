package graduation.mazerunner.controller;

import graduation.mazerunner.Paging;
import graduation.mazerunner.constant.SessionConst;
import graduation.mazerunner.controller.form.PostForm;
import graduation.mazerunner.domain.Member;
import graduation.mazerunner.domain.Post;
import graduation.mazerunner.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final PostService postService;

    @GetMapping("/list")
    public String boardList(Model model, @RequestParam(defaultValue = "1") int page) {

        int totalPostCnt = postService.findPostsCount().intValue();

        Paging paging = new Paging(totalPostCnt, page);

        List<Post> findPostList = postService.findPerPage(paging);

        model.addAttribute("postList", findPostList);
        model.addAttribute("paging", paging);

        return "board/list";
    }

    @GetMapping("/insert")
    public String insertPostForm(Model model) {

        model.addAttribute("form", new PostForm());

        return "board/insertPost";
    }

    @PostMapping("/insert")
    public String insertPost(@Validated @ModelAttribute("form") PostForm form, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "board/insertPost";
        }

        Post post = Post.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .hit(0)
                .recommend(0)
                .member((Member) session.getAttribute(SessionConst.LOGIN_MEMBER))
                .cdate(LocalDateTime.now())
                .build();

        postService.save(post);

        return "redirect:/board/list";
    }

    @GetMapping("/list/{mapId}")
    public String readPost(@PathVariable("mapId") Long id, Model model) {

        // 조회수를 증가시키기 위해 load Method 사용
        Post findPost = postService.load(id);

        model.addAttribute("post", findPost);

        return "board/readPost";
    }

    @GetMapping("/list/{mapId}/update")
    public String updatePostForm(@ModelAttribute("mapId") Long id, Model model) {

        // 단순 조회(조회수 증가x)
        Post findPost = postService.findOne(id);

        PostForm form = new PostForm();
        form.setTitle(findPost.getTitle());
        form.setContent(findPost.getContent());

        model.addAttribute("form", form);

        return "board/updatePost";
    }

    @PostMapping("/list/{mapId}/update")
    public String updatePost(@Validated @ModelAttribute("form") PostForm form, BindingResult bindingResult,
                             @PathVariable("mapId") Long id) {

        if (bindingResult.hasErrors()) {
            return "board/updatePost";
        }

        Post updatePost = Post.builder()
                .id(id)
                .title(form.getTitle())
                .content(form.getContent())
                .build();

        postService.update(updatePost);

        return "redirect:/board/list/" + id;
    }

    @GetMapping("/list/{mapId}/delete")
    public String deletePost(@PathVariable("mapId") Long id) {
        postService.delete(id);

        return "redirect:/board/list";
    }
}
