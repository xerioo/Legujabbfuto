package Futoverseny;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private final ResultRepository resultRepository;
    public static String jdbcUrl = "jdbc:h2:~/IdeaProjects/database";
    public static String jdbcUsername = "sa";
    public static String jdbcPassword = "";
    public static Charset ekezet = Charset.forName("windows-1252");

    @Autowired
    public DataLoader(RunnerRepository runnerRepository, CompetitionRepository competitionRepository,
                    ResultRepository resultRepository) {
        this.runnerRepository = runnerRepository;
        this.competitionRepository = competitionRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public void run(String... args) {

        String projectDir = System.getProperty("user.dir");
        projectDir += "/src/main/resources/csv/";
        //a versenyek és a futók internetes táblázatokból származnak, az eredmények pedig
        //véletlengenerátorral készültek (3-4  adattal nem lehetne rendesen tesztelni)
        String runnerPath = projectDir+"runners.csv";
        String compPath = projectDir+"competitions.csv";
        String resPath = projectDir+"results.csv";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
             BufferedReader br = new BufferedReader(new FileReader(runnerPath,ekezet))) {
             String line;
             String sql = "INSERT INTO runnerdb (name, age, gender) VALUES (?, ?, ?)";
//             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                String name = data[0];
                int age = Integer.parseInt(data[1]);
                int gender = Integer.parseInt(data[2]);
//                preparedStatement.setString(1, name);   //az első verzióban a H2 adatbáziskezelőt használtam
//                preparedStatement.setInt(2, age);       //itt kerültek be az adatok soronként a .csv-fájlokból a fenti SQL-sorral
//                preparedStatement.setInt(3, gender);    //a Spring Boot + JPA verzióban erre nincs szükség
//                preparedStatement.executeUpdate();                  //de még benne hagyom
                RunnerEntity runner = new RunnerEntity();
                runner.setRunnerName(name);
                runner.setAge(age);
                runner.setGender(gender);
                runnerRepository.save(runner);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
             BufferedReader br = new BufferedReader(new FileReader(compPath,ekezet))) {
            String line;
            String sql = "INSERT INTO competitiondb (name, length) VALUES (?, ? )";
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                String name = data[0];
                int length = Integer.parseInt(data[1]);
//                preparedStatement.setString(1, name);
//                preparedStatement.setInt(2, length);
//                preparedStatement.executeUpdate();
                CompetitionEntity comp = new CompetitionEntity();
                comp.setCompName(name);
                comp.setLength(length/1000);    //a versenyek hosszát km-ben tárolom, de a .csv-fájlban méterben van
                competitionRepository.save(comp);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
             BufferedReader br = new BufferedReader(new FileReader(resPath,ekezet))) {
            String line;
            String sql = "INSERT INTO resultdb (runner, competition, time) VALUES (?, ?, ? )";
 //           PreparedStatement preparedStatement = connection.prepareStatement(sql);
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                int runner = Integer.parseInt(data[0]);
                int competition = Integer.parseInt(data[1]);
                long time = Long.parseLong(data[2]);
//                preparedStatement.setInt(1, runner);
//                preparedStatement.setInt(2, competition);
//                preparedStatement.setLong(3, time);
//                preparedStatement.executeUpdate();
                ResultEntity result = new ResultEntity();
                result.setRunner(runnerRepository.findById((long)runner).orElse(null));
                result.setCompetition(competitionRepository.findById((long)competition).orElse(null));
                result.setResult(time/60000);    //eredetileg a rövidebb versenyekre tekintettel ms-ben tároltam az időt, itt lesz perc
                resultRepository.save(result);
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }
}
