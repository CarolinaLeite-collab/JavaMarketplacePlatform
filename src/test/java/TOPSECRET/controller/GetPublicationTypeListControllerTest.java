package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.PublicationTypeRepo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetPublicationTypeListControllerTest {

    @Test
    void shouldReturnAllPublicationTypes() {
        // Arrange
        PublicationTypeRepo repo = mock(PublicationTypeRepo.class);
        PublicationType publicationType = mock(PublicationType.class);
        when(repo.getAll()).thenReturn(List.of(publicationType));
        //SUT
        GetPublicationTypeListController controller = new GetPublicationTypeListController(repo);
        // Act
        List<PublicationType> result = controller.getListOfPublicationTypes();
        // Assert
        assertTrue(List.of(publicationType).equals(result));
    }

    @Test
    void shouldReturnEmptyListWhenNoPublicationTypesExist() {
        //Arrange
        PublicationTypeRepo repoDouble = mock(PublicationTypeRepo.class);
        when(repoDouble.getAll()).thenReturn(List.of());
        //SUT
        GetPublicationTypeListController controller = new GetPublicationTypeListController(repoDouble);
        //Act
        List<PublicationType> result = controller.getListOfPublicationTypes();
        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void returnedListShouldNotBeModifiable() {
        //Arrange
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        PublicationTypeRepo repoDouble = mock(PublicationTypeRepo.class);
        when(repoDouble.getAll()).thenReturn(List.of(publicationTypeDouble));
        //SUT
        GetPublicationTypeListController controller = new GetPublicationTypeListController(repoDouble);
        //Act
        List<PublicationType> result = controller.getListOfPublicationTypes();
        //Assert
        assertThrows(UnsupportedOperationException.class, () -> result.clear());

    }
}
