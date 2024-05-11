package Futoverseny;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;


@Service
@RequestMapping("/api/v1/runner")
public class RunnerService {

    private final RunnerRepository runnerRepository;

    @Autowired
    public RunnerService(RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

}