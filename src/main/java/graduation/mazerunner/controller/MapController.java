package graduation.mazerunner.controller;

import graduation.mazerunner.constant.SessionConst;
import graduation.mazerunner.controller.form.MakerForm;
import graduation.mazerunner.domain.Map;
import graduation.mazerunner.domain.Member;
import graduation.mazerunner.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/maps")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping("/list")
    public String getMapList() {
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
                .cdate(LocalDateTime.now())
                .member((Member)session.getAttribute(SessionConst.LOGIN_MEMBER))
                .build();

        mapService.save(map);
        return "redirect:/maps/list";
    }
}
