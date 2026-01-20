package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublicationTypeRepoTest {

    @Test
    void shouldCreatePublicationTypeSuccessfully() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();

        // Act
        PublicationType type = repo.create("Book");

        // Assert
        assertNotNull(type);
        assertEquals("Book", type.getPublicationType());
    }

    @Test
    void shouldNotAllowDuplicatePublicationTypes() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();
        repo.create("Magazine");

        // Act
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> repo.create("Magazine")
        );

        // Assert
        assertEquals("This publication type already exists!", exception.getMessage());
    }

    @Test
    void shouldRecognizeExistingPublicationType() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();
        repo.create("Book");

        // Act
        boolean exists = repo.exists("Book");

        // Assert
        assertTrue(exists);
    }

    @Test
    void shouldBeCaseInsensitiveWhenCheckingExistence() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();
        repo.create("Book");

        // Act
        boolean lowerCaseExists = repo.exists("book");
        boolean upperCaseExists = repo.exists("BOOK");

        // Assert
        assertTrue(lowerCaseExists);
        assertTrue(upperCaseExists);
    }

    @Test
    void shouldReturnAllCreatedPublicationTypes() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();
        repo.create("Book");
        repo.create("Magazine");

        // Act
        int numberOfTypes = repo.getAll().size();

        // Assert
        assertEquals(2, numberOfTypes);
    }

    @Test
    void returnedCollectionShouldNotAllowModifyingInternalState() {
        // Arrange
        PublicationTypeRepo repo = new PublicationTypeRepo();
        repo.create("Book");

        // Act
        var allTypes = repo.getAll();
        allTypes.clear();

        int numberOfTypesAfterExternalModification =
                repo.getAll().size();

        // Assert
        assertEquals(1, numberOfTypesAfterExternalModification);
    }
}
