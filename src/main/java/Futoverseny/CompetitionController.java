package Futoverseny;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CompetitionController {

    private final CompetitionRepository competitionRepository;
    private final RunnerRepository runnerRepository;
    private final ResultRepository resultRepository;

    public CompetitionController(CompetitionRepository competitionRepository, RunnerRepository runnerRepository,
                                 ResultRepository resultRepository) {
        this.competitionRepository = competitionRepository;
        this.runnerRepository = runnerRepository;
        this.resultRepository = resultRepository;
    }

    @GetMapping("/competitions")
    public String showCompetitions(Model model) {
        model.addAttribute("competitions", competitionRepository.findAll());
        return "competitions";
    }

    @GetMapping("/comp/{id}")
    public String showCompetitionResults(@PathVariable int id, Model model) {
        CompetitionEntity competition = competitionRepository.findById((long)id).orElse(null);
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

    @GetMapping("/comp/{id}/addcompres")
    public String showAddResultForm(@PathVariable int id, Model model) {
        CompetitionEntity competition = competitionRepository.findById((long)id).orElse(null);
        model.addAttribute("competition", competition);
        model.addAttribute("runners", runnerRepository.findAll());
        model.addAttribute("result", new ResultEntity());
        return "addcompres";
    }

    @PostMapping("/comp/{id}/addcompres")
    public ResponseEntity handleAddResultForm(@PathVariable int id, @ModelAttribute ResultEntity result) {
        CompetitionEntity competition = competitionRepository.findById((long)id).orElse(null);
        if (competition != null) {
            result.setCompetition(competition);
            resultRepository.save(result);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nincs ilyen verseny:" + id + "!");
        }
//        return "redirect:/comp/" + id;
    }

}
