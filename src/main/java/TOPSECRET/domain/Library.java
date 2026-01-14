package TOPSECRET.domain;

/**
 * Represents a {@link User} library in the domain model.
 *
 * <p>
 * A {@code Library} is an entity that groups publications and is owned by
 * a {@link User}. A library is uniquely identified within the system
 * and encapsulates the core data related to a user's library.
 *
 */

public class Library {

    private String _owner;


    public Library(String userID){

        _owner = userID;

    }

    public String getUserID() {
        return _owner;
    }

}
