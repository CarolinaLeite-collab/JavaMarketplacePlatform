package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PictureTest {

    @Test
    public void constructorCreatesValidPicture() {

        // Act
        Picture picture = new Picture("https://example.com/image.jpg");

        // Assert
        assertEquals("https://example.com/image.jpg", picture.toString());
    }

    @Test
    public void constructorThrowsExceptionWhenNull() {

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Picture(null));
        assertEquals("CoverUrl cannot be empty", exception.getMessage());
    }

    @Test
    public void constructorThrowsExceptionWhenEmpty() {

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Picture(""));
        assertEquals("CoverUrl cannot be empty", exception.getMessage());
    }

    @Test
    public void constructorThrowsExceptionWhenBlank() {

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Picture("   "));
        assertEquals("CoverUrl cannot be empty", exception.getMessage());
    }

    @Test
    public void getValueReturnsCorrectValue() {

        // Arrange
        String url = "https://example.com/image.jpg";

        // Act
        Picture picture = new Picture(url);

        // Assert
        assertEquals(url, picture.getValue());
    }

    @Test
    public void toStringReturnsValue() {

        // Arrange
        String url = "https://example.com/image.jpg";

        // Act
        Picture picture = new Picture(url);

        // Assert
        assertEquals(url, picture.toString());
    }

    @Test
    public void equalsShouldReturnTrueForSameObject() {

        // Arrange
        Picture picture = new Picture("https://example.com/image.jpg");

        // Act
        boolean result = picture.equals(picture);

        // Assert
        assertTrue(result);
    }

    @Test
    public void equalsShouldReturnTrueForPicturesWithSameValue() {

        // Arrange
        String url = "https://example.com/image.jpg";
        Picture picture1 = new Picture(url);
        Picture picture2 = new Picture(url);

        // Act
        boolean result = picture1.equals(picture2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void equalsShouldReturnFalseForPicturesWithDifferentValues() {

        // Arrange
        Picture picture1 = new Picture("https://example.com/image1.jpg");
        Picture picture2 = new Picture("https://example.com/image2.jpg");

        // Act
        boolean result = picture1.equals(picture2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void equalsShouldReturnFalseForDifferentType() {

        // Arrange
        Picture picture = new Picture("https://example.com/image.jpg");

        // Act
        boolean result = picture.equals("https://example.com/image.jpg");

        // Assert
        assertFalse(result);
    }

    @Test
    public void equalsShouldReturnFalseForNull() {

        // Arrange
        Picture picture = new Picture("https://example.com/image.jpg");

        // Act
        boolean result = picture.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    public void hashCodeSameObjectShouldReturnSameHashCode() {

        // Arrange
        String url = "https://example.com/image.jpg";
        Picture picture = new Picture(url);

        // Act + Assert
        assertEquals(url.hashCode(), picture.hashCode());
    }





}