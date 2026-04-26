package MITELOVERS.persistence.mem;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemPublicationRepoTest {

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
        MemPublicationRepo repo = new MemPublicationRepo(_publicationFactoryDouble);
    }



    @Test
    void saveValidPublicationReturnsPublication() {
        // Arrange
        Publication publicationDouble = mock(Publication.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);
        when(publicationDouble.identity()).thenReturn(publicationIdDouble);

        // SUT
        MemPublicationRepo repo = new MemPublicationRepo(_publicationFactoryDouble);


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
        MemPublicationRepo repo = new MemPublicationRepo(_publicationFactoryDouble);

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
        MemPublicationRepo repo = new MemPublicationRepo(_publicationFactoryDouble);


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
        MemPublicationRepo repo = new MemPublicationRepo(_publicationFactoryDouble);
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
        MemPublicationRepo repo = new MemPublicationRepo(_publicationFactoryDouble);

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
        MemPublicationRepo repo = new MemPublicationRepo(_publicationFactoryDouble);
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
        MemPublicationRepo repo = new MemPublicationRepo(_publicationFactoryDouble);

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
        MemPublicationRepo repo = new MemPublicationRepo(_factoryDouble);
        repo.save(_pub1Double);
        repo.save(_pub2Double);

        List<PublicationId> result = repo.findAllKeys();

        // Assert
        assertEquals(2, result.size());
    }

}
