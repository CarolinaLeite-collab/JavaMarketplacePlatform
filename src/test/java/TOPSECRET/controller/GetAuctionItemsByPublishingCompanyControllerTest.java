package TOPSECRET.controller;

import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAuctionItemsByPublishingCompanyControllerTest {

    private UserId _buyerIdDouble;
    private IAuctionRepo _iAuctionRepoDouble;

    private Item _item1Double;
    private Item _item2Double;

    @BeforeEach
    void setUp() {
        _buyerIdDouble = mock(UserId.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);

        _item1Double = mock(Item.class);
        _item2Double = mock(Item.class);
    }

    @Test
    void constructorWithValidDependenciesDoesNotThrow() {
        assertDoesNotThrow(() ->
                new GetAuctionItemsByPublishingCompanyController(_iAuctionRepoDouble, _buyerIdDouble));
    }
}