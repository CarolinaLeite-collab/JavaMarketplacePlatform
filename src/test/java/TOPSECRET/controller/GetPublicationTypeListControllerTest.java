package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.PublicationTypeFactory;
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

        GetPublicationTypeListController controller = new GetPublicationTypeListController(repo);

        // Act
        List<PublicationType> result = controller.getListOfPublicationTypes();

        // Assert
        assertTrue(List.of(publicationType).equals(result));
    }

    @Test
    void shouldReturnEmptyListWhenNoPublicationTypesExist() {
        // Arrange
        PublicationTypeRepo repo = mock(PublicationTypeRepo.class);

        when(repo.getAll()).thenReturn(List.of());

        GetPublicationTypeListController controller = new GetPublicationTypeListController(repo);

        // Act
        List<PublicationType> result = controller.getListOfPublicationTypes();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void returnedListShouldNotBeModifiable() {
        PublicationTypeRepo repo = mock(PublicationTypeRepo.class);
        PublicationType publicationType = mock(PublicationType.class);

        when(repo.getAll()).thenReturn(List.of(publicationType));

        GetPublicationTypeListController controller = new GetPublicationTypeListController(repo);

        List<PublicationType> result = controller.getListOfPublicationTypes();

        assertThrows(UnsupportedOperationException.class, () -> result.clear());

    }
}
