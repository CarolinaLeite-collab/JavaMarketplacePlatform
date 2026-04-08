package TOPSECRET.domain;

import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.valueobject.AuctionId;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemoAuctionRepoTest {
    private MemoAuctionRepo _repo;
    private AuctionFactory _auctionFactoryDouble;
    private Auction _auctionDouble1;
    private Auction _auctionDouble2;
    private Auction _auctionDouble3;
    private AuctionId _auctionId;
    private Item _itemDouble1;
    private Item _itemDouble2;
    private Item _itemDouble3;
    private List<Item> _items1;
    private List<Item> _items2;
    private List<Item> _items3;
    private Price _startingPriceDouble;
    private Price _outrightPriceDouble;
    private Price _reservePriceDouble;
    private ZonedDateTime _startDate;
    private ZonedDateTime _endDate;

    @BeforeEach
    void setUp() {
        _auctionFactoryDouble = mock(AuctionFactory.class);
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        _itemDouble1 = mock(Item.class);
        _items1 = new ArrayList<>();
        _items1.add(_itemDouble1);

        _itemDouble2 = mock(Item.class);
        _items2 = new ArrayList<>();
        _items2.add(_itemDouble2);

        _itemDouble3 = mock(Item.class);
        _items3 = new ArrayList<>();
        _items3.add(_itemDouble3);

        _auctionId = mock(AuctionId.class);

        _auctionDouble1 = mock(Auction.class);
        when(_auctionDouble1.getItems()).thenReturn(_items1);
        _auctionDouble2 = mock(Auction.class);
        when(_auctionDouble2.getItems()).thenReturn(_items2);
        _auctionDouble3 = mock(Auction.class);
        when(_auctionDouble3.getItems()).thenReturn(_items3);

        _startingPriceDouble = mock(Price.class);
        _outrightPriceDouble = mock(Price.class);
        _reservePriceDouble = mock(Price.class);

        _startDate = ZonedDateTime.now().plusDays(1);
        _endDate = ZonedDateTime.now().plusDays(2);

        }

    @Test
    void getAuctionItemsByGenreReturnsEmptyListWhenNoAuctions() {
        GenreId genreIdDouble = mock(GenreId.class);

        List<Item> result = _repo.getAuctionItemsByGenre(genreIdDouble);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAuctionItemsByGenreReturnsMatchingItems() throws Exception {
        GenreId genreIdDouble = mock(GenreId.class);

        when(_auctionDouble1.isByGenre(genreIdDouble)).thenReturn(true);
        when(_auctionDouble2.isByGenre(genreIdDouble)).thenReturn(true);

        when(_auctionFactoryDouble.createAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble1);

        when(_auctionFactoryDouble.createAuction(_auctionId, _items2, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble2);

        _repo.addAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);
        _repo.addAuction(_auctionId, _items2, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByGenre(genreIdDouble);

        assertEquals(2, result.size());
        assertTrue(result.contains(_itemDouble1));
        assertTrue(result.contains(_itemDouble2));
    }

    @Test
    void getAuctionItemsByGenreReturnsEmptyListWhenGenreIsNull() throws Exception {
        when(_auctionFactoryDouble.createAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble1);

        _repo.addAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByGenre(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(_auctionDouble1).isByGenre(null);
    }

    @Test
    void getAuctionItemsByAuthorReturnsMatchingItems() throws Exception {
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_auctionDouble1.isByAuthor(authorIdDouble)).thenReturn(true);

        when(_auctionFactoryDouble.createAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble1);

        _repo.addAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByAuthor(authorIdDouble);

        assertEquals(1, result.size());
        assertSame(_itemDouble1, result.get(0));
    }

    @Test
    void getAuctionItemsByAuthorReturnsEmptyListWhenNoMatch() throws Exception {
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_auctionDouble1.isByAuthor(authorIdDouble)).thenReturn(false);

        when(_auctionFactoryDouble.createAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble1);

        _repo.addAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByAuthor(authorIdDouble);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAuctionItemsByAuthorReturnsDefensiveCopy() throws Exception {
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_auctionDouble1.isByAuthor(authorIdDouble)).thenReturn(true);

        when(_auctionFactoryDouble.createAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble1);

        _repo.addAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> first = _repo.getAuctionItemsByAuthor(authorIdDouble);
        first.clear();

        List<Item> second = _repo.getAuctionItemsByAuthor(authorIdDouble);

        assertEquals(1, second.size());
    }

    @Test
    void getAuctionItemsByPublicationReturnsMatchingItems() throws Exception {
        Publication pubDouble = mock(Publication.class);

        when(_auctionDouble1.isByPublication(pubDouble)).thenReturn(true);
        when(_auctionDouble2.isByPublication(pubDouble)).thenReturn(true);
        when(_auctionDouble3.isByPublication(pubDouble)).thenReturn(false);

        when(_auctionFactoryDouble.createAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble1);
        when(_auctionFactoryDouble.createAuction(_auctionId, _items2, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble2);
        when(_auctionFactoryDouble.createAuction(_auctionId, _items3, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble3);

        _repo.addAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);
        _repo.addAuction(_auctionId, _items2, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);
        _repo.addAuction(_auctionId, _items3, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByPublication(pubDouble);

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

        when(_auctionFactoryDouble.createAuction(_auctionId, _items1, startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble1);

        _repo.addAuction(_auctionId, _items1, startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByPublishingCompany(publisherDouble);

        assertEquals(1, result.size());
        assertSame(_itemDouble1, result.get(0));
    }

    @Test
    void getAuctionItemsByPublishingCompanyReturnsEmptyListWhenPublisherIsNull() throws Exception {
        when(_auctionFactoryDouble.createAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble1);

        _repo.addAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByPublishingCompany(null);

        assertTrue(result.isEmpty());
        verify(_auctionDouble1).isByPublishingCompany(null);
    }

    @Test
    void createAuctionStoresAuction() throws Exception {
        GenreId genreIdDouble = mock(GenreId.class);

        when(_auctionDouble1.isByGenre(genreIdDouble)).thenReturn(true);

        when(_auctionFactoryDouble.createAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble1);

        Auction created = _repo.addAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        assertSame(_auctionDouble1, created);

        List<Item> result = _repo.getAuctionItemsByGenre(genreIdDouble);
        assertEquals(1, result.size());
        assertSame(_itemDouble1, result.get(0));
    }

    @Test
    void createAuctionWithOutrightStoresAuction() throws Exception {
        when(_auctionFactoryDouble.createAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble1);

        Auction created = _repo.addAuction(_auctionId, _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        assertSame(_auctionDouble1, created);
    }
}
