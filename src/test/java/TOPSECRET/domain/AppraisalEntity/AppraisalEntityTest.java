package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.valueobject.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppraisalEntityTest {

    private Name _nameDouble;
    private PublicationType _publicationType1Double;
    private PublicationType _publicationType2Double;
    private Genre _genreDouble1;
    private Genre _genreDouble2;

    @BeforeEach
    void setup() {
        _nameDouble = mock(Name.class);
        _publicationType1Double = mock(PublicationType.class);
        _publicationType2Double = mock(PublicationType.class);
        _genreDouble1 = mock(Genre.class);
        _genreDouble2 = mock(Genre.class);

        when(_nameDouble.toString()).thenReturn("Name");
        when(_publicationType1Double.getPublicationType()).thenReturn("Book");
        when(_publicationType2Double.getPublicationType()).thenReturn("Magazine");
        when(_genreDouble1.getGenre()).thenReturn("Science Fiction");
        when(_genreDouble2.getGenre()).thenReturn("Fantasy");
    }

    @Test
    void should_create_AppraisalEntity_successfully() {
        // Arrange
        List<PublicationType> publicationTypes = Arrays.asList(_publicationType1Double, _publicationType2Double);
        List<Genre> genres = Arrays.asList(_genreDouble1, _genreDouble2);

        // SUT
        AppraisalEntity entity = new AppraisalEntity(_nameDouble, publicationTypes, genres);

        // Act & Assert
        assertNotNull(entity);
    }

    @Test
    void should_return_AppraisalEntity_data_successfully() {
        // Arrange
        List<PublicationType> _publicationTypes = new ArrayList<>();
        _publicationTypes.add(_publicationType1Double);
        _publicationTypes.add(_publicationType2Double);

        List<Genre> _genres = new ArrayList<>();
        _genres.add(_genreDouble1);
        _genres.add(_genreDouble2);

        //SUT
        AppraisalEntity entity = new AppraisalEntity(_nameDouble, _publicationTypes, _genres);

        // Act
        Name _entityName = entity.getName();
        List<PublicationType> _entityPublicationTypes = entity.getPublicationTypes();
        List<Genre> _entityGenres = entity.getGenres();

        // Assert
        assertEquals(_nameDouble, _entityName);
        assertEquals(_publicationTypes, _entityPublicationTypes);
        assertEquals(_genres, _entityGenres);
    }

}
