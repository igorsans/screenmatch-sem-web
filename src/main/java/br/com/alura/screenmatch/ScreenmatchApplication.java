package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.SeriesData;
import br.com.alura.screenmatch.service.ApiConsume;
import br.com.alura.screenmatch.service.ConvertData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var apiconsume = new ApiConsume();
        var json = apiconsume.getData("http://www.omdbapi.com/?t=gilmore+girls&apikey=b5c4a009&");
        System.out.println(json);

        ConvertData convertData = new ConvertData();
        SeriesData data = convertData.getData(json, SeriesData.class);
        System.out.println(data);
    }
}
