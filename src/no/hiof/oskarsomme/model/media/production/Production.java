package no.hiof.oskarsomme.model.media.production;

import no.hiof.oskarsomme.model.people.Person;
import no.hiof.oskarsomme.model.people.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Production {
    private String title;
    private String description;
    private String posterURL;
    private int runningTime;
    private int id;
    private LocalDate releaseDate;
    private Person director;
    private List<Role> roles = new ArrayList<>();

    protected Production(String title, String description,
                          int runningTime, LocalDate releaseDate, Person director) {
        this.title = title;
        this.description = description;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
        this.director = director;
    }

    protected Production(String title, int runningTime, LocalDate releaseDate, Person director) {
        this.title = title;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
        this.director = director;
    }

    protected Production(String title, String description, int runningTime, LocalDate releaseDate, String posterURL, int id) {
        this.title = title;
        this.description = description;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
        this.posterURL = posterURL;
        this.id = id;
    }

    protected Production(String title, String description, int runningTime, LocalDate releaseDate, String posterURL) {
        this.title = title;
        this.description = description;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
        this.posterURL = posterURL;
    }

    protected Production(String title, String description, int runningTime, LocalDate releaseDate) {
        this.title = title;
        this.description = description;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
    }

    protected Production(String title, int runningTime, LocalDate releaseDate, int id) {
        this.title = title;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
        this.id = id;
    }

    protected Production(String title, int runningTime, LocalDate releaseDate) {
        this.title = title;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
    }

    protected Production(String title, String description, int runningTime) {
        this.title = title;
        this.description = description;
        this.runningTime = runningTime;
    }

    protected Production(String title, int runningTime) {
        this.title = title;
        this.runningTime = runningTime;
    }

    protected Production(String title) {
        this.title = title;
    }

    protected Production() {

    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void addRoles(List<Role> roles) {
        this.roles.addAll(roles);
    }

    public void printRoles() {
        for(Role role : roles) {
            System.out.println(role);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Person getDirector() {
        return director;
    }

    public void setDirector(Person director) {
        this.director = director;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public String getPosterURL() {
        if(posterURL != null) {
            return posterURL;
        }

        return "";
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return title == null && releaseDate == null && description == null;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\nRunning time: " + runningTime + " minutes";
    }
}
