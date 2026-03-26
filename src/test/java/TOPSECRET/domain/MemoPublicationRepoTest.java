package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoPublicationRepoTest {

    private MemoPublicationRepo _memoPublicationRepo;
    private PublicationFactory _publicationFactoryDouble;

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
        _publicationFactoryDouble = mock(PublicationFactory.class);
        _memoPublicationRepo = new MemoPublicationRepo(_publicationFactoryDouble); //SUT

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
    void addPublicationStoresPublicationReturnedByFactory() {
        //Arrange
        Publication created = mock(Publication.class);

        when(_publicationFactoryDouble.createPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble))
                .thenReturn(created);

        //Act
        Publication result = _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble);

        //Assert
        assertSame(created, result);
    }

    @Test
    void addPublicationThrowsWhenPublicationAlreadyExists() {
        //Arrange
        Publication _publicationDouble = mock(Publication.class);

        when(_publicationFactoryDouble.createPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble))
                .thenReturn(_publicationDouble);

        _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble);

        //Act + Assert
        // second call returns same instance → duplicate
        assertThrows(IllegalArgumentException.class, () ->
                _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble)
        );
    }

    @Test
    void getPublicationReturnsStoredPublication() {
        //Arrange
        Publication _publicationDouble = mock(Publication.class);
        Publication _publicationDouble2 = mock(Publication.class);

        when(_publicationFactoryDouble.createPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble))
                .thenReturn(_publicationDouble)
                .thenReturn(_publicationDouble2);


        _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble);
        _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble);

        //Act
        Publication result = _publicationRepo.getPublication(_publicationDouble2);

        //Assert
        assertSame(_publicationDouble2, result);
    }

    @Test
    void getPublicationThrowsWhenNotFound() {
        //Arrange
        Publication _publicationDouble = mock(Publication.class);

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                _publicationRepo.getPublication(_publicationDouble)
        );
    }

    @Test
    void getDifferentOfReturnsPublicationsNotInProvidedList() {
        //Arrange
        Publication _publicationDouble1 = mock(Publication.class);
        Publication _publicationDouble2 = mock(Publication.class);
        Publication _publicationDouble3 = mock(Publication.class);

        when(_publicationFactoryDouble.createPublication(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(_publicationDouble1)
                .thenReturn(_publicationDouble2)
                .thenReturn(_publicationDouble3);

        _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble);
        _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble);
        _publicationRepo.addPublication(_typeDouble, _identifierDouble, _yearDouble, _titleDouble, _authorDouble, _publisherDouble, _editionDouble, _genreDouble);

        List<Publication> existing = List.of(_publicationDouble1, _publicationDouble3);

        //Act
        List<Publication> result = _publicationRepo.getDifferentOf(existing);

        //Assert
        assertEquals(1, result.size());
        assertSame(_publicationDouble2, result.get(0));
    }

    @Test
    void getPublicationThrowsWhenNull() {
        //Arrange
        PublicationFactory _publicationFactoryDouble = mock(PublicationFactory.class);
        MemoPublicationRepo repo = new MemoPublicationRepo(_publicationFactoryDouble);

        //Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                repo.getPublication(null)
        );

        //Assert
        assertEquals("Publication not found", ex.getMessage());
    }
}