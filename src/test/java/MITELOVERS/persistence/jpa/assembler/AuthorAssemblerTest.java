package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.persistence.jpa.datamodel.AuthorDataModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

class AuthorAssemblerTest {

    @Test
    void shouldConvertAuthorToDataModel() {

        // Arrange
        AuthorFactory authorFactoryDouble = mock(AuthorFactory.class);
        Name nameDouble = mock(Name.class);
        Author authorDouble = mock(Author.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(nameDouble.toString()).thenReturn("Eça de Queirós");
        when(authorDouble.getName()).thenReturn(nameDouble);
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
    void shouldConvertDataModelToAuthor() {

        // Arrange
        AuthorFactory authorFactoryDouble = mock(AuthorFactory.class);
        AuthorDataModel dataModelDouble = mock(AuthorDataModel.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Name nameDouble = mock(Name.class);
        Author authorDouble = mock(Author.class);

        when(dataModelDouble.getId()).thenReturn("Lev Nikoláievitch Tolstói");
        when(dataModelDouble.getName()).thenReturn("Eça de Queirós");

        when(authorFactoryDouble.createAuthor(any(AuthorId.class), any(Name.class)))
                .thenReturn(authorDouble);

        when(authorDouble.getName()).thenReturn(nameDouble);
        when(authorDouble.identity()).thenReturn(authorIdDouble);
        when(authorIdDouble.toString()).thenReturn("1");

        // SUT
        AuthorAssembler assembler = new AuthorAssembler(authorFactoryDouble);

        // Act
        Author author = assembler.toDomain(dataModelDouble);

        // Assert
        assertEquals(nameDouble, author.getName());
        assertEquals("1", author.identity().toString());
    }

    @Test
    void shouldUseFactoryWhenConvertingToDomain() {

        // Arrange
        AuthorFactory authorFactoryDouble = mock(AuthorFactory.class);
        AuthorDataModel dataModelDouble = mock(AuthorDataModel.class);
        Name nameDouble = mock(Name.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Author authorDouble = mock(Author.class);

        when(dataModelDouble.getId()).thenReturn("1");
        when(dataModelDouble.getName()).thenReturn("Eça de Queirós");

        when(authorFactoryDouble.createAuthor(any(AuthorId.class), any(Name.class)))
                .thenReturn(authorDouble);

        when(authorDouble.getName()).thenReturn(nameDouble);
        when(authorDouble.identity()).thenReturn(authorIdDouble);
        when(authorIdDouble.toString()).thenReturn("1");

        // SUT
        AuthorAssembler assembler = new AuthorAssembler(authorFactoryDouble);

        // Act
        Author author = assembler.toDomain(dataModelDouble);

        // Assert
        assertNotNull(author);
        assertEquals(nameDouble, author.getName());
        assertEquals("1", author.identity().toString());
    }
}