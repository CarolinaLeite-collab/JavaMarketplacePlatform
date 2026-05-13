package MITELOVERS.domain.user;

import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class UserFactoryTest {

    @Test
    void shouldCreateUser() {
        // Arrange
        Name nameDouble = mock(Name.class);
        Address addressDouble = mock(Address.class);
        Email emailDouble = mock(Email.class);
        Phone phoneDouble = mock(Phone.class);


        // SUT
        UserFactory userFactory = new UserFactory();

        // Act
        try (MockedConstruction<User> mockedConstruction = mockConstruction(User.class)) {

            User userResult = userFactory.createUser(nameDouble, addressDouble, emailDouble, phoneDouble);

            // Assert
            assertNotNull(userResult);
            assertEquals(1, mockedConstruction.constructed().size());
        }
    }
    @Test
    void shouldReconstituteUser() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        Name nameDouble = mock(Name.class);
        Address addressDouble = mock(Address.class);
        Email emailDouble = mock(Email.class);
        Phone phoneDouble = mock(Phone.class);

        // SUT
        UserFactory userFactory = new UserFactory();

        // Act
        try (MockedConstruction<User> mockedConstruction = mockConstruction(User.class)) {


            User userResult = userFactory.createUser(userIdDouble, nameDouble, addressDouble, emailDouble, phoneDouble);

            // Assert
            assertNotNull(userResult);
            assertEquals(1, mockedConstruction.constructed().size());
        }
    }
}
