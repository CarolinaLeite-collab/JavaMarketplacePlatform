package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationTypeRepoTest {

    private PublicationTypeFactory _ptfDouble;
    private PublicationType _pubTypeDouble1;
    private PublicationType _pubTypeDouble2;

    @BeforeEach
    void setUp() throws InstantiationException {

        _ptfDouble = mock(PublicationTypeFactory.class);
        _pubTypeDouble1 = mock(PublicationType.class);
        _pubTypeDouble2 = mock(PublicationType.class);

        // Stub fallback for any input - guarantees true isolation
        when(_ptfDouble.createPublicationType(anyString())).thenReturn(mock(PublicationType.class));

        // Specific Stubs for BOOK and MAGAZINE inputs (in this order they take priority over anyString())
        when(_ptfDouble.createPublicationType("BOOK")).thenReturn(_pubTypeDouble1);
        when(_ptfDouble.createPublicationType("MAGAZINE")).thenReturn(_pubTypeDouble2);

    }


    @Test
    void addPublicationTypeShouldReturnPublicationType() throws InstantiationException {
        // Arrange
        String pubTypeName = "BOOK";

        // SUT
        PublicationTypeRepo repo = new PublicationTypeRepo(_ptfDouble);

        // Act
        PublicationType pubTypeResult = repo.addPublicationType(pubTypeName);

        // Assert
        assertEquals(_pubTypeDouble1, pubTypeResult);
    }

    @Test
    void shouldAddPublicationTypeSuccessfullyAndListNotEmpty() throws InstantiationException {
        // Arrange
        String pubTypeName = "BOOK";

        // SUT
        PublicationTypeRepo repo = new PublicationTypeRepo(_ptfDouble);

        // Act
        repo.addPublicationType(pubTypeName);

        // Assert
        assertEquals(1, repo.getAll().size());
    }

    @Test
    void shouldNotAllowDuplicatePublicationTypes() throws InstantiationException {
        //Arrange
        String pubTypeName = "MAGAZINE";
        when(_pubTypeDouble2.isSamePublicationType(pubTypeName)).thenReturn(true);

        // SUT
        PublicationTypeRepo repo = new PublicationTypeRepo(_ptfDouble);

        //Act
        repo.addPublicationType(pubTypeName);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> repo.addPublicationType(pubTypeName));
    }

    @Test
    void shouldBeAbleToAddMultiplePublicationTypes() throws InstantiationException {
        //Arrange
        String pubTypeName = "MAGAZINE";
        String pubTypeName2 = "BOOK";
        String pubTypeName3 = "POKEMON_CARD";

        //SUT
        PublicationTypeRepo repo = new PublicationTypeRepo(_ptfDouble);

        //Act
        repo.addPublicationType(pubTypeName);
        repo.addPublicationType(pubTypeName2);
        repo.addPublicationType(pubTypeName3);

        //Assert
        assertEquals(3, repo.getAll().size());
    }

    @Test
    void shouldThrowCorrectMessageOnDuplicatePublicationTypes() throws InstantiationException {
        // Arrange
        String pubTypeName = "BOOK";
        when(_pubTypeDouble1.isSamePublicationType(pubTypeName)).thenReturn(true);

        //SUT
        PublicationTypeRepo repo = new PublicationTypeRepo(_ptfDouble);
        repo.addPublicationType(pubTypeName);

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repo.addPublicationType(pubTypeName));

        //Assert
        assertEquals("This publication type already exists!", exception.getMessage());

    }
}
