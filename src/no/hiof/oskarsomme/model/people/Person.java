package no.hiof.oskarsomme.model.people;

public class Person {
    private String firstName;
    private String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) {
            return false;
        }

        if(getClass() != other.getClass()) {
            return false;
        }

        Person otherPerson = (Person) other;

        return getFullName().equals(otherPerson.getFullName());
    }

    @Override
    public int hashCode() {
        return firstName.hashCode() + lastName.hashCode();
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
