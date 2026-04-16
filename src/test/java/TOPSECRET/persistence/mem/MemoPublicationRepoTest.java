package TOPSECRET.persistence.mem;

import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.publication.PublicationFactory;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.PublicationId;
import TOPSECRET.domain.valueobject.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoPublicationRepoTest {

    private PublicationFactory _publicationFactoryDouble;
    private Year _yearDouble;
    private Title _titleDouble;
    private AuthorId _authorIdDouble;
    private GenreId _genreIdDouble;

    @BeforeEach
    void setUp() {
        _publicationFactoryDouble = mock(PublicationFactory.class);
        _titleDouble = mock(Title.class);
        _authorIdDouble = mock(AuthorId.class);
        _yearDouble = mock(Year.class);
        _genreIdDouble = mock(GenreId.class);
    }

    @Test
    void constructorValidFactoryCreatesMemoPublicationRepo() {
        // SUT
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);
    }

    @Test
    void addPublicationStoresPublicationReturnedByFactory() {
        //Arrange
        Publication publicationDouble = mock(Publication.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);

        when(publicationDouble.identity()).thenReturn(publicationIdDouble);
        when(_publicationFactoryDouble.createPublication(_titleDouble, _authorIdDouble, _yearDouble,  _genreIdDouble)).thenReturn(publicationDouble);

        //SUT
        MemoPublicationRepo memoPublicationRepo = new MemoPublicationRepo(_publicationFactoryDouble);

        //Act
        Publication result = memoPublicationRepo.addPublication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble);

        //Assert
        assertSame(publicationDouble, result);
    }

    @Test
    void addPublicationThrowsWhenPublicationAlreadyExists() {
        // Arrange
        Publication publicationDouble = mock(Publication.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);

        when(publicationDouble.identity())
                .thenReturn(publicationIdDouble);
        when(_publicationFactoryDouble.createPublication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble))
                .thenReturn(publicationDouble);

        // SUT
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);
        repo.addPublication(_titleDouble,_authorIdDouble,_yearDouble, _genreIdDouble);

        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.addPublication(_titleDouble,_authorIdDouble,_yearDouble, _genreIdDouble));
    }

    @Test
    void saveValidPublicationReturnsPublication() {
        // Arrange
        Publication publicationDouble = mock(Publication.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);
        when(publicationDouble.identity()).thenReturn(publicationIdDouble);

        // SUT
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);


        Publication result = repo.save(publicationDouble);

        // Assert
        assertSame(publicationDouble, result);
    }

    @Test
    void ofIdentityExistingPublicationIdReturnsPublication() {
        // Arrange
        Publication publicationDouble = mock(Publication.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);
        when(publicationDouble.identity()).thenReturn(publicationIdDouble);

        // SUT
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);

        repo.save(publicationDouble);
        var result = repo.ofIdentity(publicationIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertSame(publicationDouble, result.get());
    }

    @Test
    void ofIdentityNonExistingPublicationIdReturnsEmpty() {
        // Arrange
        PublicationId publicationIdDouble = mock(PublicationId.class);

        // SUT
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);


        var result = repo.ofIdentity(publicationIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void containsOfIdentityExistingPublicationIdReturnsTrue() {
        // Arrange
        Publication publicationDouble = mock(Publication.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);
        when(publicationDouble.identity()).thenReturn(publicationIdDouble);

        // SUT
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);
        repo.save(publicationDouble);

        // Act
        boolean result = repo.containsOfIdentity(publicationIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityNonExistingPublicationIdReturnsFalse() {
        // Arrange
        PublicationId publicationIdDouble = mock(PublicationId.class);

        // SUT
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);

        // Act
        boolean result = repo.containsOfIdentity(publicationIdDouble);

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

        // SUT
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);
        repo.save(_publication1Double);
        repo.save(_publication2Double);

        // Act
        Iterable<Publication> result = repo.findAll();

        // Assert
        List<Publication> list = new java.util.ArrayList<>();
        result.forEach(list::add);
        assertEquals(2, list.size());
    }

    @Test
    void findAllEmptyRepoReturnsEmptyIterable() {
        // Arrange & SUT
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);

        // Act
        Iterable<Publication> result = repo.findAll();

        // Assert
        assertFalse(result.iterator().hasNext());
    }

    @Test
    void findAllKeysTwoPublicationsReturnsTwoKeys() {
        // Arrange
        PublicationFactory _factoryDouble = mock(PublicationFactory.class);
        Publication _pub1Double = mock(Publication.class);
        Publication _pub2Double = mock(Publication.class);
        PublicationId _id1Double = mock(PublicationId.class);
        PublicationId _id2Double = mock(PublicationId.class);

        when(_pub1Double.identity()).thenReturn(_id1Double);
        when(_pub2Double.identity()).thenReturn(_id2Double);

        // SUT
        MemoPublicationRepo repo = new MemoPublicationRepo(_factoryDouble);
        repo.save(_pub1Double);
        repo.save(_pub2Double);

        List<PublicationId> result = repo.findAllKeys();

        // Assert
        assertEquals(2, result.size());
    }



    @Test
    void getDifferentOfReturnsPublicationsNotInProvidedList() {
        // Arrange
        Publication publication1Double = mock(Publication.class);
        Publication publication2Double = mock(Publication.class);
        Publication publication3Double = mock(Publication.class);
        PublicationId publicationId1Double = mock(PublicationId.class);
        PublicationId publicationId2Double = mock(PublicationId.class);
        PublicationId publicationId3Double = mock(PublicationId.class);
        when(publication1Double.identity()).thenReturn(publicationId1Double);
        when(publication2Double.identity()).thenReturn(publicationId2Double);
        when(publication3Double.identity()).thenReturn(publicationId3Double);

        // SUT
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);
        repo.save(publication1Double);
        repo.save(publication2Double);
        repo.save(publication3Double);


        List<Publication> existing = List.of(publication1Double, publication3Double);

        // SUT
        List<Publication> result = repo.getDifferentOf(existing);

        // Assert
        assertEquals(1, result.size());
        assertSame(publication2Double, result.get(0));
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

        // SUT
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);
        repo.save(_publication1Double);
        repo.save(_publication2Double);

        List<Publication> result = repo.getDifferentOf(List.of());

        // Assert
        assertEquals(2, result.size());
    }

}