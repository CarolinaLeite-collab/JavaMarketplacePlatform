package TOPSECRET.controller;

import TOPSECRET.domain.IPublicationTypeRepo;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.Role;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class AddPublicationTypeControllerTest {

    private IPublicationTypeRepo _iPublicationTypeRepoDouble;
    private PublicationType _pubTypeDouble;
    private User _adminDouble;

    @BeforeEach
    void setUp() throws InstantiationException {
        _iPublicationTypeRepoDouble = mock(IPublicationTypeRepo.class);

        _pubTypeDouble = mock(PublicationType.class);
        when(_iPublicationTypeRepoDouble.addPublicationType("book")).thenReturn(_pubTypeDouble);

        _adminDouble = mock(User.class);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

    }

    @Test
    void constructorAddPublicationTypeControllerShouldCreateController() {

        //SUT
        new  AddPublicationTypeController(_iPublicationTypeRepoDouble, _adminDouble);

    }

    @Test
    void addPublicationTypeThrowsWhenUserIsNotAdmin() {
        //arrange
        User adminDouble = mock(User.class);
        when(adminDouble.hasRole(Role.ADMIN)).thenReturn(false);

        String publicationTypeName = "book";

        //act + assert
        assertThrows(SecurityException.class, () -> new AddPublicationTypeController(_iPublicationTypeRepoDouble, adminDouble)); //SUT
    }

    @Test
    void addPublicationTypeToRepoAndReturnsCreatedType() {
        //arrange
        String publicationTypeName = "book";

        //SUT
        AddPublicationTypeController _addPublicationTypeController = new AddPublicationTypeController(_iPublicationTypeRepoDouble, _adminDouble);

        //act

        PublicationType pubTypeResult = _addPublicationTypeController.addPublicationType(publicationTypeName);

        //assert
        assertEquals(_pubTypeDouble, pubTypeResult);
    }

    @Test
    void addPublicationTypeThrowsWhenTypeAlreadyExists() {

        //arrange
        String publicationTypeName = "book";
        when(_iPublicationTypeRepoDouble.addPublicationType("book"))
            .thenThrow(new IllegalArgumentException("This publication type already exists!"));

        //SUT
        AddPublicationTypeController _addPublicationTypeController = new AddPublicationTypeController(_iPublicationTypeRepoDouble, _adminDouble);

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> _addPublicationTypeController.addPublicationType(publicationTypeName));
    }
}
