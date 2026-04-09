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
    private AppraisalEntityId _appraisalEntityIdDouble;

    @BeforeEach
    void setup() {
        _nameDouble = mock(Name.class);
        _publicationTypeIdDouble1 = mock(PublicationTypeId.class);
        _publicationTypeIdDouble2 = mock(PublicationTypeId.class);
        _genreIdDouble1 = mock(GenreId.class);
        _genreIdDouble2 = mock(GenreId.class);
        _appraisalEntityIdDouble = mock(AppraisalEntityId.class);

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
    void shouldCreateAppraisalEntityWhenListsAreValid() {

        //Arrange
        Name name = mock(Name.class);
        List<PublicationTypeId> publications = List.of(mock(PublicationTypeId.class));
        List<GenreId> genres = List.of(mock(GenreId.class));

        //Act & SUT
        AppraisalEntity entity = new AppraisalEntity(name, publications, genres);

        //Assert
        assertEquals(name, entity.getName());
        assertEquals(publications, entity.getPublicationTypes());
        assertEquals(genres, entity.getGenres());

    }

    @Test
    void shouldThrowExceptionWhenPublicationTypesIsNull() {

        //Arrange
        Name name = mock(Name.class);
        List<GenreId> genres = List.of(mock(GenreId.class));

        //Act & SUT
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity(name, null, genres));

        //Assert
        assertEquals("List of publication types cannot be null or empty", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenPublicationTypesIsEmpty() {

        //Arrange
        Name name = mock(Name.class);
        List<GenreId> genres = List.of(mock(GenreId.class));

        //Act & SUT
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity(name, List.of(), genres));

        //Assert
        assertEquals("List of publication types cannot be null or empty", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenGenresIsNull() {

        //Arrange
        Name name = mock(Name.class);
        List<PublicationTypeId> publications = List.of(mock(PublicationTypeId.class));

        //Act & SUT
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity(name, publications, null));

        //Assert
        assertEquals("List of genres cannot be null or empty", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenGenresIsEmpty() {

        //Arrange
        Name name = mock(Name.class);
        List<PublicationTypeId> publications = List.of(mock(PublicationTypeId.class));

        //Act & SUT
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity(name, publications, List.of()));

        //Assert
        assertEquals("List of genres cannot be null or empty", exception.getMessage());

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
    void equalsShouldReturnTrueForDifferentObjectsWithSameId() {

        // Arrange
        List<PublicationTypeId> pubTypes = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genres = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //SUT
        AppraisalEntity entity1 = new AppraisalEntity(_appraisalEntityIdDouble, _nameDouble, pubTypes, genres);
        AppraisalEntity entity2 = new AppraisalEntity(_appraisalEntityIdDouble, _nameDouble, pubTypes, genres);

        //Act
        boolean result = entity1.equals(entity2);

        //Assert
        assertTrue(result);

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

    @Test
    void shouldCreateAppraisalEntityWithIdWhenValidArguments() {

        //Arrange
        List<PublicationTypeId> pubTypes = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genres = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //Act & SUT
        AppraisalEntity entity = new AppraisalEntity(_appraisalEntityIdDouble, _nameDouble, pubTypes, genres);

        //Assert
        assertEquals(_appraisalEntityIdDouble, entity.identity());
        assertEquals(_nameDouble, entity.getName());
        assertEquals(pubTypes, entity.getPublicationTypes());
        assertEquals(genres, entity.getGenres());

    }

    @Test
    void shouldThrowIfAppraisalEntityIdIsNull() {

        //Arrange
        List<PublicationTypeId> pubTypes = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genres = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //Act & SUT
        Exception exception = assertThrows(NullPointerException.class, () ->
                new AppraisalEntity(null, _nameDouble, pubTypes, genres));

        //Assert
        assertEquals("AppraisalEntityId is required", exception.getMessage());

    }

    @Test
    void shouldThrowIfPublicationTypesIsNullWithId() {

        //Arrange
        List<GenreId> genres = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //Act & SUT
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity(_appraisalEntityIdDouble, _nameDouble, null, genres));

        //Assert
        assertEquals("List of publication types cannot be null or empty", exception.getMessage());

    }

    @Test
    void shouldThrowIfPublicationTypesIsEmptyWithId() {

        //Arrange
        List<GenreId> genres = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //Act & SUT
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity(_appraisalEntityIdDouble, _nameDouble, List.of(), genres));

        //Assert
        assertEquals("List of publication types cannot be null or empty", exception.getMessage());

    }

    @Test
    void shouldThrowIfGenresIsNullWithId() {

        //Arrange
        List<PublicationTypeId> pubTypes = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);

        //Act & SUT
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity(_appraisalEntityIdDouble, _nameDouble, pubTypes, null));

        //Assert
        assertEquals("List of genres cannot be null or empty", exception.getMessage());

    }

    @Test
    void shouldThrowIfGenresIsEmptyWithId() {

        //Arrange
        List<PublicationTypeId> pubTypes = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);

        //Act & SUT
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity(_appraisalEntityIdDouble, _nameDouble, pubTypes, List.of()));

        //Assert
        assertEquals("List of genres cannot be null or empty", exception.getMessage());

    }

    @Test
    void equalsShouldReturnTrueForSameObjectWithId() {

        //Arrange
        List<PublicationTypeId> pubTypes = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genres = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        //Act & SUT
        AppraisalEntity entity = new AppraisalEntity(_appraisalEntityIdDouble, _nameDouble, pubTypes, genres);

        //Assert
        assertTrue(entity.equals(entity));

    }

    @Test
    void equalsShouldReturnFalseForDifferentIds() {

        //Arrange
        List<PublicationTypeId> pubTypes = Arrays.asList(_publicationTypeIdDouble1, _publicationTypeIdDouble2);
        List<GenreId> genres = Arrays.asList(_genreIdDouble1, _genreIdDouble2);

        AppraisalEntityId otherId = mock(AppraisalEntityId.class);

        //Act & SUT
        AppraisalEntity entity1 = new AppraisalEntity(_appraisalEntityIdDouble, _nameDouble, pubTypes, genres);
        AppraisalEntity entity2 = new AppraisalEntity(otherId, _nameDouble, pubTypes, genres);

        //Assert
        assertFalse(entity1.equals(entity2));
    }

}
