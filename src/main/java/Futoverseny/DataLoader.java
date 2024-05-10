package Futoverseny;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataLoader implements CommandLineRunner {

    private final RunnerRepository runnerRepository;
    private final CompetitionRepository competitionRepository;

    @Autowired
    public DataLoader(RunnerRepository runnerRepository, CompetitionRepository competitionRepository) {
        this.runnerRepository = runnerRepository;
        this.competitionRepository = competitionRepository;
    }

    @Override
    public void run(String... args) {
        //creating runners with random data
        List<String> runnerNames = Arrays.asList("Tomi", "Zsuzsi", "Gazsi", "Forest", "Usain");
        Random random = new Random();
        for (String runnerName : runnerNames) {
            RunnerEntity runnerEntity = new RunnerEntity();
            runnerEntity.setRunnerName(runnerName);
            runnerEntity.setAge(16 + random.nextInt(65));
            runnerEntity.setGender(random.nextInt(3));
            runnerRepository.save(runnerEntity);
        }
        List<String> compNames = Arrays.asList("Budapest Marathon", "London félMarathon", "Berlin negyedMarathon", "New York nemMarathon", "Chicago középtáv");
        for (String compName : compNames) {
            CompetitionEntity comp = new CompetitionEntity();
            comp.setCompName(compName);
            comp.setLength(1 + random.nextInt(100));
            competitionRepository.save(comp);
        }

    }
}
