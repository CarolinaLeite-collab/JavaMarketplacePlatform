package MITELOVERS.persistence.mem;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.AuctionFactory;
import MITELOVERS.domain.valueobject.AuctionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemAuctionRepoTest {
    private MemAuctionRepo _repo;
    private Auction _auctionDouble1;
    private AuctionId _idDouble1;
    private AuctionId _idDouble2;
    private List<AuctionId> _auctionIds;
    private ItemId _itemIdDouble1;
    private ItemId _itemIdDouble2;
    private List<ItemId> _itemsId1;
    private List<ItemId> _itemsId2;
    private Price _startingPriceDouble;
    private Price _outrightPriceDouble;
    private Price _reservePriceDouble;
    private ZonedDateTime _startDate;
    private ZonedDateTime _endDate;

    @BeforeEach
    void setUp() {
        _itemIdDouble1 = mock(ItemId.class);
        _itemsId1 = new ArrayList<>();
        _itemsId1.add(_itemIdDouble1);

        _itemIdDouble2 = mock(ItemId.class);
        _itemsId2 = new ArrayList<>();
        _itemsId2.add(_itemIdDouble2);

        _idDouble1 = mock(AuctionId.class);
        _auctionIds = new ArrayList<>();
        _auctionIds.add(_idDouble1);

        _idDouble2 = mock(AuctionId.class);

        _auctionDouble1 = mock(Auction.class);
        when(_auctionDouble1.getItemsId()).thenReturn(_itemsId1);
        when(_auctionDouble1.identity()).thenReturn(_idDouble1);


        _startingPriceDouble = mock(Price.class);
        _outrightPriceDouble = mock(Price.class);
        _reservePriceDouble = mock(Price.class);

        _startDate = ZonedDateTime.now().plusDays(1);
        _endDate = ZonedDateTime.now().plusDays(2);
    }

    @Test
    void shouldConstructRepo() {
        //SUT
        _repo = new MemAuctionRepo();
    }

    @Test
    void saveStoresAndReturnsAuction() {
        //SUT
        _repo = new MemAuctionRepo();

        //Act
        Auction result = _repo.save(_auctionDouble1);

        //Assert
        assertSame(_auctionDouble1, result);
        assertTrue(_repo.containsOfIdentity(_idDouble1));
    }

    @Test
    void findAllShouldReturnStoredAuctions() {
        //Arrange
        Auction auctionDouble2 = mock(Auction.class);
        AuctionFactory auctionFactoryDouble = mock(AuctionFactory.class);
        when(auctionDouble2.getItemsId()).thenReturn(_itemsId2);
        when(auctionDouble2.identity()).thenReturn(_idDouble2);

        when(auctionFactoryDouble.createAuction(_itemsId2, _startingPriceDouble, _reservePriceDouble,
                _outrightPriceDouble, _startDate, _endDate)).thenReturn(auctionDouble2);

        //SUT
        _repo = new MemAuctionRepo();

        // Act
        _repo.save(_auctionDouble1);
        _repo.save(auctionDouble2);

        Iterable<Auction> result = _repo.findAll();

        List<Auction> list = new ArrayList<>();
        result.forEach(list::add);

        // Assert
        assertEquals(2, list.size());
        assertTrue(list.contains(_auctionDouble1));
        assertTrue(list.contains(auctionDouble2));

    }

    @Test
    void findAllKeysShouldReturnEmptyWhenRepoIsEmpty() {
        //SUT
        _repo = new MemAuctionRepo();

        //Act
        _auctionIds = _repo.findAllKeys();

        //Assert
        assertTrue(_auctionIds.isEmpty());
    }

    @Test
    void findAllKeysShouldReturnAllKeys() {
        //Arrange
        Auction auctionDouble2 = mock(Auction.class);
        AuctionFactory auctionFactoryDouble = mock(AuctionFactory.class);
        when(auctionDouble2.getItemsId()).thenReturn(_itemsId2);
        when(auctionDouble2.identity()).thenReturn(_idDouble2);

        when(auctionFactoryDouble.createAuction(_itemsId2, _startingPriceDouble, _reservePriceDouble,
                _outrightPriceDouble, _startDate, _endDate)).thenReturn(auctionDouble2);

        //SUT
        _repo = new MemAuctionRepo();

        //Act
        _repo.save(_auctionDouble1);
        _repo.save(auctionDouble2);

        _auctionIds = _repo.findAllKeys();

        //Assert
        assertEquals(2, _auctionIds.size());
        assertTrue(_auctionIds.contains(_idDouble1));
        assertTrue(_auctionIds.contains(_idDouble2));
    }

    @Test
    void findAllKeysShouldReturnCopyNotAffectingRepo() {
        //SUT
        _repo = new MemAuctionRepo();

        //Act
        _repo.save(_auctionDouble1);

        _auctionIds = _repo.findAllKeys();
        _auctionIds.clear();

        //Assert
        assertTrue(_repo.containsOfIdentity(_idDouble1));
    }

    @Test
    void findAllKeysOrderShouldNotBeGuaranteed() {
        //Arrange
        Auction auctionDouble2 = mock(Auction.class);
        AuctionFactory auctionFactoryDouble = mock(AuctionFactory.class);
        when(auctionDouble2.getItemsId()).thenReturn(_itemsId2);
        when(auctionDouble2.identity()).thenReturn(_idDouble2);

        when(auctionFactoryDouble.createAuction(_itemsId2, _startingPriceDouble, _reservePriceDouble,
                _outrightPriceDouble, _startDate, _endDate)).thenReturn(auctionDouble2);

        //SUT
        _repo = new MemAuctionRepo();

        _repo.save(_auctionDouble1);
        _repo.save(auctionDouble2);

        //Act
        _auctionIds = _repo.findAllKeys();

        //Assert
        assertEquals(2, _auctionIds.size());
        assertTrue(_auctionIds.contains(_idDouble1));
        assertTrue(_auctionIds.contains(_idDouble2));

    }

    @Test
    void ofIdentityReturnsOptionalWithAuctionIfPresent() {
        //SUT
        _repo = new MemAuctionRepo();

        //Act
        _repo.save(_auctionDouble1);

        Optional<Auction> result = _repo.ofIdentity(_idDouble1);

        //Assert
        assertTrue(result.isPresent());
        assertSame(_auctionDouble1, result.get());
    }

    @Test
    void ofIdentityReturnsEmptyOptionalIfNotPresent() {
        //SUT
        _repo = new MemAuctionRepo();

        //Act
        Optional<Auction> result = _repo.ofIdentity(mock(AuctionId.class));

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void containsOfIdentityReturnsTrueWhenPresent() {
        //SUT
        _repo = new MemAuctionRepo();

        //Act
        _repo.save(_auctionDouble1);

        //Assert
        assertTrue(_repo.containsOfIdentity(_idDouble1));
    }

    @Test
    void containsOfIdentityReturnsFalseWhenNotPresent() {
        //SUT
        _repo = new MemAuctionRepo();

        //Act
        _repo.save(_auctionDouble1);
        AuctionId unknownId = mock(AuctionId.class);

        //Assert
        assertFalse(_repo.containsOfIdentity(unknownId));
    }

    @Test
    void findByItemsIdSortedShouldReturnItemsOrderedByAuctionEndDate() {

        MemAuctionRepo repo = new MemAuctionRepo();

        ItemId item1 = mock(ItemId.class);
        ItemId item2 = mock(ItemId.class);

        Auction auction1 = mock(Auction.class);
        Auction auction2 = mock(Auction.class);

        when(auction1.getItemsId()).thenReturn(List.of(item1));
        when(auction2.getItemsId()).thenReturn(List.of(item2));

        when(auction1.getAuctionEndDate()).thenReturn(Instant.now().plus(2, ChronoUnit.DAYS));
        when(auction2.getAuctionEndDate()).thenReturn(Instant.now().plus(1, ChronoUnit.DAYS));

        when(auction1.identity()).thenReturn(mock(AuctionId.class));
        when(auction2.identity()).thenReturn(mock(AuctionId.class));

        repo.save(auction1);
        repo.save(auction2);

        List<ItemId> result = repo.findByItemsIdSorted(List.of(item1, item2));

        assertEquals(List.of(item2, item1), result);
    }

    @Test
    void findByItemsIdSortedShouldReturnOnlyMatchingItems() {

        MemAuctionRepo repo = new MemAuctionRepo();

        ItemId item1 = mock(ItemId.class);
        ItemId item2 = mock(ItemId.class);
        ItemId item3 = mock(ItemId.class);

        Auction auction = mock(Auction.class);
        when(auction.getItemsId()).thenReturn(List.of(item1, item2, item3));
        when(auction.getAuctionEndDate()).thenReturn(Instant.now().plus(1, ChronoUnit.DAYS));
        when(auction.identity()).thenReturn(mock(AuctionId.class));

        repo.save(auction);

        List<ItemId> result = repo.findByItemsIdSorted(List.of(item1, item3));

        assertEquals(List.of(item1, item3), result);
    }

    @Test
    void findByItemsIdSortedShouldReturnEmptyWhenNoAuctionMatches() {

        MemAuctionRepo repo = new MemAuctionRepo();

        ItemId item1 = mock(ItemId.class);
        ItemId item2 = mock(ItemId.class);

        Auction auction = mock(Auction.class);
        when(auction.getItemsId()).thenReturn(List.of(mock(ItemId.class)));
        when(auction.getAuctionEndDate()).thenReturn(Instant.now().plus(1, ChronoUnit.DAYS));
        when(auction.identity()).thenReturn(mock(AuctionId.class));

        repo.save(auction);

        List<ItemId> result = repo.findByItemsIdSorted(List.of(item1, item2));

        assertTrue(result.isEmpty());
    }

}
