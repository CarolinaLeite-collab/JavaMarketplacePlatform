package TOPSECRET.controller;

import TOPSECRET.domain.publicationtype.PublicationType;
import TOPSECRET.domain.repository.IPublicationTypeRepo;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetPublicationTypeListControllerTest {
        private UserId _userIdDouble;

    @BeforeEach
    void setUp() {
        _userIdDouble = mock(UserId.class);
//        when(_userIdDouble.hasRole(Role.USER)).thenReturn(true);
    }

    @Test
    void shouldReturnAllPublicationTypes() {
        // Arrange
        IPublicationTypeRepo _iPublicationTypeRepoDouble = mock(IPublicationTypeRepo.class);
        PublicationType publicationType = mock(PublicationType.class);
        when(_iPublicationTypeRepoDouble.getAll()).thenReturn(List.of(publicationType));
        //SUT
        GetPublicationTypeListController controller = new GetPublicationTypeListController(_iPublicationTypeRepoDouble, _userIdDouble);
        // Act
        List<PublicationType> result = controller.getListOfPublicationTypes();
        // Assert
        assertTrue(List.of(publicationType).equals(result));
    }

    @Test
    void shouldReturnEmptyListWhenNoPublicationTypesExist() {
        //Arrange
        IPublicationTypeRepo _iPublicationTypeRepoDouble = mock(IPublicationTypeRepo.class);
        when(_iPublicationTypeRepoDouble.getAll()).thenReturn(List.of());
        //SUT
        GetPublicationTypeListController controller = new GetPublicationTypeListController(_iPublicationTypeRepoDouble, _userIdDouble);
        //Act
        List<PublicationType> result = controller.getListOfPublicationTypes();
        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void returnedListShouldNotBeModifiable() {
        //Arrange
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        IPublicationTypeRepo _iPublicationTypeRepoDouble = mock(IPublicationTypeRepo.class);
        when(_iPublicationTypeRepoDouble.getAll()).thenReturn(List.of(publicationTypeDouble));
        //SUT
        GetPublicationTypeListController controller = new GetPublicationTypeListController(_iPublicationTypeRepoDouble, _userIdDouble);
        //Act
        List<PublicationType> result = controller.getListOfPublicationTypes();
        //Assert
        assertThrows(UnsupportedOperationException.class, () -> result.clear());

    }
}
