package TOPSECRET.controller;

import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class GetAuctionItemsByPublicationControllerTest {

    private UserId _buyerIdDouble;
    private IAuctionRepo _iAuctionRepoDouble;


    @BeforeEach
    void setUp() {

        _buyerIdDouble = mock(UserId.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
    }

    @Test
    void testAuctionItemsByPublicationController(){

        // SUT
        new GetAuctionItemsByGenreController(_iAuctionRepoDouble, _buyerIdDouble);
    }
}