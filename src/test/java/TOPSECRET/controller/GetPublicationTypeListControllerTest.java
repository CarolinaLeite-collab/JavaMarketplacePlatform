package TOPSECRET.controller;

import TOPSECRET.domain.repository.IPublicationTypeRepo;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.valueobject.Role;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetPublicationTypeListControllerTest {
        private User _userDouble;

    @BeforeEach
    void setUp() {
        _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.USER)).thenReturn(true);
    }

    @Test
    void shouldReturnAllPublicationTypes() {
        // Arrange
        IPublicationTypeRepo _iPublicationTypeRepoDouble = mock(IPublicationTypeRepo.class);
        PublicationType publicationType = mock(PublicationType.class);
        when(_iPublicationTypeRepoDouble.getAll()).thenReturn(List.of(publicationType));
        //SUT
        GetPublicationTypeListController controller = new GetPublicationTypeListController(_iPublicationTypeRepoDouble, _userDouble);
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
        GetPublicationTypeListController controller = new GetPublicationTypeListController(_iPublicationTypeRepoDouble, _userDouble);
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
        GetPublicationTypeListController controller = new GetPublicationTypeListController(_iPublicationTypeRepoDouble, _userDouble);
        //Act
        List<PublicationType> result = controller.getListOfPublicationTypes();
        //Assert
        assertThrows(UnsupportedOperationException.class, () -> result.clear());

    }
}
