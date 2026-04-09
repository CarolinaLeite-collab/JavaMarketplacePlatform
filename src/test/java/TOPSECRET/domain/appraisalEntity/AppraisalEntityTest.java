package TOPSECRET.domain.appraisalEntity;

import TOPSECRET.domain.valueobject.AppraisalEntityId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Name;
import TOPSECRET.domain.valueobject.PublicationTypeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppraisalEntityTest {

    private Name _nameDouble;
    private PublicationTypeId _publicationTypeIdDouble1;
    private PublicationTypeId _publicationTypeIdDouble2;
    private GenreId _genreIdDouble1;
    private GenreId _genreIdDouble2;

    @BeforeEach
    void setup() {
        _nameDouble = mock(Name.class);
        _publicationTypeIdDouble1 = mock(PublicationTypeId.class);
        _publicationTypeIdDouble2 = mock(PublicationTypeId.class);
        _genreIdDouble1 = mock(GenreId.class);
        _genreIdDouble2 = mock(GenreId.class);

        when(_nameDouble.toString()).thenReturn("Name");

    }

    @Test
    void testConstructor() {

        //SUT
        AppraisalEntity appraisalEntity = new AppraisalEntity(_nameDouble,
                List.of(_publicationTypeIdDouble1, _publicationTypeIdDouble2),
                List.of(_genreIdDouble1, _genreIdDouble2));

    }

    @Test
    void shouldReturnId() {

        //Arrange
        List<PublicationTypeId> publicationTypesId = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genresId = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //SUT
        AppraisalEntity appraisalEntity = new AppraisalEntity(_nameDouble, publicationTypesId, genresId);

        //Act
        AppraisalEntityId id = appraisalEntity.identity();

        //Assert
        assertNotNull(id);

    }

    @Test
    void shouldReturnSameIdForSameObject() {

        //Arrange
        List<PublicationTypeId> publicationTypesId = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genresId = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //SUT
        AppraisalEntity appraisalEntity = new AppraisalEntity(_nameDouble, publicationTypesId, genresId);

        //Act
        AppraisalEntityId id1 = appraisalEntity.identity();
        AppraisalEntityId id2 = appraisalEntity.identity();

        //Assert
        assertEquals(id1, id2);

    }

    @Test
    void sameAsShouldReturnTrueWhenAppraisalEntitiesAreSame() {

        //Arrange
        List<PublicationTypeId> publicationTypesId = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genresId = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //SUT
        AppraisalEntity appraisalEntity1 = new AppraisalEntity(_nameDouble, publicationTypesId, genresId);
        AppraisalEntity appraisalEntity2 = new AppraisalEntity(_nameDouble, publicationTypesId, genresId);

        //Act
        boolean result = appraisalEntity1.sameAs(appraisalEntity2);

        //Assert
        assertFalse(result);

    }

    @Test
    void sameAsShouldReturnTrueForSameObject() {

        //SUT
        AppraisalEntity entity = new AppraisalEntity(_nameDouble, List.of(_publicationTypeIdDouble1), List.of(_genreIdDouble1));

        //Act
        boolean result = entity.sameAs(entity);

        //Assert
        assertTrue(result);

    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {

        //Arrange
        List<PublicationTypeId> publicationTypesId = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genresId = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //SUT
        AppraisalEntity appraisalEntity = new AppraisalEntity(_nameDouble, publicationTypesId, genresId);

        //Act
        boolean result = appraisalEntity.sameAs(null);

        //Assert
        assertFalse(result);

    }

    @Test
    void shouldReturnFalseWhenComparedWithDifferentType() {

        //Arrange
        List<PublicationTypeId> publicationTypesId = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genresId = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //SUT
        AppraisalEntity appraisalEntity = new AppraisalEntity(_nameDouble, publicationTypesId, genresId);

        String otherObject = "not an entity";

        //Act
        boolean result = appraisalEntity.sameAs(otherObject);

        //Assert
        assertFalse(result);

    }

    @Test
    void shouldReturnCorrectName() {

        //Arrange
        List<PublicationTypeId> publicationTypesId = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genresId = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //SUT
        AppraisalEntity appraisalEntity = new AppraisalEntity(_nameDouble, publicationTypesId, genresId);

        //Act
        Name result = appraisalEntity.getName();

        //Assert
        assertEquals(_nameDouble, result);

    }

    @Test
    void shouldReturnPublicationTypes() {

        //Arrange
        List<PublicationTypeId> publicationTypesId = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genresId = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //SUT
        AppraisalEntity entity = new AppraisalEntity(_nameDouble, publicationTypesId, genresId);

        //Act
        List<PublicationTypeId> result = entity.getPublicationTypes();

        //Assert
        assertEquals(publicationTypesId, result);

    }

    @Test
    void shouldReturnGenres() {

        //Arrange
        List<PublicationTypeId> publicationTypesId = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genresId = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //SUT
        AppraisalEntity entity = new AppraisalEntity(_nameDouble, publicationTypesId, genresId);

        //Act
        List<GenreId> result = entity.getGenres();

        //Assert
        assertEquals(genresId, result);

    }

    @Test
    void equalsShouldReturnTrueForSameObject() {

        //Arrange
        List<PublicationTypeId> publicationTypesId = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genresId = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //SUT
        AppraisalEntity entity = new AppraisalEntity(_nameDouble, publicationTypesId, genresId);

        //Act
        boolean result = entity.equals(entity);

        //Assert
        assertTrue(result);

    }

    @Test
    void equalsShouldReturnFalseForDifferentEntities() {

        //Arrange
        List<PublicationTypeId> publicationTypesId = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genresId = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        Name nameDouble1 = mock(Name.class);
        when(nameDouble1.toString()).thenReturn("Name1");

        Name nameDouble2 = mock(Name.class);
        when(nameDouble2.toString()).thenReturn("Name2");

        AppraisalEntity entity = new AppraisalEntity(nameDouble1, publicationTypesId, genresId);
        AppraisalEntity entity2 = new AppraisalEntity(nameDouble2, publicationTypesId, genresId);

        // Act
        boolean result = entity.equals(entity2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseForDifferentObjectsWithDifferentIds() {

        //Arrange
        List<PublicationTypeId> publicationTypesId = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genresId = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        Name nameDouble1 = mock(Name.class);
        when(nameDouble1.toString()).thenReturn("Name1");

        Name nameDouble2 = mock(Name.class);
        when(nameDouble2.toString()).thenReturn("Name2");

        //SUT
        AppraisalEntity entity1 = new AppraisalEntity(nameDouble1, publicationTypesId, genresId);
        AppraisalEntity entity2 = new AppraisalEntity(nameDouble2, publicationTypesId, genresId);

        //Act
        boolean result = entity1.equals(entity2);

        //Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseWhenNull() {

        //Arrange
        List<PublicationTypeId> publicationTypesId = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genresId = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //SUT
        AppraisalEntity entity = new AppraisalEntity(_nameDouble, publicationTypesId, genresId);

        //Act
        boolean result = entity.equals(null);

        //Assert
        assertFalse(result);

    }



}
