package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartLineIdTest {

    @Test
    void testConstructorWithNoArguments() {

        //SUT
        new ShoppingCartLineId();

    }

    @Test
    void testConstructorWithOneArgument() {

        //SUT
        new ShoppingCartLineId("SCL-A1B2C3D4");

    }

    @Test
    void testConstructorWithInvalidArgumentShouldThrow() {

        //SUT + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ShoppingCartLineId("SCL-TOOMANYCHARACTERSHERE"));

    }

    @Test
    void testEqualsShouldReturnTrueIfOtherObjectIsSame() {

        //SUT
        ShoppingCartLineId id = new ShoppingCartLineId();

        //Act + Assert
        assertTrue(id.equals(id));

    }

    @Test
    void testEqualsShouldReturnFalseIfOtherObjectIsNull() {

        //SUT
        ShoppingCartLineId id = new ShoppingCartLineId();

        //Act + Assert
        assertFalse(id.equals(null));

    }

    @Test
    void testEqualsShouldReturnFalseIfObjectIfFromAnotherClass() {

        //Arrange
        String string = "test";

        //SUT
        ShoppingCartLineId id = new ShoppingCartLineId();

        //Act + Assert
        assertFalse(id.equals(string));

    }

    @Test
    void testEqualsShouldReturnTrueWhenIdsAreEquals() {

        //SUT
        ShoppingCartLineId id1 = new ShoppingCartLineId("SCL-A1B2C3D4");
        ShoppingCartLineId id2 = new ShoppingCartLineId("SCL-A1B2C3D4");

        //Act + Assert
        assertTrue(id1.equals(id2));

    }

    @Test
    void testEqualsShouldReturnFalseWhenIdsAreNotEquals() {

        //SUT
        ShoppingCartLineId id1 = new ShoppingCartLineId("SCL-A1B2C3D4");
        ShoppingCartLineId id2 = new ShoppingCartLineId("SCL-A1C2C3D4");

        //Act + Assert
        assertFalse(id1.equals(id2));

    }

    @Test
    void testHashCodeShouldBeSameWhenHashcodeIsSame() {

        //SUT
        ShoppingCartLineId id1 = new ShoppingCartLineId("SCL-A1B2C3D4");
        ShoppingCartLineId id2 = new ShoppingCartLineId("SCL-A1B2C3D4");

        //Act + Assert
        assertEquals(id1.hashCode(), id2.hashCode());

    }

    @Test
    void testHashCodeShouldBeDifferentWhenHashcodeIsDifferent() {

        //SUT
        ShoppingCartLineId id1 = new ShoppingCartLineId("SCL-A1B223D4");
        ShoppingCartLineId id2 = new ShoppingCartLineId("SCL-A1B2C3D4");

        //Act + Assert
        assertNotEquals(id1.hashCode(), id2.hashCode());

    }

    @Test
    void testToString() {

        //SUT
        ShoppingCartLineId id1 = new ShoppingCartLineId("SCL-A1B223D4");

        //Assert
        assertEquals(id1.toString(), "SCL-A1B223D4");

    }

}