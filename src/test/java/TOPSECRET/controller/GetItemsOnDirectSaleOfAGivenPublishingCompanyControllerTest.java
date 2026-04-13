package TOPSECRET.controller;

import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class GetItemsOnDirectSaleOfAGivenPublishingCompanyControllerTest {
    private IDirectSaleRepo _iDirectSaleRepoDouble;
    private UserId _buyerIdDouble;

    @BeforeEach
    void setUp() {
        _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);
        _buyerIdDouble = mock(UserId.class);
    }

    @Test
    void constructorShouldSuccessfullyGetItemsOnDirectSaleOfAGivenPublishingCompany(){
        //Act /SUT
        new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_iDirectSaleRepoDouble, _buyerIdDouble);
    }

}