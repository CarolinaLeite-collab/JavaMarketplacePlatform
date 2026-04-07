package TOPSECRET.domain;

import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.Condition;
import TOPSECRET.domain.valueobject.GenreId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link Item}.
 *
 * <p>Tests are divided into two categories:
 * <ul>
 *   <li><b>Integration-style</b> — use real domain objects ({@link Publication}, {@link Condition})
 *       to verify Item behaviour end-to-end (sale/auction lifecycle, condition preservation).</li>
 *   <li><b>Isolated</b> — use Mockito doubles for {@link Publication} and {@link Condition}
 *       to verify delegation of {@code isByAuthor}, {@code isByGenre} and {@code isByPublication}.</li>
 * </ul>
 */

class ItemTest {

    private Publication publicationDouble;
    private Condition conditionDouble;
    private Auction auctionDouble;
    private DirectSale directSaleDouble;

    @BeforeEach
    void setUp() {
        publicationDouble = mock(Publication.class);
        conditionDouble = Condition.GOOD; // enum, no need to mock
        auctionDouble = mock(Auction.class);
        directSaleDouble = mock(DirectSale.class);
    }

    // ------------------------------------------------------------
    // Creation
    // ------------------------------------------------------------

    @Test
    void itemIsCreatedWithPublicationAndCondition() {
        Item item = new Item(publicationDouble, Condition.GOOD);

        assertSame(publicationDouble, item.get_publication());
        assertEquals(Condition.GOOD, item.get_condition());
    }

    // ------------------------------------------------------------
    // Direct Sale
    // ------------------------------------------------------------

    @Test
    void canSetDirectSaleWhenNoAuctionExists() {
        Item item = new Item(publicationDouble, Condition.LIKE_NEW);

        when(directSaleDouble.getItem()).thenReturn(item);

        assertDoesNotThrow(() -> item.setDirectSale(directSaleDouble));
    }

    @Test
    void cannotSetDirectSaleIfAuctionAlreadyExists() {
        Item item = new Item(publicationDouble, Condition.GOOD);

        when(auctionDouble.getItem()).thenReturn(item);
        item.setAuction(auctionDouble);

        when(directSaleDouble.getItem()).thenReturn(item);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> item.setDirectSale(directSaleDouble)
        );

