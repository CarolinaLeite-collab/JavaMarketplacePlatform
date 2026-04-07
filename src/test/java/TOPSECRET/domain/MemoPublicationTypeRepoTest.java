package TOPSECRET.domain;

import TOPSECRET.domain.publicationtype.PublicationType;
import TOPSECRET.domain.publicationtype.PublicationTypeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoPublicationTypeRepoTest {

    private PublicationTypeFactory _ptfDouble;
    private PublicationType _publicationType1Double;
    private PublicationType _publicationType2Double;

    @BeforeEach
    void setUp() throws InstantiationException {

        _ptfDouble = mock(PublicationTypeFactory.class);
        _publicationType1Double = mock(PublicationType.class);
        _publicationType2Double = mock(PublicationType.class);

        // Stub fallback for any input - guarantees true isolation
        when(_ptfDouble.createPublicationType(anyString())).thenReturn(mock(PublicationType.class));

        // Specific Stubs for BOOK and MAGAZINE inputs (in this order they take priority over anyString())
        when(_ptfDouble.createPublicationType("BOOK")).thenReturn(_publicationType1Double);
        when(_ptfDouble.createPublicationType("MAGAZINE")).thenReturn(_publicationType2Double);

    }

    @Test
    void constructorOfPublicationTypeRepoShouldCreatePublicationTypeRepo() {

        //SUT
        new MemoPublicationTypeRepo(_ptfDouble);

    }


    @Test
    void addPublicationTypeShouldReturnPublicationType() {
        // Arrange
        String pubTypeName = "BOOK";

        // SUT
        MemoPublicationTypeRepo repo = new MemoPublicationTypeRepo(_ptfDouble);

        // Act
        PublicationType pubTypeResult = repo.addPublicationType(pubTypeName);

        // Assert
        assertEquals(_publicationType1Double, pubTypeResult);
    }

    @Test
    void shouldAddPublicationTypeSuccessfullyAndListNotEmpty() {
        // Arrange
        String pubTypeName = "BOOK";

        // SUT
        MemoPublicationTypeRepo repo = new MemoPublicationTypeRepo(_ptfDouble);

        // Act
        repo.addPublicationType(pubTypeName);

        // Assert
        assertEquals(1, repo.getAll().size());
    }

    @Test
    void shouldNotAllowDuplicatePublicationTypes() {
        //Arrange
        String pubTypeName = "MAGAZINE";
        when(_publicationType2Double.isSamePublicationType(pubTypeName)).thenReturn(true);

        // SUT
        MemoPublicationTypeRepo repo = new MemoPublicationTypeRepo(_ptfDouble);

        //Act
        repo.addPublicationType(pubTypeName);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> repo.addPublicationType(pubTypeName));
    }

    @Test
    void shouldBeAbleToAddMultiplePublicationTypes() {
        //Arrange
        String pubTypeName = "MAGAZINE";
        String pubTypeName2 = "BOOK";
        String pubTypeName3 = "POKEMON_CARD";

        //SUT
        MemoPublicationTypeRepo repo = new MemoPublicationTypeRepo(_ptfDouble);

        //Act
        repo.addPublicationType(pubTypeName);
        repo.addPublicationType(pubTypeName2);
        repo.addPublicationType(pubTypeName3);

        //Assert
        assertEquals(3, repo.getAll().size());
    }

    @Test
    void shouldThrowCorrectMessageOnDuplicatePublicationTypes() {
        // Arrange
        String pubTypeName = "BOOK";
        when(_publicationType1Double.isSamePublicationType(pubTypeName)).thenReturn(true);

        //SUT
        MemoPublicationTypeRepo repo = new MemoPublicationTypeRepo(_ptfDouble);
        repo.addPublicationType(pubTypeName);

        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repo.addPublicationType(pubTypeName));

        //Assert
        assertEquals("This publication type already exists!", exception.getMessage());

    }
}
