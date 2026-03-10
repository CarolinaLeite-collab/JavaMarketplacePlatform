package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationRepoTest {

    private PublicationRepo _publicationRepo;
    private PublicationFactory _publicationFactory;

    // Mocks for factory arguments
    private PublicationType _typeDouble;
    private Identifier _identifierDouble;
    private Year _yearDouble;
    private Title _titleDouble;
    private Author _authorDouble;
    private PublishingCompany _publisherDouble;
    private Edition _editionDouble;
    private Genre _genreDouble;

    @BeforeEach
    void setUp() {
        _publicationFactory = mock(PublicationFactory.class);
        _publicationRepo = new PublicationRepo(_publicationFactory);

        _typeDouble = mock(PublicationType.class);
        _identifierDouble = mock(Identifier.class);
        _yearDouble = mock(Year.class);
        _titleDouble = mock(Title.class);
        _authorDouble = mock(Author.class);
        _publisherDouble = mock(PublishingCompany.class);
        _editionDouble = mock(Edition.class);
        _genreDouble = mock(Genre.class);
    }

    @Test
    void addPublication_storesPublicationReturnedByFactory() {
        Publication created = mock(Publication.class);

        when(_publicationFactory.createPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble))
                .thenReturn(created);

        Publication result = _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble);

        assertSame(created, result);
    }

    @Test
    void addPublication_throwsWhenPublicationAlreadyExists() {
        Publication created = mock(Publication.class);

        when(_publicationFactory.createPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble))
                .thenReturn(created);

        _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble);

        // second call returns same instance → duplicate
        assertThrows(IllegalArgumentException.class, () ->
                _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble)
        );
    }

    @Test
    void getPublication_returnsStoredPublication() {
        Publication created = mock(Publication.class);

        when(_publicationFactory.createPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble))
                .thenReturn(created);

        _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble);

        Publication result = _publicationRepo.getPublication(created);

        assertSame(created, result);
    }

    @Test
    void getPublication_throwsWhenNotFound() {
        Publication notStored = mock(Publication.class);

        assertThrows(IllegalArgumentException.class, () ->
                _publicationRepo.getPublication(notStored)
        );
    }

    @Test
    void getDifferentOf_returnsPublicationsNotInProvidedList() {
        Publication p1 = mock(Publication.class);
        Publication p2 = mock(Publication.class);
        Publication p3 = mock(Publication.class);

        when(_publicationFactory.createPublication(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(p1)
                .thenReturn(p2)
                .thenReturn(p3);

        _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble);
        _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble);
        _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble);

        List<Publication> existing = List.of(p1, p3);

        List<Publication> result = _publicationRepo.getDifferentOf(existing);

        assertEquals(1, result.size());
        assertSame(p2, result.get(0));
    }

    @Test
    void getPublication_throwsWhenNull() {
        PublicationFactory factory = mock(PublicationFactory.class);
        PublicationRepo repo = new PublicationRepo(factory);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                repo.getPublication(null)
        );

        assertEquals("Publication not found", ex.getMessage());
    }
}