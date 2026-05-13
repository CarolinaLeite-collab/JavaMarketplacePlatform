//package MITELOVERS.controller;
//
//import MITELOVERS.domain.country.Country;
//import MITELOVERS.domain.country.CountryFactory;
//import MITELOVERS.domain.repository.ICountryRepo;
//import MITELOVERS.domain.valueobject.UserId;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//class RegisterCountryControllerTest {
//
//    private ICountryRepo _iCountryRepoDouble;
//    private CountryFactory _countryFactoryDouble;
//    private Country _countryDouble;
//    private UserId _userIdDouble;
//
//    @BeforeEach
//    void setUp() {
//        // Arrange
//        _iCountryRepoDouble = mock(ICountryRepo.class);
//        _countryFactoryDouble = mock(CountryFactory.class);
//        _countryDouble = mock(Country.class);
//        _userIdDouble = mock(UserId.class);
//    }
//
//    @Test
//    void constructsControllerSuccessfully() {
//        // Act & SUT
//        new RegisterCountryController(_iCountryRepoDouble, _countryFactoryDouble, _userIdDouble);
//    }
//
//    @Test
//    void shouldRegisterCountrySuccessfully() {
//        // Arrange
//        String countryName = "Portugal";
//        when(_countryFactoryDouble.createCountry(countryName)).thenReturn(_countryDouble);
//        when(_iCountryRepoDouble.containsOfIdentity(_countryDouble.identity())).thenReturn(false);
//        when(_iCountryRepoDouble.save(_countryDouble)).thenReturn(_countryDouble);
//
//        // SUT
//        RegisterCountryController controller = new RegisterCountryController(_iCountryRepoDouble, _countryFactoryDouble, _userIdDouble);
//
//        // Act
//        Country result = controller.registerCountry(countryName);
//
//        // Assert
//        assertSame(_countryDouble, result);
//        verify(_countryFactoryDouble, times(1)).createCountry(countryName);
//        verify(_iCountryRepoDouble, times(1)).containsOfIdentity(_countryDouble.identity());
//        verify(_iCountryRepoDouble, times(1)).save(_countryDouble);
//    }
//
//    @Test
//    void shouldThrowIllegalArgumentExceptionWhenCountryAlreadyExists() {
//        // Arrange
//        String countryName = "Portugal";
//        when(_countryFactoryDouble.createCountry(countryName)).thenReturn(_countryDouble);
//        when(_iCountryRepoDouble.containsOfIdentity(_countryDouble.identity())).thenReturn(true);
//
//        // SUT
//        RegisterCountryController controller = new RegisterCountryController(_iCountryRepoDouble, _countryFactoryDouble, _userIdDouble);
//
//        // Act
//        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
//                () -> controller.registerCountry(countryName));
//
//        // Assert
//        assertEquals("Country already exists in the repository", thrown.getMessage());
//        verify(_countryFactoryDouble, times(1)).createCountry(countryName);
//        verify(_iCountryRepoDouble, times(1)).containsOfIdentity(_countryDouble.identity());
//        verify(_iCountryRepoDouble, never()).save(any(Country.class));
//    }
//}
