package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing {@link Publication} instances.
 * <p>
 * Provides methods to add new publications, check for duplicates, retrieve a specific publication,
 * and obtain publications that are not present in a given list.
 * Ensures that publications are not null and prevents adding duplicates.
 * </p>
 */

public class PublicationRepo {
    private List<Publication> _publications;

    public PublicationRepo() {
        _publications = new ArrayList<>();
    }

    public Publication add(Publication myPublication) {
        // see if its null
        if (myPublication == null) throw new IllegalArgumentException("Publication is required");
        //already present? Then don't add
        boolean present = publicationAlreadyExists(myPublication);
        if (present) {
            throw new IllegalArgumentException("Publication already exists in the repository");
        }
        //now we know publication does not exist so...
        //add myPublicationInfo to repo
        _publications.add(myPublication);

        //return myPublication
        return myPublication;
    }

    private boolean publicationAlreadyExists(Publication myPublication) {
        for (Publication publication : _publications) {
            if (publication.equals(myPublication))
                return true;
        }
        return false;
    }

    //check publications that are still out of library
    public List<Publication> getDifferentOf(List<Publication> existentPublications) {
        List<Publication> result = new ArrayList<>();
        for (Publication publication : _publications){
            if (!existentPublications.contains(publication)){
                result.add(publication);
            }
        }
        return List.copyOf(result);
    }

    public Publication getPublication(Publication publication) {
        return _publications.stream()
                .filter(p -> p.equals(publication))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Publication not found"));
    }

}
