package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.persistence.jpa.datamodel.GenreDataModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GenreAssemblerTest {

    @Test
    @Tag("unit")
    void toDataModelShouldMapIdAndName() {
        // Arrange
        GenreFactory _genreFactory = mock(GenreFactory.class);
        Genre _genre = mock(Genre.class);
        GenreId _genreId = new GenreId("Science Fiction");
        when(_genre.identity()).thenReturn(_genreId);
        when(_genre.getGenre()).thenReturn("Science Fiction");

        // SUT
        GenreAssembler _sut = new GenreAssembler(_genreFactory);

        // Act
        GenreDataModel result = _sut.toDataModel(_genre);

        // Assert
        assertEquals("SCIENCE FICTION", result.getId());
        assertEquals("Science Fiction", result.getName());
    }

    @Test
    @Tag("unit")
    void toDataModelShouldThrowWhenGenreIsNull() {
        // Arrange
        GenreFactory _genreFactory = mock(GenreFactory.class);
        GenreAssembler _sut;

        // SUT
        _sut = new GenreAssembler(_genreFactory);

        // Act
        Exception result = assertThrows(IllegalArgumentException.class, () -> _sut.toDataModel(null));

        // Assert
        assertNotNull(result);
    }

    @Test
    @Tag("unit")
    void toDomainShouldDelegateReconstructionToFactory() {
        // Arrange
        GenreFactory _genreFactory = mock(GenreFactory.class);
        GenreDataModel _dataModel = new GenreDataModel("SCIENCE FICTION", "Science Fiction");
        Genre _genre = mock(Genre.class);
        when(_genreFactory.createGenre(new GenreId("SCIENCE FICTION"), "Science Fiction")).thenReturn(_genre);

        // SUT
        GenreAssembler _sut = new GenreAssembler(_genreFactory);

        // Act
        Genre result = _sut.toDomain(_dataModel);

        // Assert
        assertSame(_genre, result);
        verify(_genreFactory).createGenre(new GenreId("SCIENCE FICTION"), "Science Fiction");
    }

    @Test
    @Tag("unit")
    void toDomainShouldThrowWhenDataModelIsNull() {
        // Arrange
        GenreFactory _genreFactory = mock(GenreFactory.class);
        GenreAssembler _sut;

        // SUT
        _sut = new GenreAssembler(_genreFactory);

        // Act
        Exception result = assertThrows(IllegalArgumentException.class, () -> _sut.toDomain(null));

        // Assert
        assertNotNull(result);
    }
}

