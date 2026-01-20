package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.PublicationTypeRepo;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GetPublicationTypeListControllerTest {

    @Test
    void shouldReturnAllPublicationTypes() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();
        repo.create("Book");
        repo.create("Magazine");

        GetPublicationTypeListController controller =
                new GetPublicationTypeListController(repo);

        // Act
        Collection<PublicationType> result =
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
        Collection<PublicationType> result =
                controller.getListOfPublicationTypes();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void modifyingReturnedCollectionShouldNotAffectRepository() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();
        repo.create("Book");

        GetPublicationTypeListController controller =
                new GetPublicationTypeListController(repo);

        // Act
        Collection<PublicationType> result =
                controller.getListOfPublicationTypes();
        result.clear();

        // Assert
        assertEquals(1, repo.getAll().size());
    }
}
