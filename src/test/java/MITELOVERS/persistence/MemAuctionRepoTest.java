package MITELOVERS.persistence;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.AuctionFactory;
import MITELOVERS.domain.valueobject.AuctionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemAuctionRepoTest {
    private MemAuctionRepo _repo;
    private AuctionFactory _auctionFactoryDouble;
    private Auction _auctionDouble1;
    private Auction _auctionDouble2;
    private Auction _auctionDouble3;
    private AuctionId _idDouble1;
    private AuctionId _idDouble2;
    private List<AuctionId> _auctionIds;
    private ItemId _itemIdDouble1;
    private ItemId _itemIdDouble2;
    private ItemId _itemIdDouble3;
    private List<ItemId> _itemsId1;
    private List<ItemId> _itemsId2;
    private List<ItemId> _itemsId3;
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

        _itemIdDouble3 = mock(ItemId.class);
        _itemsId3 = new ArrayList<>();
        _itemsId3.add(_itemIdDouble3);

        _idDouble1 = mock(AuctionId.class);
        _auctionIds = new ArrayList<>();
        _auctionIds.add(_idDouble1);

        _idDouble2 = mock(AuctionId.class);

        _auctionDouble1 = mock(Auction.class);
        when(_auctionDouble1.getItemsId()).thenReturn(_itemsId1);
        when(_auctionDouble1.identity()).thenReturn(_idDouble1);
        _auctionDouble2 = mock(Auction.class);
        when(_auctionDouble2.getItemsId()).thenReturn(_itemsId2);
        when(_auctionDouble2.identity()).thenReturn(_idDouble2);
        _auctionDouble3 = mock(Auction.class);
        when(_auctionDouble3.getItemsId()).thenReturn(_itemsId3);

        _startingPriceDouble = mock(Price.class);
        _outrightPriceDouble = mock(Price.class);
        _reservePriceDouble = mock(Price.class);

        _startDate = ZonedDateTime.now().plusDays(1);
        _endDate = ZonedDateTime.now().plusDays(2);

        _auctionFactoryDouble = mock(AuctionFactory.class);
        when(_auctionFactoryDouble.createAuction(_itemsId1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble1);
        when(_auctionFactoryDouble.createAuction(_itemsId2, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble2);
        when(_auctionFactoryDouble.createAuction(_itemsId3, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate))
                .thenReturn(_auctionDouble3);
    }

    @Test
    void shouldConstructRepo() {
        //SUT
        _repo = new MemAuctionRepo(_auctionFactoryDouble);
    }

    @Test
    void saveStoresAndReturnsAuction() {
        //SUT
        _repo = new MemAuctionRepo(_auctionFactoryDouble);

        //Act
        Auction result = _repo.save(_auctionDouble1);

        //Assert
        assertSame(_auctionDouble1, result);
        assertTrue(_repo.containsOfIdentity(_idDouble1));
    }

    @Test
    void findAllKeysShouldReturnEmptyWhenRepoIsEmpty() {
        //SUT
        _repo = new MemAuctionRepo(_auctionFactoryDouble);

        //Act
        _auctionIds = _repo.findAllKeys();

        //Assert
        assertTrue(_auctionIds.isEmpty());
    }

    @Test
    void findAllKeysShouldReturnAllKeys() {
        //SUT
        _repo = new MemAuctionRepo(_auctionFactoryDouble);

        //Act
        _repo.save(_auctionDouble1);
        _repo.save(_auctionDouble2);

        _auctionIds = _repo.findAllKeys();

        //Assert
        assertEquals(2, _auctionIds.size());
        assertTrue(_auctionIds.contains(_idDouble1));
        assertTrue(_auctionIds.contains(_idDouble2));
    }

    @Test
    void findAllKeysShouldReturnCopyNotAffectingRepo() {
        //SUT
        _repo = new MemAuctionRepo(_auctionFactoryDouble);

        //Act
        _repo.save(_auctionDouble1);

        _auctionIds = _repo.findAllKeys();
        _auctionIds.clear();

        //Assert
        assertTrue(_repo.containsOfIdentity(_idDouble1));
    }

    @Test
    void findAllKeysOrderShouldNotBeGuaranteed() {
        //SUT
        _repo = new MemAuctionRepo(_auctionFactoryDouble);

        _repo.save(_auctionDouble1);
        _repo.save(_auctionDouble2);

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
        _repo = new MemAuctionRepo(_auctionFactoryDouble);

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
        _repo = new MemAuctionRepo(_auctionFactoryDouble);

        //Act
        Optional<Auction> result = _repo.ofIdentity(mock(AuctionId.class));

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void containsOfIdentityReturnsTrueWhenPresent() {
        //SUT
        _repo = new MemAuctionRepo(_auctionFactoryDouble);

        //Act
        _repo.save(_auctionDouble1);

        //Assert
        assertTrue(_repo.containsOfIdentity(_idDouble1));
    }

    @Test
    void containsOfIdentityReturnsFalseWhenNotPresent() {
        //SUT
        _repo = new MemAuctionRepo(_auctionFactoryDouble);

        //Act
        _repo.save(_auctionDouble1);
        AuctionId unknownId = mock(AuctionId.class);

        //Assert
        assertFalse(_repo.containsOfIdentity(unknownId));
    }

    @Test
    void addAuctionWithoutOutrightStoresAuction() {
        //Arrange
        when(_auctionFactoryDouble.createAuction(_itemsId1, _startingPriceDouble, _reservePriceDouble,
                null, _startDate, _endDate)).thenReturn(_auctionDouble1);

        //SUT
        _repo = new MemAuctionRepo(_auctionFactoryDouble);

        //Act
        Auction created = _repo.addAuction(_itemsId1, _startingPriceDouble, _reservePriceDouble, _startDate, _endDate);

        //Assert
        assertSame(_auctionDouble1, created);
    }

    @Test
    void addAuctionShouldStoreAuctionInRepository() {
        //SUT
        _repo = new MemAuctionRepo(_auctionFactoryDouble);

        //Act
        _repo.addAuction(_itemsId1, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        //Assert
        assertTrue(_repo.findAll().iterator().hasNext());
    }
}
