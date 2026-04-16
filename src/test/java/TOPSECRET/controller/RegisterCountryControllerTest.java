package TOPSECRET.controller;

import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.repository.ICountryRepo;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegisterCountryControllerTest {

    private ICountryRepo _iCountryRepoDouble;
    private Country _countryDouble;
    private UserId _userIdDouble;

    @BeforeEach
    void setUp() {
        // Arrange
        _iCountryRepoDouble = mock(ICountryRepo.class);
        _countryDouble = mock(Country.class);
        _userIdDouble = mock(UserId.class);
    }

    @Test
    void constructsControllerSuccessfully() {
        // Act & SUT
        new RegisterCountryController(_iCountryRepoDouble, _userIdDouble);
    }

    @Test
    void shouldRegisterCountrySuccessfully() {
        // Arrange
        String countryName = "Portugal";
        when(_iCountryRepoDouble.addCountry(countryName)).thenReturn(_countryDouble);

        // SUT
        RegisterCountryController controller = new RegisterCountryController(_iCountryRepoDouble, _userIdDouble);

        // Act
        Country result = controller.registerCountry(countryName);

        // Assert
        assertSame(_countryDouble, result);
        verify(_iCountryRepoDouble, times(1)).addCountry(countryName);
    }

    @Test
    void shouldPropagateExceptionWhenRepositoryFails() {
        // Arrange
        String countryName = "Portugal";
        RuntimeException expectedException = new RuntimeException("repository failure");
        when(_iCountryRepoDouble.addCountry(countryName)).thenThrow(expectedException);

        // SUT
        RegisterCountryController controller = new RegisterCountryController(_iCountryRepoDouble, _userIdDouble);

        // Act
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> controller.registerCountry(countryName));

        // Assert
        assertSame(expectedException, thrown);
        verify(_iCountryRepoDouble, times(1)).addCountry(countryName);
    }
}