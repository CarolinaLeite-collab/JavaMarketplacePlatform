package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Publishers matching US006 class diagram.
 * registerPublisher() main method orchestrates publisherExists() -> new Publisher() -> add() -> return
 */

public class PublisherRepo {

    // To get list of all publishers registered on MiteLovers
    private final List<PublishingCompany> _publishers = new ArrayList<>();

    // This method orchestrates publisherExists() -> create()/new Publisher -> add()
    public PublishingCompany registerPublisher(String publisherName) {
        if (publisherExists(publisherName)) {
            return null;
        }
        //  Instantiate a new Publisher
        PublishingCompany newPublisher = new PublishingCompany(publisherName); //this is create(publisherName) from SD

        // Add new publisher to repo
        _publishers.add(newPublisher);

        return newPublisher;
    }

    /**
     * Private helper: case-insensitive existence check using Publisher.equals().
     */

    private boolean publisherExists(String publisherName) {
        for (PublishingCompany publisher : _publishers) {
            if (publisher.equals(new PublishingCompany(publisherName))) { // case-insensitive
                return true;
            }
        }
        return false;
    }

}
