package Futoverseny;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CompetitionController {

    private final CompetitionRepository competitionRepository;
    private final ResultRepository resultRepository;

    public CompetitionController(CompetitionRepository competitionRepository, ResultRepository resultRepository) {
        this.competitionRepository = competitionRepository;
        this.resultRepository = resultRepository;
    }

    @GetMapping("/competitions")
    public String showCompetitions(Model model) {
        model.addAttribute("competitions", competitionRepository.findAll());
        return "competitions";
    }

    @GetMapping("/comp/{id}")
    public String showCompetitionResults(@PathVariable Long id, Model model) {
        CompetitionEntity competition = competitionRepository.findById(id).orElse(null);
        model.addAttribute("competition", competition);
        if (competition != null) {
            List<ResultEntity> sortedResults = resultRepository.findByCompetition(competition)
                    .stream()
                    .sorted(Comparator.comparing(ResultEntity::getResult))
                    .collect(Collectors.toList());
            model.addAttribute("results", sortedResults);
        } else {
            model.addAttribute("results", List.of());
        }
        return "comp";
    }

}
