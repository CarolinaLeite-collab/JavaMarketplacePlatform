package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoIdentifierTest {

    @Test
    void getIdentifierOfNoIdentifierShouldReturnEmptyString()
    {
        Identifier noId = new NoIdentifier();
        assertEquals("", noId.getIdentifier());
    }

}