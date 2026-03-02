package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PublishingCompanyTest {

    @Test
    void testConstructor() {
        new PublishingCompany("Penguin Random House");
    }

    @Test
    void validPublisher() {
        PublishingCompany p = new PublishingCompany("Porto Editora");
        assertEquals("Porto Editora", p.getName());
    }

    @Test
    void publisherNameIsTrimmed() {
        PublishingCompany p1 = new PublishingCompany(" Porto Editora  ");
        assertEquals("Porto Editora", p1.getName());
    }

    @Test
    void emptyPublisher() {
        assertThrows(IllegalArgumentException.class, () -> {new PublishingCompany("  ");});
    }

    @Test
    void nullPublisher() {
        assertThrows(IllegalArgumentException.class, () -> {new PublishingCompany(null);});
    }

    @Test
    void publishersWithSameName() {
        PublishingCompany p2 = new PublishingCompany("Porto editora");
        PublishingCompany p3 = new PublishingCompany("PORTO EDITORA");

        assertEquals(p2, p3);
    }

    @Test
    void publishersWithSameNameHaveSameHashCode() {
        PublishingCompany p4 = new PublishingCompany("Porto editora");
        PublishingCompany p5 = new PublishingCompany("PORTO EDITORA");

        assertEquals(p4.hashCode(), p5.hashCode());
    }

    @Test
    void publisherEqualsItself() {
        PublishingCompany p6 = new PublishingCompany("Porto Editora");

        assertEquals(p6, p6);
    }

    @Test
    void publisherNotEqualToNull() {
        PublishingCompany p7 = new PublishingCompany("Porto Editora");

        assertNotEquals(p7, null);
    }

    @Test
    void publisherNotEqualToDifferentType() {
        PublishingCompany p8 = new PublishingCompany("Porto Editora");

        assertNotEquals(p8, "Porto Editora");
    }

    @Test
    void publisherWithDifferentNameAreNotEqual() {
        PublishingCompany p9 = new PublishingCompany("Porto Editora");
        PublishingCompany p10 = new PublishingCompany("Tinta da China");

        assertNotEquals(p9, p10);
    }

    @Test
    void publisherWithDifferentNameHaveDifferentHashCode() {
        PublishingCompany p11 = new PublishingCompany("Porto Editora");
        PublishingCompany p12 = new PublishingCompany("Tinta da China");

        assertNotEquals(p11.hashCode(), p12.hashCode());
    }

}
