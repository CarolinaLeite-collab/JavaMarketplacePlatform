package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.persistence.jpa.datamodel.GenreDataModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class GenreAssemblerTest {

    @Mock
    private GenreFactory _genreFactoryDouble;

    @InjectMocks
    private GenreAssembler _sut;

    @Test
    void toDataModelShouldMapIdAndName() {
        // Arrange
        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);

        when(genreIdDouble.toString()).thenReturn("SCIENCE FICTION");
        when(genreDouble.identity()).thenReturn(genreIdDouble);
        when(genreDouble.getGenre()).thenReturn("Science Fiction");

        // Act
        GenreDataModel result = _sut.toDataModel(genreDouble);

        // Assert
        assertEquals("SCIENCE FICTION", result.getId());
        assertEquals("Science Fiction", result.getName());
        verify(genreDouble).identity();
        verify(genreDouble).getGenre();
    }

    @Test
    void toDomainShouldDelegateReconstructionToFactory() {
        // Arrange
        GenreDataModel dataModelDouble = mock(GenreDataModel.class);
        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);

        when(dataModelDouble.getId()).thenReturn("SCIENCE FICTION");
        when(dataModelDouble.getName()).thenReturn("Science Fiction");

        when(_genreFactoryDouble.createGenre(any(GenreId.class), eq("Science Fiction")))
                .thenReturn(genreDouble);

        when(genreDouble.identity()).thenReturn(genreIdDouble);
        when(genreIdDouble.toString()).thenReturn("SCIENCE FICTION");

        // Act
        Genre result = _sut.toDomain(dataModelDouble);

        // Assert
        assertNotNull(result);
        assertEquals(genreDouble, result);
        assertEquals("SCIENCE FICTION", result.identity().toString());
        verify(_genreFactoryDouble).createGenre(any(GenreId.class), eq("Science Fiction"));
    }

    @Test
    void toDataModelShouldThrowWhenGenreIsNull() {
        // Act // Assert
        assertThrows(IllegalArgumentException.class, () -> _sut.toDataModel(null));
    }

    @Test
    void toDomainShouldThrowWhenDataModelIsNull() {
        // Act // Assert
        assertThrows(IllegalArgumentException.class, () -> _sut.toDomain(null));
    }
}