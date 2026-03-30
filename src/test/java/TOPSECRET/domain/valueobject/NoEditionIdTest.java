package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoEditionIdTest {

    @Test
    void getIdentifierOfNoIdentifierShouldReturnEmptyString()
    {
        EditionId noId = new NoEditionId();
        assertEquals("", noId.getIdentifier());
    }

}