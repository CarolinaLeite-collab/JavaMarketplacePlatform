package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Item;
import TOPSECRET.domain.auction.Auction;
import TOPSECRET.domain.auction.AuctionFactory;
import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuctionId;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemoAuctionRepoTest {
    private MemoAuctionRepo _repo;
    private AuctionFactory _auctionFactoryDouble;
    private Auction _auctionDouble1;
    private Auction _auctionDouble2;
    private Auction _auctionDouble3;
    private AuctionId _idDouble;
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
        when(_auctionDouble1.identity()).thenReturn(new AuctionId("AUC-1"));
        _auctionDouble2 = mock(Auction.class);
        when(_auctionDouble2.getItems()).thenReturn(_items2);
        when(_auctionDouble2.identity()).thenReturn(new AuctionId("AUC-2"));
        _auctionDouble3 = mock(Auction.class);
        when(_auctionDouble3.getItems()).thenReturn(_items3);

        _idDouble = mock(AuctionId.class);
        when(_auctionDouble1.identity()).thenReturn(_idDouble);

        _startingPriceDouble = mock(Price.class);
        _outrightPriceDouble = mock(Price.class);
        _reservePriceDouble = mock(Price.class);

        _startDate = ZonedDateTime.now().plusDays(1);
        _endDate = ZonedDateTime.now().plusDays(2);

        _auctionFactoryDouble = mock(AuctionFactory.class);
        when(_auctionFactoryDouble.createAuction( _items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble1);
        when(_auctionFactoryDouble.createAuction( _items2, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble2);
        when(_auctionFactoryDouble.createAuction(_items3, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble3);
        }

    @Test
    void saveStoresAndReturnsAuction() {
        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        //Act
        Auction result = _repo.save(_auctionDouble1);

        //Assert
        assertSame(_auctionDouble1, result, "save should return the same Auction object");
        assertTrue(_repo.containsOfIdentity(_idDouble), "Auction should be contained after save");
    }

    @Test
    void ofIdentityReturnsOptionalWithAuctionIfPresent() {
        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        //Act
        _repo.save(_auctionDouble1);

        Optional<Auction> result = _repo.ofIdentity(_idDouble);

        //Assert
        assertTrue(result.isPresent(), "ofIdentity should return a non-empty Optional for stored Auction");
        assertSame(_auctionDouble1, result.get());
    }

    @Test
    void ofIdentityReturnsEmptyOptionalIfNotPresent() {
        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        //Act
        Optional<Auction> result = _repo.ofIdentity(mock(AuctionId.class));

        //Assert
        assertTrue(result.isEmpty(), "ofIdentity should return empty Optional for unknown AuctionId");
    }

    @Test
    void containsOfIdentityReturnsTrueWhenPresentAndFalseOtherwise() {
        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        //Act
        _repo.save(_auctionDouble1);
        AuctionId unknownId = mock(AuctionId.class);

        //Assert
        assertTrue(_repo.containsOfIdentity(_idDouble), "containsOfIdentity should return true for stored AuctionId");
        assertFalse(_repo.containsOfIdentity(unknownId), "containsOfIdentity should return false for unknown AuctionId");
    }

    @Test
    void addAuctionWithoutOutrightStoresAuction() {
        //Arrange
        when(_auctionFactoryDouble.createAuction(_items1, _startingPriceDouble, _reservePriceDouble,
                null, _startDate, _endDate)).thenReturn(_auctionDouble1);

        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        //Act
        Auction created = _repo.addAuction(_items1, _startingPriceDouble, _reservePriceDouble, _startDate, _endDate);

        //Assert
        assertSame(_auctionDouble1, created);
        verify(_auctionFactoryDouble).createAuction(_items1, _startingPriceDouble, _reservePriceDouble,
                null, _startDate, _endDate);
    }

    @Test
    void addAuctionStoresAuctionAndReturnsMatchingItemsByGenre() throws Exception {
        //Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        when(_auctionDouble1.isByGenre(genreIdDouble)).thenReturn(true);

        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        //Act
        Auction created = _repo.addAuction(_items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByGenreId(genreIdDouble);

        //Assert
        assertSame(_auctionDouble1, created);
        assertEquals(1, result.size());
        assertSame(_itemDouble1, result.get(0));
    }

    @Test
    void addAuctionShouldStoreAuctionInRepository() {
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        _repo.addAuction(_items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        assertTrue(_repo.findAll().iterator().hasNext());
    }

    @Test
    void getAuctionItemsByGenreReturnsEmptyListWhenNoAuctions() {
        //Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        //Act
        List<Item> result = _repo.getAuctionItemsByGenreId(genreIdDouble);

        //Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAuctionItemsByGenreReturnsMatchingItems() throws Exception {
        //Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        when(_auctionDouble1.isByGenre(genreIdDouble)).thenReturn(true);
        when(_auctionDouble2.isByGenre(genreIdDouble)).thenReturn(true);

        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        //Act
        _repo.addAuction(_items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);
        _repo.addAuction(_items2, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByGenreId(genreIdDouble);

        //Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(_itemDouble1));
        assertTrue(result.contains(_itemDouble2));
    }

    @Test
    void getAuctionItemsByGenreReturnsEmptyListWhenGenreIsNull() throws Exception {
        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        //Act
        _repo.addAuction(_items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByGenreId(null);

        //Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(_auctionDouble1).isByGenre(null);
    }

    @Test
    void getAuctionItemsByAuthorReturnsMatchingItems() throws Exception {
        //Arrange
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_auctionDouble1.isByAuthor(authorIdDouble)).thenReturn(true);
        when(_auctionDouble2.isByAuthor(authorIdDouble)).thenReturn(false);

        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        //Act
        _repo.addAuction(_items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);
        _repo.addAuction(_items2, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByAuthorId(authorIdDouble);

        //Assert
        assertEquals(1, result.size());
        assertSame(_itemDouble1, result.get(0));
    }

    @Test
    void getAuctionItemsByAuthorReturnsEmptyListWhenNoMatch() throws Exception {
        //Arrange
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_auctionDouble1.isByAuthor(authorIdDouble)).thenReturn(false);

        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        //Act
        _repo.addAuction(_items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByAuthorId(authorIdDouble);

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAuctionItemsByAuthorReturnsDefensiveCopy() throws Exception {
        //Arrange
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_auctionDouble1.isByAuthor(authorIdDouble)).thenReturn(true);

        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        //Act
        _repo.addAuction(_items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> first = _repo.getAuctionItemsByAuthorId(authorIdDouble);
        first.clear();

        List<Item> second = _repo.getAuctionItemsByAuthorId(authorIdDouble);

        //Assert
        assertEquals(1, second.size());
    }

    @Test
    void getAuctionItemsByPublicationReturnsMatchingItems() throws Exception {
        // Arrange
        Publication pubDouble = mock(Publication.class);
        when(_auctionDouble1.isByPublication(pubDouble)).thenReturn(true);
        when(_auctionDouble2.isByPublication(pubDouble)).thenReturn(true);
        when(_auctionDouble3.isByPublication(pubDouble)).thenReturn(false);

        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        // Act
        _repo.addAuction(_items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);
        _repo.addAuction(_items2, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);
        _repo.addAuction(_items3, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByPublicationId(pubDouble);

        //Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(_itemDouble1));
        assertTrue(result.contains(_itemDouble2));
        assertFalse(result.contains(_itemDouble3));
    }

    @Test
    void getAuctionItemsByPublishingCompanyReturnsMatchingItems() throws Exception {
        //Arrange
        PublishingCompany publisherDouble = mock(PublishingCompany.class);

        when(_auctionDouble1.isByPublishingCompany(publisherDouble)).thenReturn(true);
        when(_auctionDouble2.isByPublishingCompany(publisherDouble)).thenReturn(false);

        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        //Act
        _repo.addAuction(_items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);
        _repo.addAuction(_items2, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByPublishingCompanyId(publisherDouble);

        //Assert
        assertEquals(1, result.size());
        assertSame(_itemDouble1, result.get(0));
    }

    @Test
    void getAuctionItemsByPublishingCompanyReturnsEmptyListWhenPublisherIsNull() throws Exception {
        //SUT
        _repo = new MemoAuctionRepo(_auctionFactoryDouble);

        //Act
        _repo.addAuction(_items1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        List<Item> result = _repo.getAuctionItemsByPublishingCompanyId(null);

        //Assert
        assertTrue(result.isEmpty());
        verify(_auctionDouble1).isByPublishingCompany(null);
    }
}
