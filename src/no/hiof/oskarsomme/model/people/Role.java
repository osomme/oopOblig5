package no.hiof.oskarsomme.model.people;

public class Role {
    private String firstName;
    private String lastName;
    private Person actor;

    public Role(String firstName, String lastName, Person actor) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.actor = actor;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Person getActor() {
        return actor;
    }

    public void setActor(Person actor) {
        this.actor = actor;
    }

    @Override
    public String toString() {
        return "The character '" + firstName + " " + lastName + "' is played by " + actor;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) {
            return false;
        }

        if(getClass() != other.getClass()) {
            return false;
        }

        Role otherRole = (Role) other;

        return firstName.equals(otherRole.getFirstName())
                && lastName.equals(otherRole.getLastName())
                && actor.equals(otherRole.getActor());
    }

    @Override
    public int hashCode() {
        if(actor == null) {
            return 0;
        }

        return firstName.hashCode() + lastName.hashCode() + actor.hashCode();
    }
}
