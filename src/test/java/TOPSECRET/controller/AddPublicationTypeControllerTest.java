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
    private AddPublicationTypeController _addPublicationTypeController;

    @BeforeEach
    void setUp() throws InstantiationException {
        _ptrDouble = mock(PublicationTypeRepo.class);
        _pubTypeDouble = mock(PublicationType.class);
        when(_ptrDouble.addPublicationType("book")).thenReturn(_pubTypeDouble);

    }

    @Test
    void addPublicationTypeToRepoAndReturnsCreatedType() {
        //arrange
        User adminDouble = mock(User.class);
        when(adminDouble.hasRole(Role.ADMIN)).thenReturn(true);

        String publicationTypeName = "book";

        //SUT
        _addPublicationTypeController = new AddPublicationTypeController(_ptrDouble, adminDouble);

        //act

        PublicationType pubTypeResult = _addPublicationTypeController.addPublicationType(publicationTypeName);

        //assert
        assertEquals(_pubTypeDouble, pubTypeResult);
        verify(_ptrDouble).addPublicationType("book");

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
       _addPublicationTypeController = new AddPublicationTypeController(_ptrDouble, adminDouble);

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> _addPublicationTypeController.addPublicationType(publicationTypeName));
    }

    @Test
    void addPublicationTypeThrowsWhenUserIsNotAdmin() {
        //arrange
        User adminDouble = mock(User.class);
        when(adminDouble.hasRole(Role.ADMIN)).thenReturn(false);

        String publicationTypeName = "book";

        //act + assert
        assertThrows(SecurityException.class, () -> _addPublicationTypeController = new AddPublicationTypeController(_ptrDouble, adminDouble)); //SUT
    }
}
