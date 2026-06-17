package MITELOVERS.applicationservices;

import MITELOVERS.domain.repository.IShoppingCartRepo;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.valueobject.ShoppingCartLineId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private IShoppingCartRepo _shoppingCartrepo;

    @InjectMocks
    private ShoppingCartService _service;

    @Test
    void findCartByCartIdReturnsCartWhenFound() {
        //Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.of(cartDouble));

        //Act
        ShoppingCart result = _service.findCartByCartId("SC-A49F78E2");

        //Assert
        assertSame(cartDouble, result);
    }

    @Test
    void findCartByCartIdThrowsWhenNotFound() {
        //Arrange
        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.empty());

        //Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findCartByCartId("SC-A49F78E2"));
    }

    @Test
    void findCartByUserIdReturnsCartWhenFound() {
        //Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(_shoppingCartrepo.findShoppingCartByUserId(any())).thenReturn(Optional.of(cartDouble));

        //Act
        ShoppingCart result = _service.findCartByUserId("pedro@aeiou.com");

        //Assert
        assertSame(cartDouble, result);
    }

    @Test
    void findCartByUserIdThrowsWhenNotFound() {
        //Arrange
        when(_shoppingCartrepo.findShoppingCartByUserId(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findCartByUserId("pedro@aeiou.com"));
    }

    @Test
    void findCartLineByUserIdReturnsLineWhenFound() {
        //Arrange
        ShoppingCartLineId shoppingCartLineId = new ShoppingCartLineId("SCL-1234ABCD");

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        when(lineDouble.identity()).thenReturn(shoppingCartLineId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of(lineDouble));

        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.of(cartDouble));

        //Act
        ShoppingCartLine result = _service.findCartLineByUserId("SC-A49F78E2", "SCL-1234ABCD");

        //Assert
        assertSame(lineDouble, result);
    }

    @Test
    void findCartLineByUserIdThrowsWhenLineNotFound() {
        //Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of());

        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.of(cartDouble));

        //Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findCartLineByUserId("SC-A49F78E2", "SCL-1234ABCD"));
    }

    @Test
    void findCartLineByUserIdThrowsWhenCartNotFound() {
        //Arrange
        when(_shoppingCartrepo.ofIdentity(any())).thenReturn(Optional.empty());

        //Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findCartLineByUserId("SC-A49F78E2", "SCL-1234ABCD"));
    }
}