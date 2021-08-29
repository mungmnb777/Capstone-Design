package graduation.mazerunner.controller;

import graduation.mazerunner.Paging;
import graduation.mazerunner.constant.SessionConst;
import graduation.mazerunner.controller.form.MakerForm;
import graduation.mazerunner.domain.Map;
import graduation.mazerunner.domain.Member;
import graduation.mazerunner.domain.Ranking;
import graduation.mazerunner.repository.RankRepository;
import graduation.mazerunner.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/maps")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;
    private final RankRepository rankRepository;

    @GetMapping("/list")
    public String getMapList(Model model, @RequestParam(defaultValue = "1") int page) {

        int totalPostCnt = mapService.findMapsCount().intValue();

        Paging paging = new Paging(totalPostCnt, page);

        List<Map> mapList = mapService.findPerPage(paging);

        model.addAttribute("mapList", mapList);
        model.addAttribute("paging", paging);

        return "maps/list";
    }

    @GetMapping("/maker")
    public String getMapMaker(Model model) {

        model.addAttribute("form", new MakerForm());
        return "maps/maker";
    }

    @PostMapping("/maker")
    public String checkMap(@Validated @ModelAttribute("form") MakerForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        try {
            if (bindingResult.hasErrors()) {
                return "maps/maker";
            }

            Map map = Map.builder()
                    .title(form.getTitle())
                    .content(form.getContent())
                    .height(form.getHeight())
                    .width(form.getWidth())
                    .breakCount(form.getBreakCount())
                    .build();

            mapService.test(map);

            redirectAttributes.addFlashAttribute("form", form);

            return "redirect:/maps/test";
        } catch (IllegalArgumentException e) {

            bindingResult.reject("IllegalArgument", e.getMessage());
            return "maps/maker";
        }
    }

    @GetMapping("/test")
    public String testMap(@ModelAttribute("form") MakerForm form, Model model) {

        model.addAttribute("form", form);
        return "maps/test";
    }

    @PostMapping("/save")
    public String saveMap(@ModelAttribute("form") MakerForm form, HttpSession session) {

        Map map = Map.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .height(form.getHeight())
                .width(form.getWidth())
                .breakCount(form.getBreakCount())
                .hit(0)
                .recommend(0)
                .cdate(LocalDateTime.now())
                .member((Member) session.getAttribute(SessionConst.LOGIN_MEMBER))
                .build();

        mapService.save(map);
        return "redirect:/maps/list";
    }

    @GetMapping("/list/{mapId}")
    public String playGame(@PathVariable("mapId") Long id, Model model) {
        Map findMap = mapService.load(id);

        List<Ranking> rankings = rankRepository.findByMap(findMap);

        model.addAttribute("map", findMap);
        model.addAttribute("member", findMap.getMember());
        model.addAttribute("rankings", rankings);

        return "maps/player";
    }

    @PostMapping("/list/{mapId}")
    public String clearGame(@PathVariable("mapId") Long id, @RequestParam("timer") int timer ,HttpSession session) {
        Map findMap = mapService.findOne(id);

        Ranking ranking = Ranking.builder()
                .map(findMap)
                .member((Member)session.getAttribute(SessionConst.LOGIN_MEMBER))
                .timer(timer)
                .build();

        mapService.clear(id, ranking);

        return "redirect:/maps/list";
    }
}
