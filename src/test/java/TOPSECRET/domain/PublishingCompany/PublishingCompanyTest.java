package TOPSECRET.domain.PublishingCompany;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PublishingCompanyTest {

    @Test
    void testConstructor() {
        new PublishingCompany("Penguin Random House");
    }

    @Test
    void validPublisher() {
        //Arrange
        PublishingCompany p = new PublishingCompany("Porto Editora");

        //Act + Assert
        assertEquals("PORTO EDITORA", p.getName());
    }

    @Test
    void publisherNameIsTrimmed() {
        //Arrange
        PublishingCompany p1 = new PublishingCompany(" Porto Editora  ");

        //Act + Assert
        assertEquals("PORTO EDITORA", p1.getName());
    }

    @Test
    void emptyPublisher() {
        //Act + Assert
        assertThrows(IllegalArgumentException.class, () -> {new PublishingCompany("  ");});
    }

    @Test
    void nullPublisher() {
        //Act + Assert
        assertThrows(IllegalArgumentException.class, () -> {new PublishingCompany(null);});
    }

    @Test
    void publishersWithSameName() {
        //Act
        PublishingCompany p2 = new PublishingCompany("Porto editora");
        PublishingCompany p3 = new PublishingCompany("PORTO EDITORA");

        //Assert
        assertEquals(p2, p3);
    }

    @Test
    void publishersWithSameNameHaveSameHashCode() {
        //Act
        PublishingCompany p4 = new PublishingCompany("Porto editora");
        PublishingCompany p5 = new PublishingCompany("PORTO EDITORA");

        //Assert
        assertEquals(p4.hashCode(), p5.hashCode());
    }

    @Test
    void publisherEqualsItself() {
        //Act
        PublishingCompany p6 = new PublishingCompany("Porto Editora");

        //Assert
        assertEquals(p6, p6);
    }

    @Test
    void publisherNotEqualToNull() {
        //Act
        PublishingCompany p7 = new PublishingCompany("Porto Editora");

        //Assert
        assertNotEquals(p7, null);
    }

    @Test
    void publisherNotEqualToDifferentType() {
        //Act
        PublishingCompany p8 = new PublishingCompany("Porto Editora");

        //Assert
        assertNotEquals(p8, "Porto Editora");
    }

    @Test
    void publisherWithDifferentNameAreNotEqual() {
        //Act
        PublishingCompany p9 = new PublishingCompany("Porto Editora");
        PublishingCompany p10 = new PublishingCompany("Tinta da China");

        //Assert
        assertNotEquals(p9, p10);
    }

    @Test
    void publisherWithDifferentNameHaveDifferentHashCode() {
        //Act
        PublishingCompany p11 = new PublishingCompany("Porto Editora");
        PublishingCompany p12 = new PublishingCompany("Tinta da China");

        //Assert
        assertNotEquals(p11.hashCode(), p12.hashCode());
    }

    @Test
    void isSamePublishingCompanyShouldReturnTrueIfSame() {

        //Act
        PublishingCompany p13 = new PublishingCompany("Porto Editora");

        //Assert
        assertTrue(p13.isSamePublishingCompany("PORTO EDITORA"));

    }

    @Test
    void isSamePublishingCompanyShouldReturnFalseIfNotSame() {

        //Act
        PublishingCompany p14 = new PublishingCompany("Porto Editora");

        //Assert
        assertFalse(p14.isSamePublishingCompany("Pendant Publishing"));

    }

}
