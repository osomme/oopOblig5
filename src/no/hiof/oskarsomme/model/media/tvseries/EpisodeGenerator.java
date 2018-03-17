package no.hiof.oskarsomme.model.media.tvseries;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EpisodeGenerator {
    private final List<String> one = new ArrayList<>();
    private final List<String> two = new ArrayList<>();
    private final List<String> three = new ArrayList<>();
    private Random rng;

    public EpisodeGenerator() {
        Collections.addAll(one, "Whatever", "Hey", "Scary", "Test", "Who", "How", "The");
        Collections.addAll(two, "of", "the", "in", "is", "behind", "our", "their");
        Collections.addAll(three, "Season", "Door", "Another", "Special", "Harm", "End", "Lie");
        rng = new Random();
    }

    private String randomEpisodeName() {
        return one.get(rng.nextInt(one.size())) + " " + two.get(rng.nextInt(two.size())) +
                " "  + three.get(rng.nextInt(three.size()));
    }

    public void generateEpisodes(TVSeries series, int numberOfEpisodes, int seasons) {
        int episodesPerSeason = numberOfEpisodes / seasons;

        for(int i = 1; i <= seasons; i++) {
            for(int j = 1; j <= episodesPerSeason; j++) {
                series.addEpisode(new Episode(j, i, randomEpisodeName()));
            }
        }
    }

    public void generateEpisodes(TVSeries series, int numberOfEpisodes, LocalDate initialAirdate) {
        /* This method creates new episodes x number of times.
         * The air-date of each episode is increased by one day for each iteration
         * and episodes only air on weekdays. When the episodes enter a new year,
         * a new season is created.
        */
        int season = 1;
        int episode = 1;
        int i = 0;
        LocalDate airDate = initialAirdate;
        LocalDate lastWeek = initialAirdate;

        while(i < numberOfEpisodes) {
            // Checks to see if the new episode is previous episodes (year + 1)
            if(airDate.getYear() > lastWeek.getYear()) {
                season++;
                episode = 1;
            }

            lastWeek = LocalDate.of(airDate.getYear(), airDate.getMonth(), airDate.getDayOfMonth());

            // Checks to see if current day is a saturday or sunday. If not: add episode.
            if(!(airDate.getDayOfWeek().getValue() == 6 || airDate.getDayOfWeek().getValue() == 7)) {
                series.addEpisode(new Episode(episode, season, randomEpisodeName(), airDate));
                i++;
                episode++;
            }

            airDate = airDate.plusDays(1);
        }
    }
}
