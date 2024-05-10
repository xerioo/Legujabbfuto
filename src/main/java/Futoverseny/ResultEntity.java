package Futoverseny;

import jakarta.persistence.*;

@Entity
public class ResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "runnerId")
    private RunnerEntity runner;
    @ManyToOne
    @JoinColumn(name = "compId")
    private CompetitionEntity competition;
    private long result;

    public ResultEntity(RunnerEntity runner, CompetitionEntity competition, long result) {
        this.runner = runner;
        this.competition = competition;
        this.result = result;
    }

    public ResultEntity() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RunnerEntity getRunner() {
        return runner;
    }

    public void setRunner(RunnerEntity runner) {
        this.runner = runner;
    }

    public CompetitionEntity getCompetition() {
        return competition;
    }

    public void setCompetition(CompetitionEntity competition) {
        this.competition = competition;
    }

    public long getResult() {
        return result;
    }

    public void setResult(long result) {
        this.result = result;
    }
}
