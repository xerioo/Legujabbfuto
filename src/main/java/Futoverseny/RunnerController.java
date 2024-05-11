package Futoverseny;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

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

    @PostMapping("/runner/{id}/addresult")
    public String addResult(@PathVariable Long id, @ModelAttribute ResultEntity result) {
        ResultEntity newResult = new ResultEntity();
        RunnerEntity runner = runnerRepository.findById(id).orElse(null);
        if (runner != null) {
            newResult.setRunner(runner);
            newResult.setCompetition(null);
            newResult.setResult(result.getResult());
            resultRepository.save(result);

        } else {   return "error";
        }
        return "redirect:/runner/" + id;
    }


}
