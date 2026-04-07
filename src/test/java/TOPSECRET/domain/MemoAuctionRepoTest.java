package TOPSECRET.domain;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemoAuctionRepoTest {
    private MemoAuctionRepo repo;
    private AuctionFactory auctionFactoryDouble;
    private Auction _auctionDouble1;
    private Auction _auctionDouble2;
    private Auction _auctionDouble3;
    private Item _itemDouble1;
    private Item _itemDouble2;
    private Item _itemDouble3;
    private List<Item> _items1;
    private List<Item> _items2;
    private List<Item> _items3;
    private Price startingPriceDouble;
    private Price outrightPriceDouble;

    private ZonedDateTime startDate;
    private ZonedDateTime endDate;

    @BeforeEach
    void setUp() {
        auctionFactoryDouble = mock(AuctionFactory.class);
        repo = new MemoAuctionRepo(auctionFactoryDouble);

        _itemDouble1 = mock(Item.class);
        _items1 = new ArrayList<>();
        _items1.add(_itemDouble1);

        _itemDouble2 = mock(Item.class);
        _items2 = new ArrayList<>();
        _items2.add(_itemDouble2);

        _itemDouble3 = mock(Item.class);
        _items3 = new ArrayList<>();
        _items3.add(_itemDouble3);

        _auctionDouble1 = mock(Auction.class);
        when(_auctionDouble1.getItems()).thenReturn(_items1);
        _auctionDouble2 = mock(Auction.class);
        when(_auctionDouble2.getItems()).thenReturn(_items2);
        _auctionDouble3 = mock(Auction.class);
        when(_auctionDouble3.getItems()).thenReturn(_items3);

        startingPriceDouble = mock(Price.class);
        outrightPriceDouble = mock(Price.class);

        startDate = ZonedDateTime.of(2027, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        endDate   = ZonedDateTime.of(2027, 2, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
    }

    @Test
    void getAuctionItemsByGenreReturnsEmptyListWhenNoAuctions() {
        Genre genreDouble = mock(Genre.class);

        List<Item> result = repo.getAuctionItemsByGenre(genreDouble);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAuctionItemsByGenreReturnsMatchingItems() throws Exception {
        Genre genreDouble = mock(Genre.class);

        when(_auctionDouble1.isByGenre(genreDouble)).thenReturn(true);
        when(_auctionDouble2.isByGenre(genreDouble)).thenReturn(true);

        when(auctionFactoryDouble.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(_auctionDouble1);

        when(auctionFactoryDouble.createAuction(_items2, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(_auctionDouble2);

        repo.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate);
        repo.createAuction(_items2, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> result = repo.getAuctionItemsByGenre(genreDouble);

        assertEquals(2, result.size());
        assertTrue(result.contains(_itemDouble1));
        assertTrue(result.contains(_itemDouble2));
    }

    @Test
    void getAuctionItemsByGenreReturnsEmptyListWhenGenreIsNull() throws Exception {
        when(auctionFactoryDouble.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(_auctionDouble1);

        repo.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> result = repo.getAuctionItemsByGenre(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(_auctionDouble1).isByGenre(null);
    }

    @Test
    void getAuctionItemsByAuthorReturnsMatchingItems() throws Exception {
        Author authorDouble = mock(Author.class);

        when(_auctionDouble1.isByAuthor(authorDouble)).thenReturn(true);

        when(auctionFactoryDouble.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(_auctionDouble1);

        repo.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> result = repo.getAuctionItemsByAuthor(authorDouble);

        assertEquals(1, result.size());
        assertSame(_itemDouble1, result.get(0));
    }

    @Test
    void getAuctionItemsByAuthorReturnsEmptyListWhenNoMatch() throws Exception {
        Author authorDouble = mock(Author.class);

        when(_auctionDouble1.isByAuthor(authorDouble)).thenReturn(false);

        when(auctionFactoryDouble.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(_auctionDouble1);

        repo.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> result = repo.getAuctionItemsByAuthor(authorDouble);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAuctionItemsByAuthorReturnsDefensiveCopy() throws Exception {
        Author authorDouble = mock(Author.class);

        when(_auctionDouble1.isByAuthor(authorDouble)).thenReturn(true);

        when(auctionFactoryDouble.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(_auctionDouble1);

        repo.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> first = repo.getAuctionItemsByAuthor(authorDouble);
        first.clear();

        List<Item> second = repo.getAuctionItemsByAuthor(authorDouble);

        assertEquals(1, second.size());
    }

    @Test
    void getAuctionItemsByPublicationReturnsMatchingItems() throws Exception {
        Publication pubDouble = mock(Publication.class);

        when(_auctionDouble1.isByPublication(pubDouble)).thenReturn(true);
        when(_auctionDouble2.isByPublication(pubDouble)).thenReturn(true);
        when(_auctionDouble3.isByPublication(pubDouble)).thenReturn(false);

        when(auctionFactoryDouble.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(_auctionDouble1);
        when(auctionFactoryDouble.createAuction(_items2, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(_auctionDouble2);
        when(auctionFactoryDouble.createAuction(_items3, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(_auctionDouble3);

        repo.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate);
        repo.createAuction(_items2, startingPriceDouble, outrightPriceDouble, startDate, endDate);
        repo.createAuction(_items3, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> result = repo.getAuctionItemsByPublication(pubDouble);

        assertEquals(2, result.size());
        assertTrue(result.contains(_itemDouble1));
        assertTrue(result.contains(_itemDouble2));
        assertFalse(result.contains(_itemDouble3));
    }

    @Test
    void getAuctionItemsByPublishingCompanyReturnsMatchingItems() throws Exception {
        PublishingCompany publisherDouble = mock(PublishingCompany.class);
        Price startingPriceDouble = mock(Price.class);

        when(_auctionDouble1.isByPublishingCompany(publisherDouble)).thenReturn(true);

        when(auctionFactoryDouble.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(_auctionDouble1);

        repo.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> result = repo.getAuctionItemsByPublishingCompany(publisherDouble);

        assertEquals(1, result.size());
        assertSame(_itemDouble1, result.get(0));
    }

    @Test
    void getAuctionItemsByPublishingCompanyReturnsEmptyListWhenPublisherIsNull() throws Exception {
        when(auctionFactoryDouble.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(_auctionDouble1);

        repo.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        List<Item> result = repo.getAuctionItemsByPublishingCompany(null);

        assertTrue(result.isEmpty());
        verify(_auctionDouble1).isByPublishingCompany(null);
    }

    @Test
    void createAuctionStoresAuction() throws Exception {
        Genre genreDouble = mock(Genre.class);

        when(_auctionDouble1.isByGenre(genreDouble)).thenReturn(true);

        when(auctionFactoryDouble.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(_auctionDouble1);

        Auction created = repo.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        assertSame(_auctionDouble1, created);

        List<Item> result = repo.getAuctionItemsByGenre(genreDouble);
        assertEquals(1, result.size());
        assertSame(_itemDouble1, result.get(0));
    }

    @Test
    void createAuctionWithOutrightStoresAuction() throws Exception {
        when(auctionFactoryDouble.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate))
                .thenReturn(_auctionDouble1);

        Auction created = repo.createAuction(_items1, startingPriceDouble, outrightPriceDouble, startDate, endDate);

        assertSame(_auctionDouble1, created);
    }
}
