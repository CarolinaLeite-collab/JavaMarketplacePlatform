package TOPSECRET.domain;

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
        Email emailDouble = mock(Email.class);

        // SUT
        UserFactory userFactory = new UserFactory();

        try (MockedConstruction<User> mockedConstruction = mockConstruction(User.class)) {

            // Act
            User userResult = userFactory.createUser(nameDouble, emailDouble);

            // Assert
            assertNotNull(userResult);
            assertEquals(1, mockedConstruction.constructed().size());
        }
    }

    @Test
    void shouldCreateUserTypeB() {

        //Arrange
        Name nameDouble = mock(Name.class);
        Email emailDouble = mock(Email.class);
        Address addressDouble = mock(Address.class);
        Phone phoneDouble = mock(Phone.class);

        // SUT
        UserFactory userFactory = new UserFactory();

        try (MockedConstruction<User> mockedConstruction = mockConstruction(User.class)) {

            //Act
            User userResult = userFactory.createUser(nameDouble, addressDouble, emailDouble, phoneDouble);

            //Assert
            assertNotNull(userResult);
            assertEquals(1, mockedConstruction.constructed().size());
        }
    }
}