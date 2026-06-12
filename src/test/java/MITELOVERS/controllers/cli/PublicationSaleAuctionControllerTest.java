package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.AuctionService;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicationSaleAuctionControllerTest {

    @Mock
    private AuctionService _service;

    @InjectMocks
    private PublicationSaleAuctionController _publicationSaleAuctionController;

    @Mock
    private ItemId _itemIdDouble;

    @Mock
    private Auction _auctionDouble;

    @Mock
    private Price _startingPriceDouble;

    @Mock
    private Price _reservePriceDouble;

    @Mock
    private Price _outrightPriceDouble;

    private List<ItemId> _itemsId;
    private ZonedDateTime _startDate;
    private ZonedDateTime _endDate;

    @BeforeEach
    void setUp() {
        _itemsId = List.of(_itemIdDouble);
        _startDate = ZonedDateTime.now();
        _endDate = _startDate.plusDays(7);
    }

    @Test
    void testConstructor() {
        // SUT
        new PublicationSaleAuctionController(_service);
    }

    @Test
    void shouldPutItemOnAuctionWithOutrightPrice() {

        // Arrange
        when(_service.putItemOnAuction(
                _itemsId,
                _startingPriceDouble,
                _reservePriceDouble,
                _outrightPriceDouble,
                _startDate,
                _endDate))
                .thenReturn(_auctionDouble);

        // SUT
        new PublicationSaleAuctionController(_service);

        // Act
        Auction result =
                _publicationSaleAuctionController.putItemOnAuction(
                        _itemsId,
                        _startingPriceDouble,
                        _reservePriceDouble,
                        _outrightPriceDouble,
                        _startDate,
                        _endDate);

        // Assert
        assertSame(_auctionDouble, result);

        verify(_service).putItemOnAuction(
                _itemsId,
                _startingPriceDouble,
                _reservePriceDouble,
                _outrightPriceDouble,
                _startDate,
                _endDate);
    }

    @Test
    void shouldPutItemOnAuctionWithoutOutrightPrice() {

        // Arrange
        when(_service.putItemOnAuction(
                _itemsId,
                _startingPriceDouble,
                _reservePriceDouble,
                _startDate,
                _endDate))
                .thenReturn(_auctionDouble);

        // SUT
        new PublicationSaleAuctionController(_service);

        // Act
        Auction result =
                _publicationSaleAuctionController.putItemOnAuction(
                        _itemsId,
                        _startingPriceDouble,
                        _reservePriceDouble,
                        _startDate,
                        _endDate);

        // Assert
        assertSame(_auctionDouble, result);

        verify(_service).putItemOnAuction(
                _itemsId,
                _startingPriceDouble,
                _reservePriceDouble,
                _startDate,
                _endDate);
    }
}
