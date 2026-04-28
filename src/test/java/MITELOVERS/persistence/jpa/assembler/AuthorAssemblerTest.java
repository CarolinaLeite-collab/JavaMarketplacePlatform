package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.persistence.jpa.datamodel.AuthorDataModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class AuthorAssemblerTest {

    @Test
    void shouldConvertAuthorToDataModel() {

        // Arrange
        AuthorFactory authorFactoryDouble = mock(AuthorFactory.class);
        Author authorDouble = mock(Author.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(authorDouble.getName()).thenReturn("Eça de Queirós");
        when(authorDouble.identity()).thenReturn(authorIdDouble);
        when(authorIdDouble.toString()).thenReturn("1");

        // SUT
        AuthorAssembler assembler = new AuthorAssembler(authorFactoryDouble);

        // Act
        AuthorDataModel dataModel = assembler.toDataModel(authorDouble);

        // Assert
        assertEquals("1", dataModel.getId());
        assertEquals("Eça de Queirós", dataModel.getName());
    }

    @Test
    void shouldThrowExceptionWhenAuthorIsNull() {

        // Arrange
        AuthorFactory authorFactoryDouble = mock(AuthorFactory.class);

        // SUT
        AuthorAssembler assembler = new AuthorAssembler(authorFactoryDouble);


        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDataModel(null));
    }

    @Test
    void shouldConvertDataModelToAuthor() {

        // Arrange
        AuthorFactory authorFactoryDouble = mock(AuthorFactory.class);
        AuthorDataModel dataModelDouble = mock(AuthorDataModel.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Author authorDouble = mock(Author.class);

        when(dataModelDouble.getId()).thenReturn("Lev Nikoláievitch Tolstói");
        when(dataModelDouble.getName()).thenReturn("Eça de Queirós");

        when(authorFactoryDouble.createAuthor(any(AuthorId.class), eq("Eça de Queirós")))
                .thenReturn(authorDouble);

        when(authorDouble.getName()).thenReturn("Eça de Queirós");
        when(authorDouble.identity()).thenReturn(authorIdDouble);
        when(authorIdDouble.toString()).thenReturn("1");

        // SUT
        AuthorAssembler assembler = new AuthorAssembler(authorFactoryDouble);

        // Act
        Author author = assembler.toDomain(dataModelDouble);

        // Assert
        assertEquals("Eça de Queirós", author.getName());
        assertEquals("1", author.identity().toString());
    }

    @Test
    void shouldThrowExceptionWhenDataModelIsNull() {

        // Arrange
        AuthorFactory authorFactoryDouble = mock(AuthorFactory.class);

        // SUT
        AuthorAssembler assembler = new AuthorAssembler(authorFactoryDouble);

        // Assert
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDomain(null));
    }

    @Test
    void shouldUseFactoryWhenConvertingToDomain() {

        // Arrange
        AuthorFactory authorFactoryDouble = mock(AuthorFactory.class);
        AuthorDataModel dataModelDouble = mock(AuthorDataModel.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Author authorDouble = mock(Author.class);

        when(dataModelDouble.getId()).thenReturn("1");
        when(dataModelDouble.getName()).thenReturn("Eça de Queirós");

        when(authorFactoryDouble.createAuthor(any(AuthorId.class), eq("Eça de Queirós")))
                .thenReturn(authorDouble);

        when(authorDouble.getName()).thenReturn("Eça de Queirós");
        when(authorDouble.identity()).thenReturn(authorIdDouble);
        when(authorIdDouble.toString()).thenReturn("1");

        // SUT
        AuthorAssembler assembler = new AuthorAssembler(authorFactoryDouble);

        // Act
        Author author = assembler.toDomain(dataModelDouble);

        // Assert
        assertNotNull(author);
        assertEquals("Eça de Queirós", author.getName());
        assertEquals("1", author.identity().toString());
    }
}