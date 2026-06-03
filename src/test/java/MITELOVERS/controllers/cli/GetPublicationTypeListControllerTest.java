package MITELOVERS.controllers.cli;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.repository.IPublicationTypeRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class GetPublicationTypeListControllerTest {

    @Mock
    private IPublicationTypeRepo _iPublicationTypeRepoDouble;

    @Mock
    private PublicationType _publicationTypeDouble;

    @Mock
    private PublicationType _publicationTypeDouble2;

    // SUT
    @InjectMocks
    private GetPublicationTypeListController _controller;


    @Test
    void shouldReturnAllPublicationTypes() {

        // Arrange
        when(_iPublicationTypeRepoDouble.findAll()).thenReturn(List.of(_publicationTypeDouble));

        // Act
        List<PublicationType> result = new ArrayList<>();
        _controller.getListOfPublicationTypes().forEach(result::add);

        // Assert
        assertEquals(1, result.size());
        assertEquals(_publicationTypeDouble, result.get(0));

    }

    @Test
    void shouldReturnEmptyListWhenNoPublicationTypesExist() {

        // Arrange
        when(_iPublicationTypeRepoDouble.findAll()).thenReturn(List.of());

        // Act
        List<PublicationType> result = new ArrayList<>();
        _controller.getListOfPublicationTypes().forEach(result::add);

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void shouldReturnMultiplePublicationTypes() {

        // Arrange

        when(_iPublicationTypeRepoDouble.findAll()).thenReturn(List.of(_publicationTypeDouble, _publicationTypeDouble2));

        // Act
        List<PublicationType> result = new ArrayList<>();
        _controller.getListOfPublicationTypes().forEach(result::add);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(_publicationTypeDouble));
        assertTrue(result.contains(_publicationTypeDouble2));

    }

}