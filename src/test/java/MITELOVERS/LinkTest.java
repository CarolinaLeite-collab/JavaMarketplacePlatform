package MITELOVERS;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkTest {

    @Test
    void constructor_withTwoArgs_setsRelHrefAndDefaultMethodGET() {
        // arrange
        String rel = "self";
        String href = "/countries/1";

        // act (SUT)
        Link link = new Link(rel, href);

        // assert
        assertEquals(rel, link.rel());
        assertEquals(href, link.href());
        assertEquals("GET", link.method());
    }

    @Test
    void constructor_withThreeArgs_setsAllFields() {
        // arrange
        String rel = "update";
        String href = "/countries/1";
        String method = "PUT";

        // act (SUT)
        Link link = new Link(rel, href, method);

        // assert
        assertEquals(rel, link.rel());
        assertEquals(href, link.href());
        assertEquals(method, link.method());
    }

    @Test
    void fields_areStoredExactlyAsProvided() {
        // arrange
        String rel = "delete";
        String href = "/countries/1";
        String method = "DELETE";

        // act (SUT)
        Link link = new Link(rel, href, method);

        // assert
        assertSame(rel, link.rel());
        assertSame(href, link.href());
        assertSame(method, link.method());
    }

    @Test
    void method_canBeAnyStringProvided() {
        // arrange
        String rel = "custom";
        String href = "/test";
        String method = "PATCH";

        // act (SUT)
        Link link = new Link(rel, href, method);

        // assert
        assertEquals("PATCH", link.method());
    }

}