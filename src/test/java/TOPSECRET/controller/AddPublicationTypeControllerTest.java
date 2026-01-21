package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.PublicationTypeRepo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class AddPublicationTypeControllerTest {

    @Test
    void addPublicationTypeDelegatesToRepoAndReturnsCreatedType() {
        PublicationTypeRepo repo = new PublicationTypeRepo();
        AddPublicationTypeController controller = new AddPublicationTypeController(repo);

        PublicationType type = controller.addPublicationType("Hardcover");

        assertEquals("Hardcover", type.getPublicationType());
        assertTrue(repo.existsPublicationType("Hardcover"));
    }

    @Test
    void addPublicationTypeThrowsWhenTypeAlreadyExists() {
        PublicationTypeRepo repo = new PublicationTypeRepo();
        AddPublicationTypeController controller = new AddPublicationTypeController(repo);

        repo.createPublicationType("Magazine");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.addPublicationType("MAGAZINE")
        );

        assertEquals("Publication type already exists!", ex.getMessage());
    }
}
