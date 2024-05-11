package Futoverseny;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RunnerController {

    @Autowired
    private RunnerRepository runnerRepository;
    @Autowired
    private RunnerService runnerService;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private RunnerRestController runnerRestController;



    @GetMapping("/runners")
    public String getAllRunners(Model model) {
        List<RunnerEntity> runners = runnerRepository.findAll();

        model.addAttribute("runners", runners);
        double averageAge = Math.round(runnerRestController.getAverageAge()*100.0)/100.0;
        model.addAttribute("averageAge", averageAge);
        return "runners";
    }

    @GetMapping("/runner/{id}")
    public String getRunnerById(@PathVariable int id, Model model) {
        RunnerEntity runner = runnerRepository.findById((long)id).orElse(null);
        RunnerService runnerService = new RunnerService(runnerRepository);
        if (runner != null) {
            model.addAttribute("runner", runner);
            List<ResultEntity> results = resultRepository.findByRunner(runner);
            model.addAttribute("results", results);
            return "runner";
        } else {
            return "error";
        }
    }

    @GetMapping("/runner/{id}/addrunres")
    public String showAddResultForm(@PathVariable Long id, Model model) {
        RunnerEntity runner = runnerRepository.findById(id).orElse(null);
        model.addAttribute("runner", runner);
        model.addAttribute("competitions", competitionRepository.findAll());
        model.addAttribute("result", new ResultEntity());
        return "addrunres";
    }

    @PostMapping("/runner/{id}/addrunres")
    public String handleAddResultForm(@PathVariable Long id, @ModelAttribute ResultEntity result) {
        RunnerEntity runner = runnerRepository.findById(id).orElse(null);
        if (runner != null) {
            result.setRunner(runner);
            resultRepository.save(result);
        }
        return "redirect:/runner/" + id;
    }


}
