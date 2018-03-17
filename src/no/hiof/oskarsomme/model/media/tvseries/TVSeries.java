package no.hiof.oskarsomme.model.media.tvseries;

import no.hiof.oskarsomme.model.people.Role;

import java.time.LocalDate;
import java.util.*;

public class TVSeries implements Comparable<TVSeries>{
    private String title;
    private String description;
    private LocalDate releaseDate;
    private String posterURL;
    private List<Episode> episodeList;
    private int averageRunningTime;
    private int numberOfSeasons;
    public static List<TVSeries> LIST_OF_TVSERIES = new ArrayList<>();

    public TVSeries(String title, String description, LocalDate releaseDate) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.episodeList = new ArrayList<>();
        this.averageRunningTime = 0;
        this.numberOfSeasons = 0;
        LIST_OF_TVSERIES.add(this);
    }

    public TVSeries(String title, String description, LocalDate releaseDate, String posterURL) {
        this(title, description, releaseDate);
        this.posterURL = posterURL;
    }

    public void addEpisode(Episode episode) {
        if(episode.getSeason() == this.numberOfSeasons) {
            this.episodeList.add(episode);
            this.updateAverageRunningTime();
        } else if(episode.getSeason() == this.numberOfSeasons + 1) {
            this.episodeList.add(episode);
            this.updateAverageRunningTime();
            this.numberOfSeasons++;
        } else if(episode.getSeason() < 0) {
            System.out.println("Error when attempting to add episode: You cannot add negative seasons");
        } else if(episode.getSeason() > this.numberOfSeasons) {
            System.out.println("Error when attempting to add episode: You cannot add " +
                    "this episode because seasons have to be added sequentially from " + (this.numberOfSeasons+1) +
                    " to " + episode.getSeason());
        }
    }

    public void printEpisodes() {
        for(Episode episode : episodeList) {
            System.out.println(episode);
        }
    }

    public int getAverageRunningTime() {
        return this.averageRunningTime;
    }

    public int getNumberOfEpisodes() {
        return episodeList.size();
    }

    public String getPosterURL() {
        return posterURL;
    }

    private void updateAverageRunningTime() {
        int sum = 0;

        for(Episode episode : episodeList) {
            sum += episode.getRunningTime();
        }

        this.averageRunningTime = (sum / episodeList.size());
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public int getNumberOfSeasons() {
        return this.numberOfSeasons;
    }

    public List<Episode> getEpisodesInSeason(int season) {
        List<Episode> episodes = new ArrayList<>();

        for(Episode episode : episodeList) {
            if(episode.getSeason() == season) {
                episodes.add(episode);
            }
        }
        return episodes;
    }

    public void printEpisodesInSeason(int season) {
        List<Episode> episodes = new ArrayList<>();

        for(Episode episode : episodeList) {
            if(episode.getSeason() == season) {
                episodes.add(episode);
            }
        }

        try {
            System.out.println("Number of episodes in season " + episodes.get(0).getSeason() + ": " + episodes.size());
        } catch (Exception e) {
            System.out.println("No episodes found for the requested season.");
        }

        for(Episode episode : episodes) {
            System.out.println(episode);
        }
    }

    public List<Role> getCast() {
        // Returns cast without duplicates.

        List<Role> allRoles = new ArrayList<>(getAllCast());

        List<Role> distinctRoles = new ArrayList<>();
        for(Role role : allRoles) {
            if(!distinctRoles.contains(role)) {
                distinctRoles.add(role);
            }
        }

        return distinctRoles;
    }

    private List<Role> getAllCast() {
        // Returns cast with duplicates.

        List<Role> allRoles = new ArrayList<>();
        for(Episode episode : episodeList) {
            allRoles.addAll(episode.getRoles());
        }

        return allRoles;
    }


    public void printNumberOfEpisodesPerActor() {
        List<Role> characters = new ArrayList<>(getCast());
        Map<Role, Integer> episodesPerActor = new HashMap<>();

        // Adds distinct roles as keys in hashmap.
        for(Role role : characters) {
            episodesPerActor.put(role, 0);
        }

        // Loops through a list of all episodes and checks hashmap to see if key is there.
        // If it is, update the corrosponding value by += 1
        List<Role> allEpisodesAndCharacters = new ArrayList<>(getAllCast());
        for(Role role : allEpisodesAndCharacters) {
            //String name = role.getActor().getFullName();

            // Updates value by one if the actor is already in hashmap.
            if(episodesPerActor.containsKey(role)) {
                episodesPerActor.put(role, episodesPerActor.get(role) + 1);
            }
        }

        // list of actors' names.
        List<Role> names = new ArrayList<>(episodesPerActor.keySet());
        // and number of episodes starring the actors.
        List<Integer> values = new ArrayList<>(episodesPerActor.values());

        for(int i = 0; i < names.size(); i++) {
            if(values.get(i) > 1) {
                System.out.println(names.get(i).getActor().getFullName() + ": " + values.get(i) + " episodes");
            } else {
                System.out.println(names.get(i).getActor().getFullName() + ": " + values.get(i) + " episode");
            }
        }
    }

    @Override
    public int compareTo(TVSeries o) {
        return this.getTitle().compareToIgnoreCase(o.getTitle());
    }

    @Override
    public String toString() {
        return this.title + " (" + this.releaseDate.getYear() +"): " + this.description + ". " +
                this.episodeList.size() + " episode(s).";
    }
}
