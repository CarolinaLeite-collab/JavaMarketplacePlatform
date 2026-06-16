package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartIdTest {

    @Test
    void testConstructorWithNoArguments() {

        //SUT
        new ShoppingCartId();

    }

    @Test
    void testConstructorWithOneArgument() {

        //SUT
        new ShoppingCartId("SC-A1B2C3D4");

    }

    @Test
    void testConstructorWithInvalidArgumentShouldThrow() {

        //SUT + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ShoppingCartId("SC-TOOMANYCHARACTERSHERE"));

    }

    @Test
    void testEqualsShouldReturnTrueIfOtherObjectIsSame() {

        //SUT
        ShoppingCartId id = new ShoppingCartId();

        //Act + Assert
        assertTrue(id.equals(id));

    }

    @Test
    void testEqualsShouldReturnFalseIfOtherObjectIsNull() {

        //SUT
        ShoppingCartId id = new ShoppingCartId();

        //Act + Assert
        assertFalse(id.equals(null));

    }

    @Test
    void testEqualsShouldReturnFalseIfObjectIfFromAnotherClass() {

        //Arrange
        String string = "test";

        //SUT
        ShoppingCartId id = new ShoppingCartId();

        //Act + Assert
        assertFalse(id.equals(string));

    }

    @Test
    void testEqualsShouldReturnTrueWhenIdsAreEquals() {

        //SUT
        ShoppingCartId id1 = new ShoppingCartId("SC-A1B2C3D4");
        ShoppingCartId id2 = new ShoppingCartId("SC-A1B2C3D4");

        //Act + Assert
        assertTrue(id1.equals(id2));

    }

    @Test
    void testEqualsShouldReturnFalseWhenIdsAreNotEquals() {

        //SUT
        ShoppingCartId id1 = new ShoppingCartId("SC-A1B2C3D4");
        ShoppingCartId id2 = new ShoppingCartId("SC-A1C2C3D4");

        //Act + Assert
        assertFalse(id1.equals(id2));

    }

    @Test
    void testHashCodeShouldBeSameWhenHashcodeIsSame() {

        //SUT
        ShoppingCartId id1 = new ShoppingCartId("SC-A1B2C3D4");
        ShoppingCartId id2 = new ShoppingCartId("SC-A1B2C3D4");

        //Act + Assert
        assertEquals(id1.hashCode(), id2.hashCode());

    }

    @Test
    void testHashCodeShouldBeDifferentWhenHashcodeIsDifferent() {

        //SUT
        ShoppingCartId id1 = new ShoppingCartId("SC-A1B223D4");
        ShoppingCartId id2 = new ShoppingCartId("SC-A1B2C3D4");

        //Act + Assert
        assertNotEquals(id1.hashCode(), id2.hashCode());

    }

    @Test
    void testToString() {

        //SUT
        ShoppingCartId id1 = new ShoppingCartId("SC-A1B223D4");

        //Assert
        assertEquals(id1.toString(), "SC-A1B223D4");

    }


}