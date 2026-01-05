package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BindingTest {

    @Test
    void getDisplayNameShouldReturnCorrectText() {

        //Arrange
        Binding hardCover = Binding.HARDCOVER;
        Binding saddle = Binding.SADDLE_STITCH;
        Binding pur = Binding.PUR;

        //Act
        String hardCoverText = hardCover.getDisplayName();
        String saddleText = saddle.getDisplayName();
        String purBoundText = pur.getDisplayName();

        //Assert
        assertEquals("Hardcover binding", hardCover.getDisplayName());
        assertEquals("Saddle stitch binding", saddle.getDisplayName());
        assertEquals("PUR binding", pur.getDisplayName());
    }


    @Test
    void toStringShouldReturnDisplayName() {
        // Arrange
        Binding hardCover = Binding.HARDCOVER;
        Binding saddle = Binding.SADDLE_STITCH;

        // Act
        String hardCoverText = hardCover.toString();
        String saddleText = saddle.toString();

        // Assert
        assertEquals("Hardcover binding", hardCoverText);
        assertEquals("Saddle stitch binding", saddleText);
    }

}