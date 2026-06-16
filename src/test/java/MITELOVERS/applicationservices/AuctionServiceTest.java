package MITELOVERS.applicationservices;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.AuctionFactory;
import MITELOVERS.domain.auction.Bid;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IAuctionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    //SUT
    @InjectMocks
    AuctionService _auctionService;

    @Mock
    IAuctionRepo _iAuctionRepoDouble;

    @Mock
    AuctionFactory _auctionFactoryDouble;

    @Mock
    IItemRepo _iItemRepoDouble;

    private List<ItemId> _itemsId;
    private ItemId _itemIdDouble;
    private Price _startingPriceDouble;
    private Price _reservePriceDouble;
    private Price _outrightPriceDouble;
    private Auction _auctionDouble;
    private Item _itemDouble;
    private ZonedDateTime _startDate;
    private ZonedDateTime _endDate;

    @BeforeEach
    void setUp() {
        _itemIdDouble = mock(ItemId.class);
        _itemsId = new ArrayList<>();
        _itemsId.add(_itemIdDouble);
        _startingPriceDouble = mock(Price.class);
        _reservePriceDouble = mock(Price.class);
        _outrightPriceDouble = mock(Price.class);
        _auctionDouble = mock(Auction.class);
        _itemDouble = mock(Item.class);
        _startDate = ZonedDateTime.now().plusDays(1);
        _endDate = ZonedDateTime.now().plusDays(2);
    }

    @Test
    void testPutItemIdOnAuctionSuccess() {
        // Arrange
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble))
                .thenReturn(Optional.of(_itemDouble));

        when(_itemDouble.getSaleStatus())
                .thenReturn(SaleStatus.NotOnSale);

        when(_auctionFactoryDouble.createAuction(_itemsId, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble,
                _startDate, _endDate)).thenReturn(_auctionDouble);

        when(_iAuctionRepoDouble.save(any())).thenReturn(_auctionDouble);

        // Act
        Auction result = _auctionService.putItemOnAuction(
                _itemsId, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startDate, _endDate);

        // Assert
        assertNotNull(result);
        assertSame(_auctionDouble, result);
    }

    @Test
    void testPutItemIdOnAuctionWithoutOutrightPriceSuccess() {
        // Arrange
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble))
                .thenReturn(Optional.of(_itemDouble));

        when(_itemDouble.getSaleStatus())
                .thenReturn(SaleStatus.NotOnSale);

        when(_auctionFactoryDouble.createAuction(_itemsId, _startingPriceDouble, _reservePriceDouble, null,
                _startDate, _endDate)).thenReturn(_auctionDouble);

        when(_iAuctionRepoDouble.save(any())).thenReturn(_auctionDouble);

        // Act
        Auction result = _auctionService.putItemOnAuction(
                _itemsId, _startingPriceDouble, _reservePriceDouble, _startDate, _endDate);

        // Assert
        assertNotNull(result);
        assertSame(_auctionDouble, result);
    }

    @Test
    void testPutItemOnAuctionWhenItemDoesNotExist() {
        // Arrange
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> _auctionService.putItemOnAuction(
                        _itemsId, _startingPriceDouble, _reservePriceDouble,
                        _outrightPriceDouble, _startDate, _endDate)
        );

        assertTrue(exception.getMessage().contains("Item not found"));
    }

    @Test
    void testPutItemOnAuctionWhenItemIsAlreadyOnSale() {
        // Arrange
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getSaleStatus()).thenReturn(SaleStatus.OnAuction);

        // Act + Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> _auctionService.putItemOnAuction(
                        _itemsId, _startingPriceDouble, _reservePriceDouble,
                        _outrightPriceDouble, _startDate, _endDate)
        );

        assertTrue(exception.getMessage().contains("already on sale"));
    }

    @Test
    void getAuctionByIdReturnsAuctionWhenFound() {
        // Arrange
        String rawAuctionId = "AU-12345678";

        when(_iAuctionRepoDouble.ofIdentity(any(AuctionId.class)))
                .thenReturn(Optional.of(_auctionDouble));

        // Act
        Auction result = _auctionService.getAuctionById(rawAuctionId);

        // Assert
        assertSame(_auctionDouble, result);
    }

    @Test
    void getAuctionByIdThrowsWhenNotFound() {
        // Arrange
        String rawAuctionId = "AU-12345678";

        when(_iAuctionRepoDouble.ofIdentity(any(AuctionId.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> _auctionService.getAuctionById(rawAuctionId));
    }

    @Test
    void placeBidShouldReturnResultAndSaveAuction() {
        // Arrange
        String rawAuctionId = "AU-12345678";
        UserId userIdDouble = mock(UserId.class);
        Price offerPriceDouble = mock(Price.class);

        Bid bidDouble = mock(Bid.class);

        when(_iAuctionRepoDouble.ofIdentity(any(AuctionId.class)))
                .thenReturn(Optional.of(_auctionDouble));

        when(_auctionDouble.placeBid(userIdDouble, offerPriceDouble)).thenReturn(bidDouble);

        when(_iAuctionRepoDouble.save(_auctionDouble)).thenReturn(_auctionDouble);

        // Act
        AuctionService.BidPlacementResult result =
                _auctionService.placeBid(rawAuctionId, userIdDouble, offerPriceDouble);

        // Assert
        assertNotNull(result);
        assertSame(_auctionDouble, result.auction());
        assertSame(bidDouble, result.bid());
    }

    @Test
    void placeBidShouldThrowWhenAuctionNotFound() {
        // Arrange
        String rawAuctionId = "AU-12345678";
        UserId userId = mock(UserId.class);
        Price offerPrice = mock(Price.class);

        when(_iAuctionRepoDouble.ofIdentity(any(AuctionId.class)))
                .thenReturn(Optional.empty());

        // Act + Assert
        NoSuchElementException ex = assertThrows(
                NoSuchElementException.class,
                () -> _auctionService.placeBid(rawAuctionId, userId, offerPrice)
        );

        assertTrue(ex.getMessage().contains("Auction not found"));
    }

}