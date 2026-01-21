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

    private User _owner;

    private List<Publication> _publications = new ArrayList<>();


    public Library(User user){

        _owner = user;

    }

    public User getUser() {
        return _owner;
    }

    /**
     * Returns a list of publications in this library with all required details
     * (title, author, publication type, ISBN/ISSN) formatted for UI display.
     *
     * <p>The method iterates through all publications, extracts details via
     * {@link PublicationDetails#PublicationDetails(Publication)}, and returns
     * an <strong>unmodifiable list</strong> to ensure encapsulation and
     * thread-safety.</p>
     *
     * @return unmodifiable {@link List}&lt;{@link PublicationDetails}&gt;
     *         containing publication details suitable for UI presentation
     */


    public List<PublicationDetails> getPublicationsInLibrary() {

        List <PublicationDetails> listWithDetails = new ArrayList<>();

        for (Publication p : _publications) {

            PublicationDetails pDetails = new PublicationDetails(p);

            listWithDetails.add(pDetails);
        }

        return Collections.unmodifiableList(listWithDetails);
    }

    private List<Publication> copyOfLibrary() {
        List<Publication> publicationsCopy = List.copyOf(_publications);
        return publicationsCopy;
    }

    public List<Publication> getAllPublications() {
        return copyOfLibrary();
    }

    private boolean verifyUnique(Publication selectedPublication) {
        for (Publication check : _publications) {
            if (selectedPublication.equals(check)) {
                return false;
            }
        }
        return true;
    }

    public boolean addPublicationToLibrary(Publication selectedPublication) {
        if (_publications == null) {
            _publications = new ArrayList<>();
        }
        if (selectedPublication == null) {
            return false;
        }
        if (!verifyUnique(selectedPublication)) {
            return false;
        }
        _publications.add(selectedPublication);
        return true;
    }
}
