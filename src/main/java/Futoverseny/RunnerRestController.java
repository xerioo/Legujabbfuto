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

    @PostMapping("/{id}/addresult")
    public ResponseEntity addResult(@PathVariable Long rId, @RequestBody ResultRequest resultRequest) {
        RunnerEntity runner = runnerRepository.findById(rId).orElse(null);
        CompetitionEntity competition = competitionRepository.findById(1L).orElse(null);
        if (runner != null) {
            ResultEntity result = new ResultEntity();
            result.setResult(resultRequest.getResult());
            result.setRunner(runner);
            result.setCompetition(competition);
            resultRepository.save(result);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Runner with ID " + rId + " not found");
        }
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
