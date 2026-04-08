package TOPSECRET.domain;

import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemoAuctionRepoTest {
    private MemoAuctionRepo repo;
    private AuctionFactory auctionFactoryDouble;

    private Price startingPriceDouble;
    private Price outrightPriceDouble;

    private ZonedDateTime startDate;
    private ZonedDateTime endDate;

    @BeforeEach
    void setUp() {
        auctionFactoryDouble = mock(AuctionFactory.class);
        repo = new MemoAuctionRepo(auctionFactoryDouble);

        startingPriceDouble = mock(Price.class);
        outrightPriceDouble = mock(Price.class);

        startDate = ZonedDateTime.of(2027, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        endDate   = ZonedDateTime.of(2027, 2, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
    }

    // Helper: auction with item
    private Auction auctionWithItem(Item itemDouble) {
        Auction auctionDouble = mock(Auction.class);
        when(auctionDouble.getItem()).thenReturn(itemDouble);
        return auctionDouble;
    }

    @Test
    void getAuctionItemsByGenreReturnsEmptyListWhenNoAuctions() {
        GenreId genreIdDouble = mock(GenreId.class);

        List<Item> result = repo.getAuctionItemsByGenre(genreIdDouble);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAuctionItemsByGenreReturnsMatchingItems() throws Exception {
        GenreId genreIdDouble = mock(GenreId.class);

        Item itemDouble1 = mock(Item.class);
        Item itemDouble2 = mock(Item.class);

        Auction auctionDouble1 = auctionWithItem(itemDouble1);
        Auction auctionDouble2 = auctionWithItem(itemDouble2);

        when(auctionDouble1.isByGenre(genreIdDouble)).thenReturn(true);
        when(auctionDouble2.isByGenre(genreIdDouble)).thenReturn(true);

        when(auctionFactoryDouble.createAuction(itemDouble1, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(auctionDouble1);

        when(auctionFactoryDouble.createAuction(itemDouble2, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(auctionDouble2);

        repo.createAuction(itemDouble1, startingPriceDouble, outrightPriceDouble, startDate, endDate);
        repo.createAuction(itemDouble2, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> result = repo.getAuctionItemsByGenre(genreIdDouble);

        assertEquals(2, result.size());
        assertTrue(result.contains(itemDouble1));
        assertTrue(result.contains(itemDouble2));
    }

    @Test
    void getAuctionItemsByGenreReturnsEmptyListWhenGenreIsNull() throws Exception {
        Item itemDouble = mock(Item.class);

        Auction auctionDouble = auctionWithItem(itemDouble);

        when(auctionFactoryDouble.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(auctionDouble);

        repo.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> result = repo.getAuctionItemsByGenre(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(auctionDouble).isByGenre(null);
    }

    @Test
    void getAuctionItemsByAuthorReturnsMatchingItems() throws Exception {
        AuthorId authorIdDouble = mock(AuthorId.class);
        Item itemDouble = mock(Item.class);

        Auction auctionDouble = auctionWithItem(itemDouble);
        when(auctionDouble.isByAuthor(authorIdDouble)).thenReturn(true);

        when(auctionFactoryDouble.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(auctionDouble);

        repo.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> result = repo.getAuctionItemsByAuthor(authorIdDouble);

        assertEquals(1, result.size());
        assertSame(itemDouble, result.get(0));
    }

    @Test
    void getAuctionItemsByAuthorReturnsEmptyListWhenNoMatch() throws Exception {
        AuthorId authorIdDouble = mock(AuthorId.class);
        Item itemDouble = mock(Item.class);

        Auction auctionDouble = auctionWithItem(itemDouble);
        when(auctionDouble.isByAuthor(authorIdDouble)).thenReturn(false);

        when(auctionFactoryDouble.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(auctionDouble);

        repo.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> result = repo.getAuctionItemsByAuthor(authorIdDouble);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAuctionItemsByAuthorReturnsDefensiveCopy() throws Exception {
        AuthorId authorIdDouble = mock(AuthorId.class);
        Item itemDouble = mock(Item.class);

        Auction auctionDouble = auctionWithItem(itemDouble);
        when(auctionDouble.isByAuthor(authorIdDouble)).thenReturn(true);

        when(auctionFactoryDouble.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(auctionDouble);

        repo.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> first = repo.getAuctionItemsByAuthor(authorIdDouble);
        first.clear();

        List<Item> second = repo.getAuctionItemsByAuthor(authorIdDouble);

        assertEquals(1, second.size());
    }

    @Test
    void getAuctionItemsByPublicationReturnsMatchingItems() throws Exception {
        Publication pubDouble = mock(Publication.class);

        Item itemDouble1 = mock(Item.class);
        Item itemDouble2 = mock(Item.class);
        Item itemDouble3 = mock(Item.class);

        Auction auction1 = auctionWithItem(itemDouble1);
        Auction auction2 = auctionWithItem(itemDouble2);
        Auction auction3 = auctionWithItem(itemDouble3);

        when(auction1.isByPublication(pubDouble)).thenReturn(true);
        when(auction2.isByPublication(pubDouble)).thenReturn(true);
        when(auction3.isByPublication(pubDouble)).thenReturn(false);

        when(auctionFactoryDouble.createAuction(itemDouble1, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(auction1);
        when(auctionFactoryDouble.createAuction(itemDouble2, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(auction2);
        when(auctionFactoryDouble.createAuction(itemDouble3, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(auction3);

        repo.createAuction(itemDouble1, startingPriceDouble, outrightPriceDouble, startDate, endDate);
        repo.createAuction(itemDouble2, startingPriceDouble, outrightPriceDouble, startDate, endDate);
        repo.createAuction(itemDouble3, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> result = repo.getAuctionItemsByPublication(pubDouble);

        assertEquals(2, result.size());
        assertTrue(result.contains(itemDouble1));
        assertTrue(result.contains(itemDouble2));
        assertFalse(result.contains(itemDouble3));
    }

    @Test
    void getAuctionItemsByPublishingCompanyReturnsMatchingItems() throws Exception {
        PublishingCompany publisherDouble = mock(PublishingCompany.class);

        Item itemDouble = mock(Item.class);
        Price startingPriceDouble = mock(Price.class);

        Auction auctionDouble = auctionWithItem(itemDouble);
        when(auctionDouble.isByPublishingCompany(publisherDouble)).thenReturn(true);

        when(auctionFactoryDouble.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(auctionDouble);

        repo.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> result = repo.getAuctionItemsByPublishingCompany(publisherDouble);

        assertEquals(1, result.size());
        assertSame(itemDouble, result.get(0));
    }

    @Test
    void getAuctionItemsByPublishingCompanyReturnsEmptyListWhenPublisherIsNull() throws Exception {
        Item itemDouble = mock(Item.class);

        Auction auctionDouble = auctionWithItem(itemDouble);

        when(auctionFactoryDouble.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(auctionDouble);

        repo.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> result = repo.getAuctionItemsByPublishingCompany(null);

        assertTrue(result.isEmpty());
        verify(auctionDouble).isByPublishingCompany(null);
    }

    @Test
    void createAuctionStoresAuction() throws Exception {
        Item itemDouble = mock(Item.class);

        GenreId genreIdDouble = mock(GenreId.class);

        Auction auctionDouble = auctionWithItem(itemDouble);
        when(auctionDouble.isByGenre(genreIdDouble)).thenReturn(true);

        when(auctionFactoryDouble.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(auctionDouble);

        Auction created = repo.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        assertSame(auctionDouble, created);

        List<Item> result = repo.getAuctionItemsByGenre(genreIdDouble);
        assertEquals(1, result.size());
        assertSame(itemDouble, result.get(0));
    }

    @Test
    void createAuctionWithOutrightStoresAuction() throws Exception {
        Item itemDouble = mock(Item.class);

        Auction auctionDouble = auctionWithItem(itemDouble);

        when(auctionFactoryDouble.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(auctionDouble);

        Auction created = repo.createAuction(itemDouble, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        assertSame(auctionDouble, created);
    }
}
