package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PublisherTest {
    @Test
    void validPublisher() {
        Publisher p = new Publisher("Porto Editora");
        assertEquals("Porto Editora", p.getName());
    }

    @Test
    void publisherNameIsTrimmed() {
        Publisher p1 = new Publisher (" Porto Editora  ");
        assertEquals("Porto Editora", p1.getName());
    }

    @Test
    void emptyPublisher() {
        assertThrows(IllegalArgumentException.class, () -> {new Publisher("  ");});
    }

    @Test
    void nullPublisher() {
        assertThrows(IllegalArgumentException.class, () -> {new Publisher(null);});
    }

    @Test
    void publishersWithSameName() {
        Publisher p2 = new Publisher("Porto editora");
        Publisher p3 = new Publisher("PORTO EDITORA");

        assertEquals(p2, p3);
    }

    @Test
    void publishersWithSameNameHaveSameHashCode() {
        Publisher p4 = new Publisher("Porto editora");
        Publisher p5 = new Publisher("PORTO EDITORA");

        assertEquals(p4.hashCode(), p5.hashCode());
    }

    @Test
    void publisherEqualsItself() {
        Publisher p6 = new Publisher("Porto Editora");

        assertEquals(p6, p6);
    }

    @Test
    void publisherNotEqualToNull() {
        Publisher p7 = new Publisher("Porto Editora");

        assertNotEquals(p7, null);
    }

    @Test
    void publisherNotEqualToDifferentType() {
        Publisher p8 = new Publisher("Porto Editora");

        assertNotEquals(p8, "Porto Editora");
    }

    @Test
    void publisherWithDifferentNameAreNotEqual() {
        Publisher p9 = new Publisher("Porto Editora");
        Publisher p10 = new Publisher("Tinta da China");

        assertNotEquals(p9, p10);
    }

    @Test
    void publisherWithDifferentNameHaveDifferentHashCode() {
        Publisher p11 = new Publisher("Porto Editora");
        Publisher p12 = new Publisher("Tinta da China");

        assertNotEquals(p11.hashCode(), p12.hashCode());
    }

}
