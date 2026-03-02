package TOPSECRET.controller;

import TOPSECRET.domain.PublishingCompany;
import TOPSECRET.domain.PublisherRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterPublishingCompanyControllerTest {

    private PublisherRepo _publisherRepo;
    private RegisterPublishingCompanyController _controller;

    @BeforeEach
    void setUp() {
        _publisherRepo = new PublisherRepo();
        _controller = new RegisterPublishingCompanyController(_publisherRepo);
        // First registered publisher
        _publisherRepo.registerPublisher("Penguin Random House");
    }

    @Test
    void usingConstructorRegisterPublisherController() {
        // tests our controller object was created successfully
        RegisterPublishingCompanyController controller = new RegisterPublishingCompanyController(_publisherRepo);
        assertNotNull(controller);
    }

    @Test
    void registeringNewPublisher() {
        // delegating task of registering to PublisherRepo and returning the registered Publisher
        PublishingCompany newPublisher = _controller.registerPublisher("Bertrand Editora");
        assertNotNull(newPublisher);
        assertEquals("Bertrand Editora", newPublisher.getName());
    }

    @Test
    void registeringNewPublisherAfterTrimAndSpaceNormalization() {
        // delegating task of registering to PublisherRepo and returning the registered Publisher
        PublishingCompany newPublisher = _controller.registerPublisher(" Porto  Editora  ");
        assertNotNull(newPublisher);
        assertEquals("Porto Editora", newPublisher.getName());
    }

    @Test
    void registeringExistingPublisher() {
        PublishingCompany newPublisher = _controller.registerPublisher("Penguin Random House");
        assertNull(newPublisher);
    }

    @Test
    void registeringExistingPublisherTrimAndSpaceNormalizationCaseInsensitive() {
        PublishingCompany duplicatePublisher = _controller.registerPublisher("  PENGUIN   raNDom  houSE ");
        assertNull(duplicatePublisher);
    }

    @Test
    void registeringEmptyPublisher(){
        assertThrows(IllegalArgumentException.class, () -> _controller.registerPublisher(" "));
    }

    @Test
    void registeringInvalidPublisher(){
        assertThrows(IllegalArgumentException.class, () -> _controller.registerPublisher(null));
    }

}