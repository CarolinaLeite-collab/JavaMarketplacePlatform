package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


    class NoIsbnBookTest {

        @Test
        public void shouldCreateValidInstance() {
            //Arrange
            String id = "123456";

            //Act
            //SUT
            NoIsbnBook bookInternalId = new NoIsbnBook(id);

            //Assert
            assertEquals(id, bookInternalId.getIdentifier());
        }

        @Test
        void shouldThrowExceptionWhenIdIsNull() {
            //Act
            //SUT
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new NoIsbnBook(null)
            );

            // Assert
            assertEquals("Internal id cannot be null", exception.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenIdIsBlank() {
            //Arrange
            String id = " ";

            //Act
            //SUT
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new NoIsbnBook(id)
            );

            // Assert
            assertEquals("Internal id cannot be null", exception.getMessage());
        }

        @Test
        void shouldGenerateNonNullId() {
            //Act
            //SUT
            NoIsbnBook generatedId = NoIsbnBook.generate();

            //Assert
            assertNotNull(generatedId);
        }

        @Test
        void generatedIdsShouldBeDifferent() {
            //Arrange
            //SUT
            NoIsbnBook generatedId1 = NoIsbnBook.generate();
            NoIsbnBook generatedId2 = NoIsbnBook.generate();

            //Assert
            assertNotEquals(generatedId1, generatedId2);
        }

        @Test
        void shouldBeEqualWhenSameInternalId() {
            //Arrange
            String id = "123456";

            //Act
            //SUT
            NoIsbnBook bookInternalId1 = new NoIsbnBook(id);
            NoIsbnBook bookInternalId2 = new NoIsbnBook(id);

            //Assert
            assertEquals(bookInternalId1, bookInternalId2);
        }

        @Test
        void shouldNotBeEqualWhenDifferentInternalId() {
            //Arrange
            String id1 = "123456";
            String id2 = "12345";

            //Act
            //SUT
            NoIsbnBook bookInternalId1 = new NoIsbnBook(id1);
            NoIsbnBook bookInternalId2 = new NoIsbnBook(id2);

            //Assert
            assertNotEquals(bookInternalId1, bookInternalId2);
        }

        @Test
        void equalsShouldReturnFalseWhenComparedWithNull() {
            //Arrange
            String id = "12345";
            NoIsbnBook bookInternalId = new NoIsbnBook(id);

            //Act
            //SUT
            boolean result = bookInternalId.equals(null);

            //Assert
            assertFalse(result);
        }

        @Test
        void equalsShouldReturnFalseWhenComparedWithDifferentType() {
            //Arrange
            String id = "12345";
            NoIsbnBook bookInternalId = new NoIsbnBook(id);

            //Act
            //SUT
            boolean result = bookInternalId.equals("12345");

            //Assert
            assertFalse(result);
        }

        @Test
        void shouldBeEqualToItself() {
            // Arrange
            //SUT
            NoIsbnBook id = new NoIsbnBook("12345");

            //Assert
            assertEquals(id, id);
        }

        @Test
        void shouldBeImmutable() {
            //Arrange
            String id = "123456";

            //Act
            //SUT
            NoIsbnBook bookInternalId = new NoIsbnBook(id);

            //Assert
            assertEquals("123456", bookInternalId.getIdentifier());
        }
    }

