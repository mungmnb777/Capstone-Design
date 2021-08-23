package graduation.mazerunner.controller;

import graduation.mazerunner.controller.form.MakerForm;
import graduation.mazerunner.domain.Map;
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
    public String checkMap(@Validated @ModelAttribute("form") MakerForm form, BindingResult bindingResult) {

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

            return "redirect:/maps/test";
        } catch (IllegalArgumentException e) {

            bindingResult.reject("IllegalArgument", e.getMessage());
            return "maps/maker";
        }



    }
}
