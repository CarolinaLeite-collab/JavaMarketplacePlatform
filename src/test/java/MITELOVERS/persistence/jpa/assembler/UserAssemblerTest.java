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
    void domain2DMShouldMapIdFromUserIdentity() {
        // Arrange
        UserFactory factoryDouble = mock(UserFactory.class);

        // SUT
        UserAssembler assembler = new UserAssembler(factoryDouble);

        User userDouble = mock(User.class);
        UserId userId = new UserId(new Email("pedro@mitelovers.com"));
        when(userDouble.identity()).thenReturn(userId);
        when(userDouble.getName()).thenReturn(new Name("Pedro"));

        // Act
        UserDataModel result = assembler.domain2DM(userDouble);

        // Assert
        assertEquals("pedro@mitelovers.com", result.getId());
    }

    @Test
    void domain2DMShouldMapNameCorrectly() {
        // Arrange
        UserFactory factoryDouble = mock(UserFactory.class);

        // SUT
        UserAssembler assembler = new UserAssembler(factoryDouble);

        User userDouble = mock(User.class);
        UserId userId = new UserId(new Email("pedro@mitelovers.com"));
        when(userDouble.identity()).thenReturn(userId);
        when(userDouble.getName()).thenReturn(new Name("Pedro"));

        // Act
        UserDataModel result = assembler.domain2DM(userDouble);

        // Assert
        assertEquals("Pedro", result.getName());
    }

    @Test
    void domain2DMShouldMapEmailCorrectly() {
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
        UserDataModel result = assembler.domain2DM(userDouble);

        // Assert
        assertEquals("pedro@mitelovers.com", result.getEmail());
    }

    @Test
    void domain2DMShouldThrowWhenUserIsNull() {
        // Arrange
        UserFactory factoryDouble = mock(UserFactory.class);

        // SUT
        UserAssembler assembler = new UserAssembler(factoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> assembler.domain2DM(null));
    }


    @Test
    void DM2DomainShouldDelegateToFactory() {
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
        User result = assembler.DM2Domain(dmDouble);

        // Assert
        assertEquals(userDouble, result);
    }

    @Test
    void DM2DomainShouldThrowWhenDataModelIsNull() {
        // Arrange
        UserFactory factoryDouble = mock(UserFactory.class);

        // SUT
        UserAssembler assembler = new UserAssembler(factoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> assembler.DM2Domain(null));
    }

}