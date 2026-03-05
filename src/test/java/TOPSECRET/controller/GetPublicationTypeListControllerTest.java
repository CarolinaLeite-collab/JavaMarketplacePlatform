package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.PublicationTypeFactory;
import TOPSECRET.domain.PublicationTypeRepo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetPublicationTypeListControllerTest {

    @Test
    void shouldReturnAllPublicationTypes() throws InstantiationException {
        // Arrange
        PublicationTypeFactory factory = new PublicationTypeFactory();
        PublicationTypeRepo repo = new PublicationTypeRepo(factory);
        repo.addPublicationType("Book");
        repo.addPublicationType("Magazine");

        GetPublicationTypeListController controller =
                new GetPublicationTypeListController(repo);

        // Act
        List<PublicationType> result =
                controller.getListOfPublicationTypes();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoPublicationTypesExist() {
        // Arrange
        PublicationTypeFactory factory = new PublicationTypeFactory();
        PublicationTypeRepo repo = new PublicationTypeRepo(factory);

        GetPublicationTypeListController controller =
                new GetPublicationTypeListController(repo);

        // Act
        List<PublicationType> result =
                controller.getListOfPublicationTypes();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void returnedListShouldNotBeModifiable() {
        PublicationTypeFactory factory = new PublicationTypeFactory();
        PublicationTypeRepo repo = new PublicationTypeRepo(factory);
        repo.addPublicationType("Book");

        GetPublicationTypeListController controller =
                new GetPublicationTypeListController(repo);

        List<PublicationType> result =
                controller.getListOfPublicationTypes();

        boolean modificationWorked = true;

        try {
            result.clear();
        } catch (Exception e) {
            modificationWorked = false;
        }

        if (modificationWorked) {
            fail("Returned list should be immutable");
        }
    }
}
