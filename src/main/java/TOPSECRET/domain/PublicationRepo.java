package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

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

}
