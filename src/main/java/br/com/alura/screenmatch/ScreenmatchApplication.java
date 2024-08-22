package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.EpisodesData;
import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.SeriesData;
import br.com.alura.screenmatch.service.ApiConsume;
import br.com.alura.screenmatch.service.ConvertData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var address = "http://www.omdbapi.com/?t=";
        var key = "&apikey=b5c4a009&";
        var url = "gilmore+girls";

        var apiconsume = new ApiConsume();
        var json = apiconsume.getData(address + url + key);
        System.out.println(json);

        ConvertData convertData = new ConvertData();
        SeriesData data = convertData.getData(json, SeriesData.class);
        System.out.println(data);
        var urlEp = "gilmore+girls&season=1&episode=2";
        json = apiconsume.getData(address + urlEp + key);
        EpisodesData episodesData = convertData.getData(json, EpisodesData.class);
        System.out.println(episodesData);

        List<SeasonData> seasons = new ArrayList<>();
        for (int i = 1; i<=data.totalSeasons(); i++){
            json = apiconsume.getData(address + "gilmore+girls&season=" + i + key);
            SeasonData seasonData = convertData.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        seasons.forEach(System.out::println);
    }
}
