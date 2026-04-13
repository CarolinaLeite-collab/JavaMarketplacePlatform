package TOPSECRET.domain.user;

import TOPSECRET.domain.valueobject.Address;
import TOPSECRET.domain.valueobject.Email;
import TOPSECRET.domain.valueobject.Name;
import TOPSECRET.domain.valueobject.Phone;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class UserFactoryTest {

    @Test
    void shouldCreateUser() {
        Name nameDouble = mock(Name.class);
        Address addressDouble = mock(Address.class);
        Email emailDouble = mock(Email.class);
        Phone phoneDouble = mock(Phone.class);

        UserFactory userFactory = new UserFactory();

        try (MockedConstruction<User> mockedConstruction = mockConstruction(User.class)) {

            User userResult = userFactory.createUser(nameDouble, addressDouble, emailDouble, phoneDouble);

            assertNotNull(userResult);
            assertEquals(1, mockedConstruction.constructed().size());
        }
    }
}