package TOPSECRET.controller;

import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class GetDirectSaleItemsByAuthorControllerTest {

    private UserId _userIdDouble;
    private IDirectSaleRepo _iDirectSaleRepoDouble;

    @BeforeEach
    void setUp() {

        _userIdDouble = mock(UserId.class);
        _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);

    }

    @Test
    void testAConstructor(){

        //act / SUT
        new GetDirectSaleItemsByAuthorController(_iDirectSaleRepoDouble, _userIdDouble);

    }



}