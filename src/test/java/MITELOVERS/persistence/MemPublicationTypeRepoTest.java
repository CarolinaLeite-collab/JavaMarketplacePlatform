package MITELOVERS.persistence.mem;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.publicationtype.PublicationTypeFactory;
import MITELOVERS.domain.valueobject.PublicationTypeId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemPublicationTypeRepoTest {

    @Test
    void constructorShouldCreateNonNullPublicationTypeRepo() {
        //Arrange
        PublicationTypeFactory ptfDouble = mock(PublicationTypeFactory.class);

        //SUT + Act
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo(ptfDouble);

        //Assert
        assertNotNull(repo);

    }


    @Test
    void addPublicationTypeShouldReturnPublicationType() {
        // Arrange
        String pubTypeName = "BOOK";

        PublicationTypeFactory ptfDouble = mock(PublicationTypeFactory.class);
        PublicationType publicationType1Double = mock(PublicationType.class);
        when(ptfDouble.createPublicationType("BOOK")).thenReturn(publicationType1Double);

        // SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo(ptfDouble);

        // Act
        PublicationType pubTypeResult = repo.addPublicationType(pubTypeName);

        // Assert
        assertEquals(publicationType1Double, pubTypeResult);
    }

    @Test
    void shouldAddPublicationTypeSuccessfullyAndListNotEmpty() {
        // Arrange
        String pubTypeName = "BOOK";

        PublicationTypeFactory ptfDouble = mock(PublicationTypeFactory.class);
        PublicationType publicationType1Double = mock(PublicationType.class);
        when(ptfDouble.createPublicationType("BOOK")).thenReturn(publicationType1Double);

        // SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo(ptfDouble);

        // Act
        repo.addPublicationType(pubTypeName);

        List<PublicationType> result = new ArrayList<>();
        repo.findAll().forEach(result::add);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void shouldNotAllowDuplicatePublicationTypes() {
        //Arrange
        String pubTypeName = "MAGAZINE";

        PublicationTypeFactory ptfDouble = mock(PublicationTypeFactory.class);

        PublicationType firstPublicationTypeDouble = mock(PublicationType.class);
        PublicationType secondPublicationTypeDouble = mock(PublicationType.class);

        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);

        when(firstPublicationTypeDouble.identity()).thenReturn(publicationTypeIdDouble);

        when(secondPublicationTypeDouble.identity()).thenReturn(publicationTypeIdDouble);

        when(ptfDouble.createPublicationType(pubTypeName)).thenReturn(firstPublicationTypeDouble).thenReturn(secondPublicationTypeDouble);

        // SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo(ptfDouble);

        //Act
        repo.addPublicationType(pubTypeName);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> repo.addPublicationType(pubTypeName));
    }

    @Test
    void shouldBeAbleToAddMultiplePublicationTypes() {
        //Arrange
        String pubTypeName1 = "MAGAZINE";
        String pubTypeName2 = "BOOK";
        String pubTypeName3 = "POKEMON CARD";

        PublicationTypeFactory ptfDouble = mock(PublicationTypeFactory.class);

        PublicationType publicationType1Double = mock(PublicationType.class);
        PublicationType publicationType2Double = mock(PublicationType.class);
        PublicationType publicationType3Double = mock(PublicationType.class);

        PublicationTypeId publicationTypeId1Double = mock(PublicationTypeId.class);
        PublicationTypeId publicationTypeId2Double = mock(PublicationTypeId.class);
        PublicationTypeId publicationTypeId3Double = mock(PublicationTypeId.class);

        when(publicationType1Double.identity()).thenReturn(publicationTypeId1Double);
        when(publicationType2Double.identity()).thenReturn(publicationTypeId2Double);
        when(publicationType3Double.identity()).thenReturn(publicationTypeId3Double);

        when(ptfDouble.createPublicationType("MAGAZINE")).thenReturn(publicationType1Double);
        when(ptfDouble.createPublicationType("BOOK")).thenReturn(publicationType2Double);
        when(ptfDouble.createPublicationType("POKEMON CARD")).thenReturn(publicationType3Double);

        //SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo(ptfDouble);

        //Act
        repo.addPublicationType(pubTypeName1);
        repo.addPublicationType(pubTypeName2);
        repo.addPublicationType(pubTypeName3);

        List<PublicationType> result = new ArrayList<>();
        repo.findAll().forEach(result::add);

        //Assert
        assertEquals(3, result.size());
    }

    @Test
    void shouldThrowCorrectMessageOnDuplicatePublicationTypes() {
        // Arrange
        String pubTypeName = "BOOK";

        PublicationTypeFactory ptfDouble = mock(PublicationTypeFactory.class);

        PublicationType firstPublicationTypeDouble = mock(PublicationType.class);
        PublicationType secondPublicationTypeDouble = mock(PublicationType.class);
        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);

        when(firstPublicationTypeDouble.identity()).thenReturn(publicationTypeIdDouble);
        when(secondPublicationTypeDouble.identity()).thenReturn(publicationTypeIdDouble);

        when(ptfDouble.createPublicationType(pubTypeName)).thenReturn(firstPublicationTypeDouble).thenReturn(secondPublicationTypeDouble);

        //SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo(ptfDouble);

        // Act
        repo.addPublicationType(pubTypeName);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repo.addPublicationType(pubTypeName));

        //Assert
        assertEquals("This publication type already exists!", exception.getMessage());

    }

    @Test
    void ofIdentityShouldReturnEmptyWhenIdNotPresent() {
        // Arrange
        PublicationTypeFactory ptfDouble = mock(PublicationTypeFactory.class);

        //SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo(ptfDouble);

        //This id was never saved in MemoRepo
        PublicationTypeId notSavedIdDouble = mock(PublicationTypeId.class);

        // Act
        var result = repo.ofIdentity(notSavedIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void ofIdentityShouldReturnPublicationTypeWhenIdPresent() {
        // Arrange
        String pubTypeName = "BOOK";

        PublicationTypeFactory ptfDouble = mock(PublicationTypeFactory.class);

        PublicationType publicationTypeDouble = mock(PublicationType.class);
        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);

        when(publicationTypeDouble.identity()).thenReturn(publicationTypeIdDouble);
        when(ptfDouble.createPublicationType(pubTypeName)).thenReturn(publicationTypeDouble);

        //SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo(ptfDouble);
        repo.addPublicationType(pubTypeName);

        // Act
        var result = repo.ofIdentity(publicationTypeIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(publicationTypeDouble, result.get());
    }

    @Test
    void findAllKeysShouldReturnListOfIds() {

        //Arrange
        String pubTypeName1 = "MAGAZINE";
        String pubTypeName2 = "BOOK";
        String pubTypeName3 = "POKEMON CARD";

        PublicationTypeFactory ptfDouble = mock(PublicationTypeFactory.class);

        PublicationType publicationType1Double = mock(PublicationType.class);
        PublicationType publicationType2Double = mock(PublicationType.class);
        PublicationType publicationType3Double = mock(PublicationType.class);

        PublicationTypeId publicationTypeId1Double = mock(PublicationTypeId.class);
        PublicationTypeId publicationTypeId2Double = mock(PublicationTypeId.class);
        PublicationTypeId publicationTypeId3Double = mock(PublicationTypeId.class);

        when(publicationType1Double.identity()).thenReturn(publicationTypeId1Double);
        when(publicationType2Double.identity()).thenReturn(publicationTypeId2Double);
        when(publicationType3Double.identity()).thenReturn(publicationTypeId3Double);

        when(ptfDouble.createPublicationType(pubTypeName1)).thenReturn(publicationType1Double);
        when(ptfDouble.createPublicationType(pubTypeName2)).thenReturn(publicationType2Double);
        when(ptfDouble.createPublicationType(pubTypeName3)).thenReturn(publicationType3Double);

        //SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo(ptfDouble);

        //Act
        repo.addPublicationType(pubTypeName1);
        repo.addPublicationType(pubTypeName2);
        repo.addPublicationType(pubTypeName3);
        List<PublicationTypeId> result = repo.findAllKeys();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(publicationTypeId1Double));
        assertTrue(result.contains(publicationTypeId2Double));
        assertTrue(result.contains(publicationTypeId3Double));

    }

    @Test
    void findAllKeysShouldReturnDefensiveCopy() {

        //Arrange
        String pubTypeName1 = "MAGAZINE";
        String pubTypeName2 = "BOOK";
        String pubTypeName3 = "POKEMON CARD";

        PublicationTypeFactory ptfDouble = mock(PublicationTypeFactory.class);

        PublicationType publicationType1Double = mock(PublicationType.class);
        PublicationType publicationType2Double = mock(PublicationType.class);
        PublicationType publicationType3Double = mock(PublicationType.class);

        PublicationTypeId publicationTypeId1Double = mock(PublicationTypeId.class);
        PublicationTypeId publicationTypeId2Double = mock(PublicationTypeId.class);
        PublicationTypeId publicationTypeId3Double = mock(PublicationTypeId.class);

        when(publicationType1Double.identity()).thenReturn(publicationTypeId1Double);
        when(publicationType2Double.identity()).thenReturn(publicationTypeId2Double);
        when(publicationType3Double.identity()).thenReturn(publicationTypeId3Double);

        when(ptfDouble.createPublicationType(pubTypeName1)).thenReturn(publicationType1Double);
        when(ptfDouble.createPublicationType(pubTypeName2)).thenReturn(publicationType2Double);
        when(ptfDouble.createPublicationType(pubTypeName3)).thenReturn(publicationType3Double);

        //SUT
        MemPublicationTypeRepo repo = new MemPublicationTypeRepo(ptfDouble);


        repo.addPublicationType(pubTypeName1);
        repo.addPublicationType(pubTypeName2);
        repo.addPublicationType(pubTypeName3);

        List<PublicationTypeId> result = repo.findAllKeys();
        result.clear();
        List<PublicationTypeId> resultAfterClear = repo.findAllKeys();

        // Assert
        assertEquals(3, resultAfterClear.size());
        assertTrue(resultAfterClear.contains(publicationTypeId1Double));
        assertTrue(resultAfterClear.contains(publicationTypeId2Double));
        assertTrue(resultAfterClear.contains(publicationTypeId3Double));
    }

}
