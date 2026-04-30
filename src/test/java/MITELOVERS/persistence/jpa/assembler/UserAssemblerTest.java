package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.jpa.datamodel.UserDataModel;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.user.UserFactory;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.Name;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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


}