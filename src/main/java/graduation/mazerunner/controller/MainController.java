package graduation.mazerunner.controller;

import graduation.mazerunner.domain.Map;
import graduation.mazerunner.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final RecommendService recommendService;

    @GetMapping("/")
    public String main(Model model) {

        List<Map> popularMapList = recommendService.findByPopularMapList();

        model.addAttribute("mapList", popularMapList);

        return "index";
    }

}
