package no.hiof.oskarsomme.model.media.tvseries;

import no.hiof.oskarsomme.model.people.Role;

import java.time.LocalDate;
import java.util.*;

public class TVSeries implements Comparable<TVSeries>{
    private String title;
    private String description;
    private LocalDate releaseDate;
    private String posterURL;
    private int id;


    private List<Episode> episodeList;
    private int averageRunningTime;
    private int numberOfSeasons;
    public static List<TVSeries> listOfTVseries = new ArrayList<>();

    public TVSeries(String title, String description, LocalDate releaseDate, String posterURL, int id) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.posterURL = posterURL;
        this.id = id;
        this.episodeList = new ArrayList<>();
        this.averageRunningTime = 0;
        this.numberOfSeasons = 0;
        listOfTVseries.add(this);
    }

    public TVSeries(String title, String description, LocalDate releaseDate, String posterURL) {
        this(title, description, releaseDate, posterURL, 0);
    }

    public TVSeries(String title, String description, LocalDate releaseDate) {
        this(title, description, releaseDate, null, 0);
    }


    public TVSeries() {
        this(null, null, null, null, 0);
    }

    /*
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
    */

    public void addEpisode(Episode episode) {
        episodeList.add(episode);
    }

    public void printEpisodes() {
        for(Episode episode : episodeList) {
            System.out.println(episode);
        }
    }

    public List<Episode> getEpisodes() {
        return episodeList;
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

    public boolean isEmpty() {
        return title == null && releaseDate == null && description == null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(TVSeries o) {
        return this.getTitle().compareToIgnoreCase(o.getTitle());
    }

    public static Comparator<TVSeries> sortByTitle = new Comparator<TVSeries>() {
        @Override
        public int compare(TVSeries o1, TVSeries o2) {
            return o1.getTitle().compareToIgnoreCase(o2.getTitle());
        }
    };

    public static Comparator<TVSeries> sortByReleaseDate = Comparator.comparing(TVSeries::getReleaseDate);

    public static Comparator<TVSeries> sortByNumberOfEpisodes = Comparator.comparingInt(TVSeries::getNumberOfEpisodes);

    @Override
    public String toString() {
        return this.title + " (" + this.releaseDate.getYear() +"): " + this.description + ". " +
                this.episodeList.size() + " episode(s).";
    }
}
