package TOPSECRET.controller;

import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetItemsOnAuctionOfAGivenAuthorControllerTest {

    private UserId _buyerIdDouble;
    private IAuctionRepo _iAuctionRepoDouble;

    @BeforeEach
    void setUp() {
        _buyerIdDouble = mock(UserId.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
    }

    @Test
    void testAConstructor() {

        //SUT
        new GetItemsOnAuctionOfAGivenAuthorController(_iAuctionRepoDouble, _buyerIdDouble);

    }
}
