package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.persistence.jpa.datamodel.GenreDataModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GenreAssemblerTest {

    @Test
    @Tag("unit")
    void domain2DMShouldMapIdAndName() {
        // Arrange
        GenreFactory _genreFactory = mock(GenreFactory.class);
        GenreAssembler _sut = new GenreAssembler(_genreFactory);

        Genre _genre = mock(Genre.class);
        GenreId _genreId = new GenreId("Science Fiction");
        when(_genre.identity()).thenReturn(_genreId);
        when(_genre.getGenre()).thenReturn("Science Fiction");

        // Act
        GenreDataModel result = _sut.domain2DM(_genre);

        // Assert
        assertEquals("SCIENCE FICTION", result.getId());
        assertEquals("Science Fiction", result.getName());
    }

    @Test
    @Tag("unit")
    void domain2DMShouldThrowWhenGenreIsNull() {
        // Arrange
        GenreFactory _genreFactory = mock(GenreFactory.class);
        GenreAssembler _sut = new GenreAssembler(_genreFactory);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> _sut.domain2DM(null));
    }

    @Test
    @Tag("unit")
    void DM2DomainShouldDelegateReconstructionToFactory() {
        // Arrange
        GenreFactory _genreFactory = mock(GenreFactory.class);
        GenreAssembler _sut = new GenreAssembler(_genreFactory);

        GenreDataModel _dataModel = new GenreDataModel("SCIENCE FICTION", "Science Fiction");
        Genre _genre = mock(Genre.class);
        when(_genreFactory.createGenre("Science Fiction")).thenReturn(_genre);

        // Act
        Genre result = _sut.DM2Domain(_dataModel);

        // Assert
        assertSame(_genre, result);
        verify(_genreFactory).createGenre("Science Fiction");
    }

    @Test
    @Tag("unit")
    void DM2DomainShouldThrowWhenDataModelIsNull() {
        // Arrange
        GenreFactory _genreFactory = mock(GenreFactory.class);
        GenreAssembler _sut = new GenreAssembler(_genreFactory);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> _sut.DM2Domain(null));
    }
}

