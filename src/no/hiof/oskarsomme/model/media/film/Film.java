package no.hiof.oskarsomme.model.media.film;

import no.hiof.oskarsomme.model.media.production.Production;
import no.hiof.oskarsomme.model.people.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Film extends Production implements Comparable<Film>{
    public static List<Film> LIST_OF_FILMS = new ArrayList<>();
    private String imageURL;

    public Film(String title, String description, int runningTime,
                LocalDate releaseDate, Person director) {
        super(title, description, runningTime, releaseDate, director);
        LIST_OF_FILMS.add(this);
    }

    public Film(String title, String description, int runningTime,
                LocalDate releaseDate) {
        super(title, description, runningTime, releaseDate);
        LIST_OF_FILMS.add(this);
    }

    public Film(String title, String description, int runningTime) {
        super(title, description, runningTime);
        LIST_OF_FILMS.add(this);
    }

    public Film(String title, String description, int runningTime, LocalDate releaseDate, String imageURL) {
        super(title, description, runningTime, releaseDate);
        this.imageURL = imageURL;
        LIST_OF_FILMS.add(this);
    }

    public Film() {
        super();
        LIST_OF_FILMS.add(this);
    }

    public String getImageURL() {
        if(imageURL != null) {
            return imageURL;
        }

        return "";
    }

    public boolean isEmpty() {
        return getTitle() == null && getReleaseDate() == null && getDescription() == null;
    }

    @Override
    public int compareTo(Film o) {
        return this.getTitle().compareToIgnoreCase(o.getTitle());
    }

    public static Comparator<Film> sortByReleaseDate = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            if(o1.getReleaseDate().getYear() == o2.getReleaseDate().getYear()) {
                int dayOfYear1 = o1.getReleaseDate().getDayOfYear();
                int dayOfYear2 = o2.getReleaseDate().getDayOfYear();

                return dayOfYear1 - dayOfYear2;
            } else {
                return o1.getReleaseDate().getYear() - o2.getReleaseDate().getYear();
            }
        }
    };

    public static Comparator<Film> sortByRuntime = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            return o1.getRunningTime() - o2.getRunningTime();
        }
    };

    @Override
    public String toString() {
        if(getReleaseDate() == null) {
            return super.toString()+ "\nDescription: " + getDescription();
        }

        return super.toString()+ "\nRelease date: " + getReleaseDate() + "\nDescription: " + getDescription();

    }
}
