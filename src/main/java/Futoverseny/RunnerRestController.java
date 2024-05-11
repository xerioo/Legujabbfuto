package Futoverseny;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/runner")
public class RunnerRestController {

    @Autowired
    private CompetitionRepository competitionRepository;
    private RunnerRepository runnerRepository;
    private ResultRepository resultRepository;

    @Autowired
    public RunnerRestController(RunnerRepository runnerRepository, CompetitionRepository competitionRepository) {
        this.runnerRepository = runnerRepository;
        this.competitionRepository = competitionRepository;
    }

    @GetMapping("/{id}")
    public RunnerEntity getRunner(@PathVariable Long id) {
        return runnerRepository.findById(id).orElse(null);
    }

    @GetMapping("")
    public List<RunnerEntity> getAllRunners() {
        return runnerRepository.findAll();
    }

    public static class ResultRequest {
        private int result;

        public long getResult() {
            return result;
        }

    }
    @GetMapping("/averageage")
    public double getAverageAge() {
        List<RunnerEntity> runners = runnerRepository.findAll();
        double totalAge = 0;
        for (RunnerEntity runner : runners) {
            totalAge += runner.getAge();
        }
        return (double) totalAge / runners.size();
    }
}
