package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.EpisodesData;
import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.SeriesData;
import br.com.alura.screenmatch.service.ApiConsume;
import br.com.alura.screenmatch.service.ConvertData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private ApiConsume apiConsume = new ApiConsume();
    private ConvertData convertData = new ConvertData();
    private final String ADDRESS = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b5c4a009&";

    public void showMenu() {
        System.out.println("Digite o nome da série para buscar:");
        var serieName = scanner.nextLine();
        var json = apiConsume.getData(ADDRESS + serieName.replace(" ", "+") + API_KEY);
        SeriesData data = convertData.getData(json, SeriesData.class);
        System.out.println(data);

        List<SeasonData> seasons = new ArrayList<>();
        for (int i = 1; i <= data.totalSeasons(); i++) {
            json = apiConsume.getData(ADDRESS + serieName.replace(" ", "+") + "&season=" + i + API_KEY);
            SeasonData seasonData = convertData.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }

        seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));

        List<EpisodesData> episodesData = seasons.stream()
                .flatMap(s -> s.episodes().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 10 episodios");
        episodesData.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primeiro filtro(N/A): " + e))
                .sorted(Comparator.comparing(EpisodesData::rating).reversed())
                .peek(e -> System.out.println("Ordenação: " + e))
                .limit(10)
                .peek(e -> System.out.println("Limite: " + e))
                .map(e -> e.title().toUpperCase())
                .peek(e -> System.out.println("Mapeamento: " + e))
                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(s -> s.episodes().stream()
                        .map(d -> new Episode(s.number(), d))
                ).collect(Collectors.toList());

        episodes.forEach(System.out::println);

        System.out.println("Digite o episodio que você deseja buscar:");
        var searchTitle = scanner.nextLine();
        Optional<Episode> episodeFound = episodes.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(searchTitle.toUpperCase()))
                .findFirst();
        if (episodeFound.isPresent()){
            System.out.println("Episodio encontrado!");
            System.out.println("Temporada: " + episodeFound.get().getSeason());
        } else {
            System.out.println("Episodio não encontrado!");
        }

        System.out.println("A partir de que ano você deseja ver os episodios? ");
        var ano = scanner.nextInt();
        scanner.nextLine();

        LocalDate searchDate = LocalDate.of(ano, 1, 1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodes.stream()
                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDate))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getSeason() +
                                " Episodio: " + e.getTitle() +
                                " Data Lançamento: " + e.getReleaseDate().format(dtf)
                ));

        Map<Integer, Double> ratingBySeason = episodes.stream()
                .filter(e -> e.getRating()> 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason,
                        Collectors.averagingDouble(Episode::getRating)));

        System.out.println(ratingBySeason);
    }
}