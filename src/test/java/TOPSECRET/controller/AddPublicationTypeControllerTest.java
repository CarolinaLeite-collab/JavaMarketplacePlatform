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
    void addPublicationTypeToRepoAndReturnsCreatedType() {
        //arrange
        User admin = mock(User.class);
        when(admin.hasRole(Role.ADMIN)).thenReturn(true);

        String publicationTypeName = "book";

        //SUT
        AddPublicationTypeController controller = new AddPublicationTypeController(_ptrDouble);

        //act

        PublicationType pubTypeResult = controller.addPublicationType(publicationTypeName, admin);

        //assert
        assertEquals(_pubTypeDouble, pubTypeResult);
        verify(_ptrDouble).addPublicationType("book");

    }

    @Test
    void addPublicationTypeThrowsWhenTypeAlreadyExists() {

        //arrange

        User admin = mock(User.class);
        when(admin.hasRole(Role.ADMIN)).thenReturn(true);

        String publicationTypeName = "book";
        when(_ptrDouble.addPublicationType("book"))
            .thenThrow(new IllegalArgumentException("This publication type already exists!"));

        //SUT
        AddPublicationTypeController controller = new AddPublicationTypeController(_ptrDouble);

        //act + assert
        assertThrows(IllegalArgumentException.class, () -> controller.addPublicationType(publicationTypeName, admin));
    }

    @Test
    void addPublicationTypeThrowsWhenUserIsNotAdmin() {
        //arrange
        User admin = mock(User.class);
        when(admin.hasRole(Role.ADMIN)).thenReturn(false);

        String publicationTypeName = "book";

        //SUT
        AddPublicationTypeController controller = new AddPublicationTypeController(_ptrDouble);

        //act + assert
        assertThrows(SecurityException.class, () -> controller.addPublicationType(publicationTypeName, admin));
    }
}
