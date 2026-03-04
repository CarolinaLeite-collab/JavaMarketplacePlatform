package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.PublicationTypeRepo;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AddPublicationTypeControllerTest {

    private User _admin;
    private PublicationTypeRepo _ptrDouble;
    private PublicationType _pubTypeDouble;

    @BeforeEach
    void setUp() {

        _admin = mock (User.class);
        _ptrDouble = mock(PublicationTypeRepo.class);
        _pubTypeDouble = mock(PublicationType.class);
        when(_ptrDouble.addPublicationType("book")).thenReturn(_pubTypeDouble);

    }

    @Test
    void addPublicationTypeToRepoAndReturnsCreatedType() {

        //arrange
        String publicationTypeName = "book";

        //SUT
        AddPublicationTypeController controller = new AddPublicationTypeController(_ptrDouble, _admin);

        //act

        PublicationType pubTypeResult = controller.addPublicationType(publicationTypeName);

        //assert
        assertEquals(_pubTypeDouble, pubTypeResult);
        verify(_ptrDouble).addPublicationType("book");

    }

    @Test
    void addPublicationTypeThrowsWhenTypeAlreadyExists() {

        //Arrange
        String publicationTypeName = "book";
        when(_ptrDouble.addPublicationType("book"))
            .thenThrow(new IllegalArgumentException("This publication type already exists!"));

        //SUT
        AddPublicationTypeController controller = new AddPublicationTypeController(_ptrDouble, _admin);

        //act and assert
        assertThrows(IllegalArgumentException.class, () -> controller.addPublicationType(publicationTypeName));

    }
}
