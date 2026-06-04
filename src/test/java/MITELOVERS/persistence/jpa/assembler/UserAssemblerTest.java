package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.user.User;
import MITELOVERS.domain.user.UserFactory;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.Role;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.jpa.datamodel.UserDataModel;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserAssemblerTest {

    @Test
    void toDataModelShouldMapIdFromUserIdentity() {
        // Arrange
        UserFactory factoryDouble = mock(UserFactory.class);

        // SUT
        UserAssembler assembler = new UserAssembler(factoryDouble);

        User userDouble = mock(User.class);
        UserId userId = new UserId(new Email("pedro@mitelovers.com"));
        when(userDouble.identity()).thenReturn(userId);
        when(userDouble.getName()).thenReturn(new Name("Pedro"));

        // Act
        UserDataModel result = assembler.toDataModel(userDouble);

        // Assert
        assertEquals("pedro@mitelovers.com", result.getId());
    }

    @Test
    void toDataModelShouldMapNameCorrectly() {
        // Arrange
        UserFactory factoryDouble = mock(UserFactory.class);

        // SUT
        UserAssembler assembler = new UserAssembler(factoryDouble);

        User userDouble = mock(User.class);
        UserId userId = new UserId(new Email("pedro@mitelovers.com"));
        when(userDouble.identity()).thenReturn(userId);
        when(userDouble.getName()).thenReturn(new Name("Pedro"));

        // Act
        UserDataModel result = assembler.toDataModel(userDouble);

        // Assert
        assertEquals("Pedro", result.getName());
    }

    @Test
    void toDataModelShouldMapEmailCorrectly() {
        // Arrange
        UserFactory factoryDouble = mock(UserFactory.class);

        // SUT
        UserAssembler assembler = new UserAssembler(factoryDouble);

        User userDouble = mock(User.class);
        UserId userId = new UserId(new Email("pedro@mitelovers.com"));
        when(userDouble.identity()).thenReturn(userId);
        when(userDouble.getName()).thenReturn(new Name("Pedro"));
        when(userDouble.getEmail()).thenReturn("pedro@mitelovers.com");

        // Act
        UserDataModel result = assembler.toDataModel(userDouble);

        // Assert
        assertEquals("pedro@mitelovers.com", result.getEmail());
    }



    @Test
    void toDomainShouldDelegateToFactory() {
        // Arrange
        UserFactory factoryDouble = mock(UserFactory.class);

        // SUT
        UserAssembler assembler = new UserAssembler(factoryDouble);

        UserDataModel dmDouble = mock(UserDataModel.class);
        User userDouble = mock(User.class);

        when(dmDouble.getId()).thenReturn("pedro@mitelovers.com");
        when(dmDouble.getName()).thenReturn("Pedro");
        when(dmDouble.getEmail()).thenReturn("pedro@mitelovers.com");
        when(factoryDouble.createUser(any(), any(), any(), any(), any())).thenReturn(userDouble);


        // Act
        User result = assembler.toDomain(dmDouble);

        // Assert
        assertEquals(userDouble, result);
    }

    @Test
    void toDomainSetsRolesFromDataModel() {
        // Arrange
        UserDataModel dataModelDouble = mock(UserDataModel.class);
        UserFactory userFactory = new UserFactory();

        when(dataModelDouble.getEmail()).thenReturn("guest@aeiou.com");
        when(dataModelDouble.getId()).thenReturn("guest@aeiou.com");
        when(dataModelDouble.getName()).thenReturn("Guest");
        when(dataModelDouble.getRoles()).thenReturn(Set.of(Role.NONREGISTRED));

        // SUT
        UserAssembler assembler = new UserAssembler(userFactory);

        // Act
        User result = assembler.toDomain(dataModelDouble);

        // Assert
        assertTrue(result.hasRole(Role.NONREGISTRED));
        assertFalse(result.hasRole(Role.USER));
    }

}