package MITELOVERS.controller;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class AddPublicationTypeControllerTest {

    private IPublicationTypeRepo _iPublicationTypeRepoDouble;
    private PublicationType _pubTypeDouble;
    private UserId _adminDoubleId;

    @BeforeEach
    void setUp() throws InstantiationException {
        _iPublicationTypeRepoDouble = mock(IPublicationTypeRepo.class);
        _adminDoubleId = mock(UserId.class);

    }

    @Test
    void constructorAddPublicationTypeControllerShouldCreateController() {

        //SUT
        new  AddPublicationTypeController(_iPublicationTypeRepoDouble, _adminDoubleId);

    }


    @Test
    void addPublicationTypeToRepoAndReturnsCreatedType() {
        //arrange
        String publicationTypeName = "book";
        _pubTypeDouble = mock(PublicationType.class);
        when(_iPublicationTypeRepoDouble.addPublicationType(publicationTypeName)).thenReturn(_pubTypeDouble);

        //SUT
        AddPublicationTypeController _addPublicationTypeController = new AddPublicationTypeController(_iPublicationTypeRepoDouble, _adminDoubleId);

        //act

        PublicationType pubTypeResult = _addPublicationTypeController.addPublicationType(publicationTypeName);

        //assert
        assertEquals(_pubTypeDouble, pubTypeResult);
    }

    @Test
    void addPublicationTypeThrowsWhenTypeAlreadyExists() {

        //arrange
        String publicationTypeName = "book";
        when(_iPublicationTypeRepoDouble.addPublicationType(publicationTypeName))
            .thenThrow(new IllegalArgumentException("This publication type already exists!"));

        //SUT
        AddPublicationTypeController _addPublicationTypeController = new AddPublicationTypeController(_iPublicationTypeRepoDouble, _adminDoubleId);

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> _addPublicationTypeController.addPublicationType(publicationTypeName));
    }
}
