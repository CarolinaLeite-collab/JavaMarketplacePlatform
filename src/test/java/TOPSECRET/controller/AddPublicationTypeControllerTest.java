package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.PublicationTypeRepo;
import TOPSECRET.domain.Role;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AddPublicationTypeControllerTest {

    private PublicationTypeRepo _ptrDouble;
    private PublicationType _pubTypeDouble;

    @BeforeEach
    void setUp() throws InstantiationException {
        _ptrDouble = mock(PublicationTypeRepo.class);
        _pubTypeDouble = mock(PublicationType.class);
        when(_ptrDouble.addPublicationType("book")).thenReturn(_pubTypeDouble);

    }

    @Test
    void constructorAddPublicationTypeControllerShouldCreateController() {

        User adminDouble = mock(User.class);
        when(adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        //SUT
        new  AddPublicationTypeController(_ptrDouble, adminDouble);

    }

    @Test
    void addPublicationTypeThrowsWhenUserIsNotAdmin() {
        //arrange
        User adminDouble = mock(User.class);
        when(adminDouble.hasRole(Role.ADMIN)).thenReturn(false);

        String publicationTypeName = "book";

        //act + assert
        assertThrows(SecurityException.class, () -> new AddPublicationTypeController(_ptrDouble, adminDouble)); //SUT
    }

    @Test
    void addPublicationTypeToRepoAndReturnsCreatedType() {
        //arrange
        User adminDouble = mock(User.class);
        when(adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        String publicationTypeName = "book";

        //SUT
        AddPublicationTypeController _addPublicationTypeController = new AddPublicationTypeController(_ptrDouble, adminDouble);

        //act

        PublicationType pubTypeResult = _addPublicationTypeController.addPublicationType(publicationTypeName);

        //assert
        assertEquals(_pubTypeDouble, pubTypeResult);
    }

    @Test
    void addPublicationTypeThrowsWhenTypeAlreadyExists() {

        //arrange

        User adminDouble = mock(User.class);
        when(adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        String publicationTypeName = "book";
        when(_ptrDouble.addPublicationType("book"))
            .thenThrow(new IllegalArgumentException("This publication type already exists!"));

        //SUT
        AddPublicationTypeController _addPublicationTypeController = new AddPublicationTypeController(_ptrDouble, adminDouble);

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> _addPublicationTypeController.addPublicationType(publicationTypeName));
    }
}
