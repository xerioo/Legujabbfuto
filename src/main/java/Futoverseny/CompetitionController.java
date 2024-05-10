package Futoverseny;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompetitionController {

    private final CompetitionRepository competitionRepository;

    public CompetitionController(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    @GetMapping("/competitions")
    public String showCompetitions(Model model) {
        model.addAttribute("competitions", competitionRepository.findAll());
        return "competitions";
    }
}