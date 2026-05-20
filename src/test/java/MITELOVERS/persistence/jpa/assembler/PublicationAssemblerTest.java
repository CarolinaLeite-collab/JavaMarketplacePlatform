package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publication.PublicationFactory;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.persistence.jpa.datamodel.PublicationDataModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PublicationAssemblerTest {
    private PublicationFactory _publicationFactoryDouble;
    private Publication _publicationDouble;
    private Title _titleDouble;
    private AuthorId _authorIdDouble;
    private Year _releaseYearDouble;
    private GenreId _genreIdDouble;
    private PublicationId _publicationIdDouble;
    private PublicationDataModel _publicationDataModelDouble;

    @BeforeEach
    void setUp() {
        _publicationFactoryDouble = mock(PublicationFactory.class);
        _publicationDouble = mock(Publication.class);

        _titleDouble = mock(Title.class);
        _authorIdDouble = mock(AuthorId.class);
        _releaseYearDouble = mock(Year.class);
        _genreIdDouble = mock(GenreId.class);
        _publicationIdDouble = mock(PublicationId.class);

        _publicationDataModelDouble = mock(PublicationDataModel.class);
    }

    @Test
    void shouldReturnPublicationDataModelFromPublication() {
        //arrange
        when(_publicationDouble.identity()).thenReturn(_publicationIdDouble);
        when(_publicationDouble.getTitle()).thenReturn(_titleDouble);
        when(_publicationDouble.getAuthorId()).thenReturn(_authorIdDouble);
        when(_publicationDouble.getReleaseYear()).thenReturn(_releaseYearDouble);
        when(_publicationDouble.getGenreId()).thenReturn(_genreIdDouble);

        //SUT
        PublicationAssembler assembler = new PublicationAssembler(_publicationFactoryDouble);

        // act
        PublicationDataModel result = assembler.toDataModel(_publicationDouble);

        // assert
        assertEquals(_publicationIdDouble.toString(), result.getId());
        assertEquals(_titleDouble.toString(), result.getTitle());
        assertEquals(_authorIdDouble.toString(), result.getAuthorId());
        assertEquals(_releaseYearDouble.toString(), result.getReleaseYear());
        assertEquals(_genreIdDouble.toString(), result.getGenreId());
    }

    @Test
    void shouldReturnPublicationFromPublicationDataModel() {
        // arrange
        when(_authorIdDouble.toString()).thenReturn("Maria Tavares");
        when(_genreIdDouble.toString()).thenReturn("Romance");
        when(_publicationIdDouble.toString()).thenReturn("publicationIdDouble");
        when(_releaseYearDouble.toString()).thenReturn("2000");
        when(_titleDouble.toString()).thenReturn("titleDouble");

        when(_publicationDataModelDouble.getId()).thenReturn("publicationIdDouble");
        when(_publicationDataModelDouble.getAuthorId()).thenReturn("Maria Tavares");
        when(_publicationDataModelDouble.getReleaseYear()).thenReturn("2000");
        when(_publicationDataModelDouble.getGenreId()).thenReturn("Romance");
        when(_publicationDataModelDouble.getTitle()).thenReturn("titleDouble");


        when(_publicationDouble.getAuthorId()).thenReturn(_authorIdDouble);
        when(_publicationDouble.getGenreId()).thenReturn(_genreIdDouble);
        when(_publicationDouble.getTitle()).thenReturn(_titleDouble);
        when(_publicationDouble.getReleaseYear()).thenReturn(_releaseYearDouble);
        when(_publicationDouble.identity()).thenReturn(_publicationIdDouble);

        when(_publicationFactoryDouble.createPublication(any(PublicationId.class), any(Title.class), any(AuthorId.class), any(Year.class), any(GenreId.class))).thenReturn(_publicationDouble);

        //SUT
        PublicationAssembler assembler = new PublicationAssembler(_publicationFactoryDouble);

        // act
        Publication result = assembler.toDomain(_publicationDataModelDouble);

        // assert
        assertEquals(_publicationDouble, result);
        assertEquals("publicationIdDouble", result.identity().toString());
        assertEquals("Romance", result.getGenreId().toString());
        assertEquals("Maria Tavares", result.getAuthorId().toString());
        assertEquals("2000", result.getReleaseYear().toString());
        assertEquals("titleDouble", result.getTitle().toString());
    }

    @Test
    void shouldMaintainIdentityAfterRoundTrip() {

        // Arrange
        when(_publicationDataModelDouble.getId()).thenReturn("examplePublication");
        when(_publicationDataModelDouble.getAuthorId()).thenReturn("authorIdDouble");
        when(_publicationDataModelDouble.getReleaseYear()).thenReturn("2000");
        when(_publicationDataModelDouble.getGenreId()).thenReturn("genreIdDouble");
        when(_publicationDataModelDouble.getTitle()).thenReturn("titleDouble");

        when(_publicationDouble.identity()).thenReturn(_publicationIdDouble);
        when(_publicationDouble.getReleaseYear()).thenReturn(_releaseYearDouble);
        when(_publicationDouble.getGenreId()).thenReturn(_genreIdDouble);
        when(_publicationDouble.getTitle()).thenReturn(_titleDouble);
        when(_publicationDouble.getAuthorId()).thenReturn(_authorIdDouble);

        when(_publicationIdDouble.toString()).thenReturn("examplePublication");
        when(_authorIdDouble.toString()).thenReturn("authorIdDouble");
        when(_releaseYearDouble.toString()).thenReturn("2000");
        when(_genreIdDouble.toString()).thenReturn("genreIdDouble");
        when(_titleDouble.toString()).thenReturn("titleDouble");


        when(_publicationFactoryDouble.createPublication(any(PublicationId.class), any(Title.class), any(AuthorId.class), any(Year.class), any(GenreId.class))).thenReturn(_publicationDouble);

        // SUT
        PublicationAssembler assembler = new PublicationAssembler(_publicationFactoryDouble);

        // Act
        PublicationDataModel dm = assembler.toDataModel(_publicationDouble);
        Publication reconstructed = assembler.toDomain(_publicationDataModelDouble);

        // Assert
        assertEquals(_publicationDouble, reconstructed);
        assertEquals(_publicationIdDouble, reconstructed.identity());
    }
}