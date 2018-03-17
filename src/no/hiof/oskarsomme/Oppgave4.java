package no.hiof.oskarsomme;

import no.hiof.oskarsomme.model.media.film.Film;
import no.hiof.oskarsomme.model.media.tvseries.Episode;
import no.hiof.oskarsomme.model.media.tvseries.TVSeries;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Oppgave4 {
    public static void main(String[] args) {
        Film starWars = new Film("Star Wars", "Space wars wowzers", 130);
        Film jumanji = new Film("Jumanji: Welcome to The Jungle", "Test", 125);
        Film test1 = new Film("A movie", "Simply a movie", 95);
        Film test2 = new Film("Z movie", "Simply z movie", 95);


        for(Film film : Film.LIST_OF_FILMS)
            System.out.println(film.getTitle());

        Collections.sort(Film.LIST_OF_FILMS);
        System.out.println("------------------------");
        System.out.println("After sorting:");

        for(Film film : Film.LIST_OF_FILMS)
            System.out.println(film.getTitle());


        System.out.println("------------------------");
        System.out.println("TV series:");

        TVSeries series1 = new TVSeries("A tv series", "A tv series", LocalDate.of(666, 6, 6));
        TVSeries series2 = new TVSeries("Z tv series", "Z tv series", LocalDate.of(666, 6, 6));
        TVSeries series3 = new TVSeries("K tv series", "K tv series", LocalDate.of(666, 6, 6));

        for(TVSeries series : TVSeries.LIST_OF_TVSERIES)
            System.out.println(series.getTitle());

        Collections.sort(TVSeries.LIST_OF_TVSERIES);
        System.out.println("------------------------");
        System.out.println("After sorting:");

        for(TVSeries series : TVSeries.LIST_OF_TVSERIES)
            System.out.println(series.getTitle());

        System.out.println("------------------------");
        System.out.println("Episodes: ");

        Episode ep1 = new Episode(1, 1, "");
        Episode ep2 = new Episode(4, 1, "");
        Episode ep3 = new Episode(5, 3, "");
        Episode ep4 = new Episode(2, 3, "");
        Episode ep5 = new Episode(2, 6, "");

        List<Episode> episodeList = new ArrayList<>();
        episodeList.add(ep4);
        episodeList.add(ep2);
        episodeList.add(ep1);
        episodeList.add(ep5);
        episodeList.add(ep3);

        for(Episode ep : episodeList)
            System.out.println(ep);

        Collections.sort(episodeList);
        System.out.println("------------------------");
        System.out.println("After sorting:");

        for(Episode ep : episodeList)
            System.out.println(ep);
    }
}
