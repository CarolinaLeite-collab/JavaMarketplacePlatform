package TOPSECRET.controller;

import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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