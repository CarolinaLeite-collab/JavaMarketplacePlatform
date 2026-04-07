package TOPSECRET.persistence.mem;

import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.publication.PublicationFactory;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoPublicationRepoTest {

    private PublicationFactory _publicationFactoryDouble;
    private PublicationTypeId _typeIdDouble;
    private Year _yearDouble;
    private Title _titleDouble;
    private AuthorId _authorIdDouble;
    private GenreId _genreIdDouble;

    @BeforeEach
    void setUp() {
        _publicationFactoryDouble = mock(PublicationFactory.class);
        _typeIdDouble = mock(PublicationTypeId.class);
        _titleDouble = mock(Title.class);
        _authorIdDouble = mock(AuthorId.class);
        _yearDouble = mock(Year.class);
        _genreIdDouble = mock(GenreId.class);
    }

    @Test
    void constructorValidFactoryCreatesMemoPublicationRepo() {
        // SUT
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);

        // Assert
        assertNotNull(repo);
    }

    @Test
    void addPublicationStoresPublicationReturnedByFactory() {
        //Arrange
        Publication publicationDouble = mock(Publication.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);

        when(publicationDouble.identity()).thenReturn(publicationIdDouble);
        when(_publicationFactoryDouble.createPublication(_titleDouble, _authorIdDouble, _yearDouble, _typeIdDouble, _genreIdDouble)).thenReturn(publicationDouble);

        //SUT
        MemoPublicationRepo memoPublicationRepo = new MemoPublicationRepo(_publicationFactoryDouble);

        //Act
        Publication result = memoPublicationRepo.addPublication(_titleDouble, _authorIdDouble, _yearDouble, _typeIdDouble, _genreIdDouble);

        //Assert
        assertSame(publicationDouble, result);
    }

    @Test
    void addPublicationThrowsWhenPublicationAlreadyExists() {
        // Arrange
        Title title = new Title("Ficção");
        AuthorId authorId = new AuthorId("José Saramago");
        Year year = Year.of(2000);

        Publication _publicationDouble = mock(Publication.class);
        when(_publicationDouble.identity())
                .thenReturn(new PublicationId(title, authorId, year));
        when(_publicationFactoryDouble.createPublication(
                title, authorId, year,
               _typeIdDouble, _genreIdDouble))
                .thenReturn(_publicationDouble);
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);
        repo.addPublication(title, authorId, year, _typeIdDouble, _genreIdDouble);

        // SUT + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.addPublication(title, authorId, year, _typeIdDouble, _genreIdDouble));
    }

    @Test
    void saveValidPublicationReturnsPublication() {
        // Arrange
        Publication _publicationDouble = mock(Publication.class);
        PublicationId _publicationIdDouble = mock(PublicationId.class);
        when(_publicationDouble.identity()).thenReturn(_publicationIdDouble);
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);

        // SUT
        Publication result = repo.save(_publicationDouble);

        // Assert
        assertSame(_publicationDouble, result);
    }

    @Test
    void ofIdentityExistingPublicationIdReturnsPublication() {
        // Arrange
        Publication _publicationDouble = mock(Publication.class);
        PublicationId _publicationIdDouble = mock(PublicationId.class);
        when(_publicationDouble.identity()).thenReturn(_publicationIdDouble);
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);
        repo.save(_publicationDouble);

        // SUT
        var result = repo.ofIdentity(_publicationIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertSame(_publicationDouble, result.get());
    }

    @Test
    void ofIdentityNonExistingPublicationIdReturnsEmpty() {
        // Arrange
        PublicationId _publicationIdDouble = mock(PublicationId.class);
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);

        // SUT
        var result = repo.ofIdentity(_publicationIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void containsOfIdentityExistingPublicationIdReturnsTrue() {
        // Arrange
        Publication _publicationDouble = mock(Publication.class);
        PublicationId _publicationIdDouble = mock(PublicationId.class);
        when(_publicationDouble.identity()).thenReturn(_publicationIdDouble);
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);
        repo.save(_publicationDouble);

        // SUT
        boolean result = repo.containsOfIdentity(_publicationIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityNonExistingPublicationIdReturnsFalse() {
        // Arrange
        PublicationId _publicationIdDouble = mock(PublicationId.class);
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);

        // SUT
        boolean result = repo.containsOfIdentity(_publicationIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void findAllReturnsTwoStoredPublications() {
        // Arrange
        Publication _publication1Double = mock(Publication.class);
        Publication _publication2Double = mock(Publication.class);
        PublicationId _publicationId1Double = mock(PublicationId.class);
        PublicationId _publicationId2Double = mock(PublicationId.class);
        when(_publication1Double.identity()).thenReturn(_publicationId1Double);
        when(_publication2Double.identity()).thenReturn(_publicationId2Double);
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);
        repo.save(_publication1Double);
        repo.save(_publication2Double);

        // SUT
        Iterable<Publication> result = repo.findAll();

        // Assert
        List<Publication> list = new java.util.ArrayList<>();
        result.forEach(list::add);
        assertEquals(2, list.size());
    }

    @Test
    void findAllEmptyRepoReturnsEmptyIterable() {
        // Arrange
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);

        // SUT
        Iterable<Publication> result = repo.findAll();

        // Assert
        assertFalse(result.iterator().hasNext());
    }

    @Test
    void getPublicationReturnsStoredPublication() {
        // Arrange
        Publication _publicationDouble = mock(Publication.class);
        PublicationId _publicationIdDouble = mock(PublicationId.class);
        when(_publicationDouble.identity()).thenReturn(_publicationIdDouble);
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);
        repo.save(_publicationDouble);

        // SUT
        Publication result = repo.getPublication(_publicationDouble);

        // Assert
        assertSame(_publicationDouble, result);
    }

    @Test
    void getPublicationThrowsWhenNotFound() {
        //Arrange
        Publication _publicationDouble = mock(Publication.class);

        //SUT
        MemoPublicationRepo memoPublicationRepo = new MemoPublicationRepo(_publicationFactoryDouble);

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                memoPublicationRepo.getPublication(_publicationDouble)
        );
    }

    @Test
    void getDifferentOfReturnsPublicationsNotInProvidedList() {
        // Arrange
        Publication _publication1Double = mock(Publication.class);
        Publication _publication2Double = mock(Publication.class);
        Publication _publication3Double = mock(Publication.class);
        PublicationId _publicationId1Double = mock(PublicationId.class);
        PublicationId _publicationId2Double = mock(PublicationId.class);
        PublicationId _publicationId3Double = mock(PublicationId.class);
        when(_publication1Double.identity()).thenReturn(_publicationId1Double);
        when(_publication2Double.identity()).thenReturn(_publicationId2Double);
        when(_publication3Double.identity()).thenReturn(_publicationId3Double);
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);
        repo.save(_publication1Double);
        repo.save(_publication2Double);
        repo.save(_publication3Double);
        List<Publication> existing = List.of(_publication1Double, _publication3Double);

        // SUT
        List<Publication> result = repo.getDifferentOf(existing);

        // Assert
        assertEquals(1, result.size());
        assertSame(_publication2Double, result.get(0));
    }

    @Test
    void getDifferentOfEmptyListReturnsAllPublications() {
        // Arrange
        Publication _publication1Double = mock(Publication.class);
        Publication _publication2Double = mock(Publication.class);
        PublicationId _publicationId1Double = mock(PublicationId.class);
        PublicationId _publicationId2Double = mock(PublicationId.class);
        when(_publication1Double.identity()).thenReturn(_publicationId1Double);
        when(_publication2Double.identity()).thenReturn(_publicationId2Double);
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);
        repo.save(_publication1Double);
        repo.save(_publication2Double);

        // SUT
        List<Publication> result = repo.getDifferentOf(List.of());

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getPublicationThrowsWhenNull() {
        //Arrange
        PublicationFactory _publicationFactoryDouble = mock(PublicationFactory.class);

        //SUT
        MemoPublicationRepo memoPublicationRepo = new MemoPublicationRepo(_publicationFactoryDouble);

        //Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                memoPublicationRepo.getPublication(null)
        );

        //Assert
        assertEquals("Publication not found", ex.getMessage());
    }

}