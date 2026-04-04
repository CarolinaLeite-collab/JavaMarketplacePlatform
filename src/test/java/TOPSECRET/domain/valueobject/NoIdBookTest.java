package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoIdBookTest {

    @Test
    void getIdentifierOfNoIdentifierShouldReturnEmptyString()
    {
        EditionId noId = new NoIdBook();
        assertEquals("", noId.getIdentifier());
    }

}