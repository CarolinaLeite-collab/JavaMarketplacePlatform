package TOPSECRET.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SubtitleTest {

    @Test
    public void testValidSubtitle() throws InstantiationException {
        Subtitle subtitle = new Subtitle("A Journey Begins");
        assertEquals("A Journey Begins", subtitle.getTitle());
        assertEquals("a journey begins", subtitle.getLowercaseTitle());
    }

    @Test
    public void testSubtitleWithExtraWhitespace() throws InstantiationException {
        Subtitle subtitle = new Subtitle("  Extra Spaces  ");
        assertEquals("Extra Spaces", subtitle.getTitle());
        assertEquals("extra spaces", subtitle.getLowercaseTitle());
    }

    @Test
    public void testEmptySubtitle() throws InstantiationException {
        Subtitle subtitle = new Subtitle("");
        assertEquals("", subtitle.getTitle());
        assertEquals("", subtitle.getLowercaseTitle());
    }

    @Test
    public void testBlankSubtitle() throws InstantiationException {
        Subtitle subtitle = new Subtitle("   ");
        assertEquals("", subtitle.getTitle());
        assertEquals("", subtitle.getLowercaseTitle());
    }

    @Test
    public void testNullSubtitle() throws InstantiationException {
        Subtitle subtitle = new Subtitle(null);
        assertEquals("", subtitle.getTitle());
        assertEquals("", subtitle.getLowercaseTitle());
    }

    @Test
    public void testSubtitleWithSpecialCharacters() throws InstantiationException {
        Subtitle subtitle = new Subtitle("Part 2: The Return!");
        assertEquals("Part 2: The Return!", subtitle.getTitle());
        assertEquals("part 2: the return!", subtitle.getLowercaseTitle());
    }

    @Test
    public void testSubtitleWithNumbers() throws InstantiationException {
        Subtitle subtitle = new Subtitle("Chapter 42");
        assertEquals("Chapter 42", subtitle.getTitle());
        assertEquals("chapter 42", subtitle.getLowercaseTitle());
    }

    @Test
    public void testSingleCharacterSubtitle() throws InstantiationException {
        Subtitle subtitle = new Subtitle("A");
        assertEquals("A", subtitle.getTitle());
        assertEquals("a", subtitle.getLowercaseTitle());
    }

    @Test
    public void testLongSubtitle() throws InstantiationException {
        String longText = "A Very Long Subtitle That Contains Many Words And Goes On For Quite Some Time";
        Subtitle subtitle = new Subtitle(longText);
        assertEquals(longText, subtitle.getTitle());
        assertEquals(longText.toLowerCase(), subtitle.getLowercaseTitle());
    }

    @Test
    public void testSubtitleInheritanceFromTitle() throws InstantiationException {
        Subtitle subtitle = new Subtitle("Test Subtitle");
        assertInstanceOf(Title.class, subtitle);
    }
}