        assertEquals("Item is already in an auction.", ex.getMessage());
    }

    @Test
    void settingDirectSaleDoesNotOverwriteCondition() {
        Item item = new Item(publicationDouble, Condition.POOR);

        when(directSaleDouble.getItem()).thenReturn(item);
        item.setDirectSale(directSaleDouble);

        assertEquals(Condition.POOR, item.get_condition());
    }

    @Test
    void puttingPublicationOnDirectSaleWrongDirectSaleItem() {
        Item item = new Item(publicationDouble, Condition.GOOD);

        Item wrongItem = mock(Item.class);
        when(directSaleDouble.getItem()).thenReturn(wrongItem);

        assertThrows(IllegalArgumentException.class,
                () -> item.setDirectSale(directSaleDouble));
    }

    @Test
    void getDirectSaleReturnsAssignedDirectSale() {
        Item item = new Item(publicationDouble, Condition.GOOD);

        when(directSaleDouble.getItem()).thenReturn(item);
        item.setDirectSale(directSaleDouble);

        assertSame(directSaleDouble, item.getDirectSale());
    }

    @Test
    void getDirectSaleReturnsNullWhenNoneAssigned() {
        Item item = new Item(publicationDouble, Condition.GOOD);

        assertNull(item.getDirectSale());
    }

    // ------------------------------------------------------------
    // Auction
    // ------------------------------------------------------------

    @Test
    void canSetAuctionWhenNoDirectSaleExists() {
        Item item = new Item(publicationDouble, Condition.FAIR);

        when(auctionDouble.getItem()).thenReturn(item);

        assertDoesNotThrow(() -> item.setAuction(auctionDouble));
    }

    @Test
    void cannotSetAuctionIfDirectSaleAlreadyExists() {
        Item item = new Item(publicationDouble, Condition.GOOD);

        when(directSaleDouble.getItem()).thenReturn(item);
        item.setDirectSale(directSaleDouble);

        when(auctionDouble.getItem()).thenReturn(item);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> item.setAuction(auctionDouble)
        );

        assertTrue(ex.getMessage().contains("Item is already in a direct sale."));
    }

    @Test
    void settingAuctionDoesNotOverwriteCondition() {
        Item item = new Item(publicationDouble, Condition.LIKE_NEW);

        when(auctionDouble.getItem()).thenReturn(item);
        item.setAuction(auctionDouble);

        assertEquals(Condition.LIKE_NEW, item.get_condition());
    }

    @Test
    void puttingPublicationOnAuctionWrongAuctionItem() {
        Item item = new Item(publicationDouble, Condition.GOOD);

        Item wrongItem = mock(Item.class);
        when(auctionDouble.getItem()).thenReturn(wrongItem);

        assertThrows(IllegalArgumentException.class,
                () -> item.setAuction(auctionDouble));
    }

    @Test
    void getAuctionReturnsAssignedAuction() {
        Item item = new Item(publicationDouble, Condition.GOOD);

        when(auctionDouble.getItem()).thenReturn(item);
        item.setAuction(auctionDouble);

        assertSame(auctionDouble, item.getAuction());
    }

    // ------------------------------------------------------------
    // Delegation to Publication
    // ------------------------------------------------------------

    @Test
    void isByAuthorDelegatesToPublication() {
        AuthorId authorIdDouble = mock(AuthorId.class);
        Item item = new Item(publicationDouble, conditionDouble);

        item.isByAuthor(authorIdDouble);

        verify(publicationDouble).isByAuthor(authorIdDouble);
    }

    @Test
    void isByGenreDelegatesToPublication() {
        GenreId genreIdDouble = mock(GenreId.class);
        Item item = new Item(publicationDouble, conditionDouble);

        item.isByGenre(genreIdDouble);

        verify(publicationDouble).isByGenre(genreIdDouble);
    }

    @Test
    void isByPublicationDelegatesCorrectly() {
        Item item = new Item(publicationDouble, conditionDouble);

        assertTrue(item.isByPublication(publicationDouble));
        assertFalse(item.isByPublication(mock(Publication.class)));
    }

    // ------------------------------------------------------------
    // Equality & HashCode
    // ------------------------------------------------------------

    @Test
    void equalItemsReturnTrueWhenPublicationsMatch() {
        Item item1 = new Item(publicationDouble, conditionDouble);
        Item item2 = new Item(publicationDouble, conditionDouble);

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void notEqualWhenPublicationsDiffer() {
        Publication publication_double2 = mock(Publication.class);

        Item item1 = new Item(publicationDouble, conditionDouble);
        Item item2 = new Item(publication_double2, conditionDouble);

        assertNotEquals(item1, item2);
        assertNotEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void equalsReturnsTrueForSameObject() {
        Item item = new Item(publicationDouble, conditionDouble);

        assertEquals(item, item);
    }

    @Test
    void equalsReturnsFalseForDifferentType() {
        Item item = new Item(publicationDouble, conditionDouble);

        assertNotEquals(item, "not-an-item");
    }

    // -------------
    // Is by Author
    // -------------

    @Test
    void isByAuthorReturnsTrueWhenPublicationMatches() {
        AuthorId authorIdDouble = mock(AuthorId.class);
        Publication pubDouble = mock(Publication.class);
        when(pubDouble.isByAuthor(authorIdDouble)).thenReturn(true);

        Item item = new Item(pubDouble, conditionDouble);

        assertTrue(item.isByAuthor(authorIdDouble));
    }

    @Test
    void isByAuthorReturnsFalseWhenPublicationDoesNotMatch() {
        AuthorId authorIdDouble = mock(AuthorId.class);
        Publication pubDouble = mock(Publication.class);
        when(pubDouble.isByAuthor(authorIdDouble)).thenReturn(false);

        Item item = new Item(pubDouble, conditionDouble);

        assertFalse(item.isByAuthor(authorIdDouble));
    }

    // --------------------
    // Is By Genre
    // --------------------

    @Test
    void isByGenreReturnsTrueWhenPublicationMatches() {
        GenreId genreIdDouble = mock(GenreId.class);
        Publication pub = mock(Publication.class);
        when(pub.isByGenre(genreIdDouble)).thenReturn(true);

        Item item = new Item(pub, conditionDouble);

        assertTrue(item.isByGenre(genreIdDouble));
    }

    @Test
    void isByGenreReturnsFalseWhenPublicationDoesNotMatch() {
        GenreId genreIdDouble = mock(GenreId.class);
        Publication pub = mock(Publication.class);
        when(pub.isByGenre(genreIdDouble)).thenReturn(false);

        Item item = new Item(pub, conditionDouble);

        assertFalse(item.isByGenre(genreIdDouble));
    }

    // ------------------------------
    // Is by PublishingCompany - waiting for refactoring
    // ------------------------------

    @Test
    void isByPublishingCompany_returnsAlwaysFalse() {
        // Arrange
        PublishingCompany _publisherDouble = mock(PublishingCompany.class);
        Item item = new Item(publicationDouble, conditionDouble);

        // Act
        boolean result = item.isByPublishingCompany(_publisherDouble); // SUT

        // Assert
        assertFalse(result);
    }

}