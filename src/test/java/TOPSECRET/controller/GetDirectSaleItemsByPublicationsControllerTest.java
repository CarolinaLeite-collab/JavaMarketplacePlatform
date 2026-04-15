package TOPSECRET.controller;

import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GetDirectSaleItemsByPublicationsControllerTest {

    private UserId _buyerIdDouble;
    private IDirectSaleRepo _iDirectSaleRepoDouble;

    @BeforeEach
    void setUp(){
            _buyerIdDouble = mock(UserId.class);
            _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);
    }

    @Test
    void testDirectSaleItemsByPublicationControllerConstructor(){

        //SUT
        new GetDirectSaleItemsByPublicationsController(_iDirectSaleRepoDouble, _buyerIdDouble);

    }

}
