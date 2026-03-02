package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublishingCompanyRepoTest {

    private PublisherRepo repo;

    @BeforeEach
    void setUp() {
        repo = new PublisherRepo();
        // First publisher added to repo
        repo.registerPublisher("Penguin Random House");
    }

    @Test
    void usingConstructorPublisherRepo() {
        PublisherRepo repo = new PublisherRepo();
        assertNotNull(repo);
    }

    @Test
    void registeringNewPublisher(){
        PublishingCompany pub1 = repo.registerPublisher("Pendant Publishing");
        assertNotNull(pub1);
        assertEquals("Pendant Publishing", pub1.getName());
    }

    @Test
    void registeringNewPublisherAfterTrimAndSpaceNormalization(){
        PublishingCompany pub1 = repo.registerPublisher(" Simon   &  Schuster  ");
        assertNotNull(pub1);
        assertEquals("Simon & Schuster", pub1.getName());
    }

    @Test
    void registeringExistingPublisher() {
       PublishingCompany pub1 = repo.registerPublisher("Penguin Random House"); // already registered in setUp repo
       assertNull(pub1); // duplicate returns null
    }

    @Test
    void registeringExistingPublisherTrimAndSpaceNormalizationCaseInsensitive() {
        PublishingCompany pub1 = repo.registerPublisher("   PengUin   RAndom  HOUSE ");
        assertNull(pub1);
    }

    @Test
    void RegisteringEmptyPublisher() {
        // Publisher constructor throws IllegalArgumentException on empty/whitespace
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            repo.registerPublisher(" "); // whitespace triggers trim().isEmpty() in Publisher constructor
        });
        assertEquals("Publisher name cannot be null, empty or blank", ex.getMessage());
    }

    @Test
    void registeringInvalidPublisher() {
        // Publisher constructor throws IllegalArgumentException on invalid
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            repo.registerPublisher(null);
        });
        assertEquals("Publisher name cannot be null, empty or blank", ex.getMessage());
    }

}