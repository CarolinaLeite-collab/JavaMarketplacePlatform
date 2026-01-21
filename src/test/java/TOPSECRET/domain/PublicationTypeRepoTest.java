package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicationTypeRepoTest {

    @Test
    void shouldCreatePublicationTypeSuccessfully() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();

        // Act
        PublicationType type = repo.createPublicationType("Book");

        // Assert
        assertNotNull(type);
        assertEquals("Book", type.getPublicationType());
    }

    @Test
    void shouldNotAllowDuplicatePublicationTypes() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();
        repo.createPublicationType("Magazine");

        // Act
        boolean duplicateWasCreated = true;

        try {
            repo.createPublicationType("Magazine");
        } catch (Exception e) {
            duplicateWasCreated = false;
        }

        // Assert
        if (duplicateWasCreated) {
            fail("It should not be possible to create duplicate publication types");
        }
    }

    @Test
    void shouldRecognizeExistingPublicationType() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();
        repo.createPublicationType("Book");

        // Act
        boolean exists = repo.existsPublicationType("Book");

        // Assert
        assertTrue(exists);
    }

    @Test
    void shouldBeCaseInsensitiveWhenCheckingExistence() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();
        repo.createPublicationType("Book");

        // Act
        boolean lowerCaseExists = repo.existsPublicationType("book");
        boolean upperCaseExists = repo.existsPublicationType("BOOK");

        // Assert
        assertTrue(lowerCaseExists);
        assertTrue(upperCaseExists);
    }

    @Test
    void shouldReturnAllCreatedPublicationTypes() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();
        repo.createPublicationType("Book");
        repo.createPublicationType("Magazine");

        // Act
        int numberOfTypes = repo.getAll().size();

        // Assert
        assertEquals(2, numberOfTypes);
    }

    @Test
    void returnedListCannotBeModifiedFromOutside() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();
        repo.createPublicationType("Book");

        List<PublicationType> returnedList = repo.getAll();

        // Act
        boolean modificationSucceeded = true;

        try {
            returnedList.clear();   // tentativa de modificação externa
        } catch (Exception e) {
            modificationSucceeded = false;
        }

        // Assert
        if (modificationSucceeded) {
            fail("External code should not be able to modify the returned list");
        }
    }
}
