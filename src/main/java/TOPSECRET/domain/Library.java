package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private List<PublicationInfo> publications = new ArrayList<>();


    public Library(String userID){

        _owner = userID;

    }

    public String getUserID() {
        return _owner;
    }

    /**
     * Returns a list of publications in this library with required details
     * (title, author, type, ISBN/ISSN) for UI display.
     *
     * @return unmodifiable list of publications suitable for UI presentation
     */

    public List<String> getPublicationsInLibrary() {

        List <String> listWithDetails = new ArrayList<>();

        for (PublicationInfo p : publications) {
            String pubDetails = String.format("%s | %s | %s | %s/%s",
                    p.getTitle(),
                    p.getAuthor(),
                    //p.getType(),
                    p.getISSN(),
                    p.getISBN()
            );
            listWithDetails.add(pubDetails);
        }

        return Collections.unmodifiableList(listWithDetails);
    }

    


}
