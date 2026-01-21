package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.PublicationTypeRepo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetPublicationTypeListControllerTest {

    @Test
    void shouldReturnAllPublicationTypes() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();
        repo.createPublicationType("Book");
        repo.createPublicationType("Magazine");

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
        PublicationTypeRepo repo = new PublicationTypeRepo();

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
        PublicationTypeRepo repo = new PublicationTypeRepo();
        repo.createPublicationType("Book");

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
