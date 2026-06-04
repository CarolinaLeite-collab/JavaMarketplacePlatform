package MITELOVERS.applicationservices;

import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.publishingcompany.PublishingCompanyFactory;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import MITELOVERS.dto.request.PublishingCompanyRequestDTO;
import MITELOVERS.dto.response.PublishingCompanyResponseDTO;
import MITELOVERS.mapper.PublishingCompanyResponseDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublishingCompanyServiceTest {

    private PublishingCompanyFactory _publishingCompanyFactoryDouble;
    private IPublishingCompanyRepo _iPublishingCompanyRepoDouble;
    private PublishingCompanyResponseDTOMapper _responseMapperDouble;

    private PublishingCompany _publishingCompanyDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;
    private PublishingCompanyResponseDTO _responseDTODouble;

    @BeforeEach
    void setUp() {
        _publishingCompanyFactoryDouble = mock(PublishingCompanyFactory.class);
        _iPublishingCompanyRepoDouble = mock(IPublishingCompanyRepo.class);
        _responseMapperDouble = mock(PublishingCompanyResponseDTOMapper.class);

        _publishingCompanyDouble = mock(PublishingCompany.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);
        _responseDTODouble = mock(PublishingCompanyResponseDTO.class);

        when(_publishingCompanyDouble.identity()).thenReturn(_publishingCompanyIdDouble);
        when(_responseMapperDouble.toModel(_publishingCompanyDouble)).thenReturn(_responseDTODouble);
    }

    @Test
    void registerPublishingCompanyNewReturnsDTO() {
        // Arrange
        PublishingCompanyRequestDTO dtoDouble = mock(PublishingCompanyRequestDTO.class);

        when(dtoDouble.toString()).thenReturn("Porto Editora");
        when(_publishingCompanyFactoryDouble.createPublishingCompany(any(String.class)))
                .thenReturn(_publishingCompanyDouble);
        when(_iPublishingCompanyRepoDouble.containsOfIdentity(_publishingCompanyIdDouble)).thenReturn(false);
        when(_iPublishingCompanyRepoDouble.save(_publishingCompanyDouble)).thenReturn(_publishingCompanyDouble);

        // SUT
        PublishingCompanyService service = new PublishingCompanyService(
                _publishingCompanyFactoryDouble,
                _iPublishingCompanyRepoDouble,
                _responseMapperDouble);

        // Act
        PublishingCompanyResponseDTO result = service.registerPublishingCompany(dtoDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void registerPublishingCompanyAlreadyExistsReturnsExisting() {
        // Arrange
        PublishingCompanyRequestDTO dtoDouble = mock(PublishingCompanyRequestDTO.class);

        when(dtoDouble.toString()).thenReturn("Porto Editora");
        when(_publishingCompanyFactoryDouble.createPublishingCompany(any(String.class)))
                .thenReturn(_publishingCompanyDouble);
        when(_iPublishingCompanyRepoDouble.containsOfIdentity(_publishingCompanyIdDouble)).thenReturn(true);

        // SUT
        PublishingCompanyService service = new PublishingCompanyService(
                _publishingCompanyFactoryDouble,
                _iPublishingCompanyRepoDouble,
                _responseMapperDouble);

        // Act
        PublishingCompanyResponseDTO result = service.registerPublishingCompany(dtoDouble);

        // Assert
        assertNotNull(result);
    }


    @Test
    void getAllPublishingCompaniesReturnsListOfDTOs() {
        // Arrange
        when(_iPublishingCompanyRepoDouble.findAll()).thenReturn(List.of(_publishingCompanyDouble));

        // SUT
        PublishingCompanyService service = new PublishingCompanyService(
                _publishingCompanyFactoryDouble,
                _iPublishingCompanyRepoDouble,
                _responseMapperDouble);

        // Act
        List<PublishingCompanyResponseDTO> result = service.getAllPublishingCompanies();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAllPublishingCompaniesEmptyReturnsEmptyList() {
        // Arrange
        when(_iPublishingCompanyRepoDouble.findAll()).thenReturn(List.of());

        // SUT
        PublishingCompanyService service = new PublishingCompanyService(
                _publishingCompanyFactoryDouble,
                _iPublishingCompanyRepoDouble,
                _responseMapperDouble);

        // Act
        List<PublishingCompanyResponseDTO> result = service.getAllPublishingCompanies();

        // Assert
        assertTrue(result.isEmpty());
    }


    @Test
    void getPublishingCompanyByIdReturns() {
        // Arrange
        when(_iPublishingCompanyRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_publishingCompanyDouble));

        // SUT
        PublishingCompanyService service = new PublishingCompanyService(
                _publishingCompanyFactoryDouble,
                _iPublishingCompanyRepoDouble,
                _responseMapperDouble);

        // Act
        PublishingCompany result = service.getPublishingCompanyById("PORTO EDITORA");

        // Assert
        assertNotNull(result);
    }

    @Test
    void getPublishingCompanyByIdNotFoundThrowsException() {
        // Arrange
        when(_iPublishingCompanyRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // SUT
        PublishingCompanyService service = new PublishingCompanyService(
                _publishingCompanyFactoryDouble,
                _iPublishingCompanyRepoDouble,
                _responseMapperDouble);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                service.getPublishingCompanyById("UNKNOWN"));
    }

}