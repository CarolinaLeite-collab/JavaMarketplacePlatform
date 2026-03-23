package TOPSECRET.domain;

import TOPSECRET.ddd.ValueObject;
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
        ValueObject.Name nameDouble = mock(ValueObject.Name.class);
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
        ValueObject.Name nameDouble = mock(ValueObject.Name.class);
        Email emailDouble = mock(Email.class);
        Address addressDouble = mock(Address.class);
        Phone phoneDouble = mock(Phone.class);

        // SUT
        UserFactory userFactory = new UserFactory();

        try (MockedConstruction<User> mockedConstruction = mockConstruction(User.class)) {

            //Act
            User userTypeB_Result = userFactory.createUserTypeB(nameDouble, addressDouble, emailDouble, phoneDouble);

            //Assert
            assertNotNull(userTypeB_Result);
            assertEquals(1, mockedConstruction.constructed().size());
        }
    }
}