package no.hiof.oskarsomme.model.media.tvseries;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

    public Episode(int episodeNumber, int season, String title, LocalDate releaseDate, int runningTime) {
        super(title, runningTime, releaseDate);
        this.episodeNumber = episodeNumber;
        this.season = season;
    }

    public Episode(int episodeNumber, int season, String title, LocalDate releaseDate, int runningTime, int id) {
        super(title, runningTime, releaseDate, id);
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

    public Episode() {

    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public int getSeason() {
        return season;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public StringProperty getTitleProperty() {
        return new SimpleStringProperty(getTitle());
    }

    public ObjectProperty<LocalDate> getReleaseDateProperty() {
        return new SimpleObjectProperty<>(getReleaseDate());
    }

    @Override
    public boolean isEmpty() {
        return getTitle() == null && (getEpisodeNumber() == 0 || getSeason() == 0) &&
                getReleaseDate() == null && getRunningTime() == 0;
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
