package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PublisherTest {
    @Test
    void shouldCreateValidPublisher() {
        Publisher p1 = new Publisher("Porto Editora");
        assertEquals("Porto Editora", p1.getName());
    }

    @Test
    void publisherNameIsTrimmed() {
        Publisher p = new Publisher (" Porto Editora  ");
        assertEquals("Porto Editora", p.getName());
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
}
