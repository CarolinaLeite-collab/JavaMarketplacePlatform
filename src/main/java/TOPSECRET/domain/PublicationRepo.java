package TOPSECRET.domain;
import java.util.ArrayList;
import java.util.List;

public class PublicationRepo {
    private List<Publication> _publications;

    public PublicationRepo() {
        _publications = new ArrayList<>();
    }

    /*public Publication create(PublicationType publicationType, String id, Title title, PublicationDate publicationDate, ...){

        // instantiate a new myPublicationInfo
        Publication myPublication = new Publication ();

        //already present?

        //add myPublicationInfo to repo
        _publications.add(myPublication);

        //return myPublication
        return myPublication;
    }*/

    private boolean publicationAlreadyExists (Publication myPublication) {
        for (Publication publication : _publications) {
            if (publication.equals(myPublication))
                return true;
        }
        return false;
    }
}
