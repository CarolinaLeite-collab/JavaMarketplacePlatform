package MITELOVERS.domain.author;

import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.Name;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AuthorTest {

    @Test
    void testConstructor() {

        // Arrange
        Name nameDouble = mock(Name.class);

        // Act & SUT
        new Author(nameDouble);

    }

    @Test
    void testConstructorWithId() {

        // Arrange
        AuthorId authorId = mock(AuthorId.class);
        Name nameDouble = mock(Name.class);

        // Act & SUT
        new Author(authorId, nameDouble);

    }

    @Test
    void validNameAuthor() {

        // Arrange
        Name nameDouble = mock(Name.class);

        // Act & SUT
        Author a = new Author(nameDouble);

        // Assert
        assertEquals(nameDouble, a.getName());

    }

    @Test
    void validNameAuthorWithId() {

        // Arrange
        AuthorId authorId = mock(AuthorId.class);
        Name nameDouble = mock(Name.class);

        // Act & SUT
        Author a = new Author(authorId, nameDouble);

        // Assert
        assertEquals(nameDouble, a.getName());
        assertNotNull(a.identity());

    }

    @Test
    void testEqualsWithSameObject() {

        // Arrange
        Name nameDouble = mock(Name.class);

        // Arrange & SUT
        Author a = new Author(nameDouble);

        // Act & Assert
        assertTrue(a.equals(a));

    }

    @Test
    void equalsShouldReturnFalseWhenNull() {

        // Arrange
        Name nameDouble = mock(Name.class);

        // Arrange & SUT
        Author a = new Author(nameDouble);

        // Act & Assert
        assertFalse(a.equals(null));
    }

    @Test
    void equalsShouldReturnFalseWhenDifferentType() {

        // Arrange
        Name nameDouble = mock(Name.class);

        // Arrange & SUT
        Author a = new Author(nameDouble);

        // Act & Assert
        assertFalse(a.equals("Name"));
    }

    @Test
    void equalsShouldReturnTrueForSameAuthorId() {

        // Arrange
        AuthorId id = mock(AuthorId.class);
        Name nameDouble = mock(Name.class);

        // Arrange & SUT
        Author a1 = new Author(id, nameDouble);
        Author a2 = new Author(id, nameDouble);

        // Act & Assert
        assertTrue(a1.equals(a2));
    }

    @Test
    void equalsShouldReturnFalseForDifferentAuthorIds() {

        // Arrange
        AuthorId id1 = mock(AuthorId.class);
        AuthorId id2 = mock(AuthorId.class);
        Name nameDouble = mock(Name.class);

        // Arrange & SUT
        Author a1 = new Author(id1, nameDouble);
        Author a2 = new Author(id2, nameDouble);

        // Act & Assert
        assertFalse(a1.equals(a2));
    }

    @Test
    void testEqualHashCodeWhenSameAuthorId() {

        // Arrange
        AuthorId id = mock(AuthorId.class);
        Name nameDouble = mock(Name.class);

        // Arrange & SUT
        Author a = new Author(id, nameDouble);
        Author a2 = new Author(id, nameDouble);

        // Act & Assert
        assertEquals(a.hashCode(), a2.hashCode());

    }

    @Test
    void testNonEqualHashCodeWhenDifferentAuthorId() {

        // Arrange
        AuthorId id1 = mock(AuthorId.class);
        AuthorId id2 = mock(AuthorId.class);
        Name nameDouble = mock(Name.class);

        // Arrange & SUT
        Author a = new Author(id1, nameDouble);
        Author a2 = new Author(id2, nameDouble);

        // Act & Assert
        assertNotEquals(a.hashCode(), a2.hashCode());

    }

    @Test
    void identityShouldReturnAuthorId() {
        
        // Arrange
        Name nameDouble = mock(Name.class);

        // Arrange & SUT
        Author author = new Author(nameDouble);

        // Act
        AuthorId id = author.identity();

        // Assert
        assertNotNull(id);

    }

    @Test
    void identityShouldBeNotEqualForSameName() {
        
        // Arrange
        Name nameDouble = mock(Name.class);

        // Arrange & SUT
        Author a1 = new Author(nameDouble);
        Author a2 = new Author(nameDouble);

        // Act & Assert
        assertNotEquals(a1.identity(), a2.identity());

    }

    @Test
    void identityShouldBeDifferentForDifferentNames() {
        
        // Arrange
        Name nameDouble = mock(Name.class);

        // Arrange & SUT
        Author a1 = new Author(nameDouble);
        Author a2 = new Author(nameDouble);

        // Act & Assert
        assertNotEquals(a1.identity(), a2.identity());

    }

    @Test
    void sameAsShouldReturnFalseForDifferentNames() {

        // Arrange
        Name nameDouble1 = mock(Name.class);
        when(nameDouble1.toString()).thenReturn("Seneca");

        Name nameDouble2 = mock(Name.class);
        when(nameDouble2.toString()).thenReturn("Justinian");

        // Arrange & SUT
        Author a1 = new Author(nameDouble1);
        Author a2 = new Author(nameDouble2);

        // Act
        boolean result = a1.sameAs(a2);

        // Assert
        assertFalse(result);

    }

    @Test
    void sameAsShouldReturnFalseWhenNull() {

        // Arrange
        Name nameDouble = mock(Name.class);

        // Arrange & SUT
        Author a = new Author(nameDouble);

        // Act
        boolean result = a.sameAs(null);

        // Assert
        assertFalse(result);

    }

    @Test
    void sameAsShouldReturnFalseForDifferentType() {

        // Arrange
        Name nameDouble = mock(Name.class);
        when(nameDouble.toString()).thenReturn("Seneca");

        // Arrange & SUT
        Author a = new Author(nameDouble);

        // Act
        boolean result = a.sameAs(nameDouble);

        // Assert
        assertFalse(result);

    }

    @Test
    void sameAsShouldReturnTrueForSameObject() {

        // Arrange
        Name nameDouble = mock(Name.class);

        // Arrange & SUT
        Author a = new Author(nameDouble);

        // Act
        boolean result = a.sameAs(a);

        // Assert
        assertTrue(result);

    }
}
