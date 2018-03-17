package no.hiof.oskarsomme.model.media.production;

import no.hiof.oskarsomme.model.people.Person;
import no.hiof.oskarsomme.model.people.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Production {
    private String title;
    private String description;
    private int runningTime;
    private LocalDate releaseDate;
    private Person director;
    private List<Role> roles = new ArrayList<>();

    protected  Production(String title, String description,
                          int runningTime, LocalDate releaseDate, Person director) {
        this.title = title;
        this.description = description;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
        this.director = director;
    }

    protected  Production(String title, int runningTime, LocalDate releaseDate, Person director) {
        this.title = title;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
        this.director = director;
    }

    protected  Production(String title, String description,
                          int runningTime, LocalDate releaseDate) {
        this.title = title;
        this.description = description;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
    }

    protected  Production(String title, int runningTime, LocalDate releaseDate) {
        this.title = title;
        this.runningTime = runningTime;
        this.releaseDate = releaseDate;
    }

    protected  Production(String title, String description, int runningTime) {
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

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\nRunning time: " + runningTime + " minutes";
    }
}
