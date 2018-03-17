package no.hiof.oskarsomme.model.media.tvseries;

import no.hiof.oskarsomme.model.media.production.Production;
import no.hiof.oskarsomme.model.people.Person;

import java.time.LocalDate;
import java.util.Random;

public class Episode extends Production implements Comparable<Episode>{
    private int episodeNumber;
    private int season;

    public Episode(int episodeNumber, int season, String title, int runningTime, LocalDate releaseDate, Person director) {
        super(title, runningTime, releaseDate, director);
        this.episodeNumber = episodeNumber;
        this.season = season;
    }

    public Episode(int episodeNumber, int season, String title, int runningTime) {
        super(title, runningTime);
        this.episodeNumber = episodeNumber;
        this.season = season;
    }

    public Episode(int episodeNumber, int season, String title, LocalDate releaseDate) {
        super(title, 0, releaseDate);
        Random rng = new Random();
        this.episodeNumber = episodeNumber;
        this.season = season;
        this.setRunningTime(rng.nextInt(11)+ 20);
    }

    public Episode(int episodeNumber, int season, String title) {
        super(title, 0);
        Random rng = new Random();
        this.episodeNumber = episodeNumber;
        this.season = season;
        this.setRunningTime(rng.nextInt(11)+ 20);
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public int getSeason() {
        return season;
    }

    @Override
    public int compareTo(Episode o) {
        if(o.getSeason() == this.getSeason()) {
            return this.getEpisodeNumber() - o.getEpisodeNumber();
        } else {
            return this.getSeason() - o.getSeason();
        }
    }

    @Override
    public String toString() {
        return super.getTitle() + " (S" + this.season + "E" +
                this.episodeNumber + "), " + super.getRunningTime() + " minutes, on-air date: " + super.getReleaseDate();
    }
}
