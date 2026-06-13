package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.valueobject.ShoppingCartId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.jpa.assembler.ShoppingCartAssembler;
import MITELOVERS.persistence.jpa.datamodel.ShoppingCartDataModel;
import MITELOVERS.persistence.springdata.IShoppingCartSpringDataRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaShoppingCartRepoTest {

    @Mock
    private IShoppingCartSpringDataRepo _shoppingCartSpringDataRepoDouble;

    @Mock
    private ShoppingCartAssembler _shoppingCartAssemblerDouble;

    @InjectMocks
    private JpaShoppingCartRepo _jpaShoppingCartRepo;

    @Test
    void saveShouldSaveDataModelAndReturnShoppingCart() {

        // Arrange
        ShoppingCart shoppingCartDouble = mock(ShoppingCart.class);
        ShoppingCartDataModel dmDouble = mock(ShoppingCartDataModel.class);
        ShoppingCartDataModel savedDmDouble = mock(ShoppingCartDataModel.class);

        when(_shoppingCartAssemblerDouble.toDataModel(shoppingCartDouble)).thenReturn(dmDouble);
        when(_shoppingCartSpringDataRepoDouble.save(dmDouble)).thenReturn(savedDmDouble);
        when(_shoppingCartAssemblerDouble.toDomain(savedDmDouble)).thenReturn(shoppingCartDouble);

        // Act
        ShoppingCart result = _jpaShoppingCartRepo.save(shoppingCartDouble);

        // Assert
        assertEquals(shoppingCartDouble, result);
    }

    @Test
    void testFindAllKeysReturnShoppingCartIds() {

        // Arrange
        ShoppingCartDataModel dm1Double = mock(ShoppingCartDataModel.class);
        ShoppingCartDataModel dm2Double = mock(ShoppingCartDataModel.class);
        List<ShoppingCartDataModel> dmList = List.of(dm1Double, dm2Double);

        when(dm1Double.getShoppingCartId()).thenReturn("SC-1234ABCD");
        when(dm2Double.getShoppingCartId()).thenReturn("SC-5678EFGH");
        when(_shoppingCartSpringDataRepoDouble.findAll()).thenReturn(dmList);

        // Act
        Iterable<ShoppingCartId> result = _jpaShoppingCartRepo.findAllKeys();
        List<ShoppingCartId> resultList = (List<ShoppingCartId>) result;

        // Assert
        assertEquals(2, resultList.size());
        assertEquals("SC-1234ABCD", resultList.get(0).toString());
        assertEquals("SC-5678EFGH", resultList.get(1).toString());
    }

    @Test
    void testFindAllReturnIterableOfShoppingCarts() {

        // Arrange
        ShoppingCartDataModel dm1Double = mock(ShoppingCartDataModel.class);
        ShoppingCartDataModel dm2Double = mock(ShoppingCartDataModel.class);
        List<ShoppingCartDataModel> dmList = List.of(dm1Double, dm2Double);

        ShoppingCart cart1Double = mock(ShoppingCart.class);
        ShoppingCart cart2Double = mock(ShoppingCart.class);
        List<ShoppingCart> cartList = List.of(cart1Double, cart2Double);

        when(_shoppingCartSpringDataRepoDouble.findAll()).thenReturn(dmList);
        when(_shoppingCartAssemblerDouble.toDomain(dm1Double)).thenReturn(cart1Double);
        when(_shoppingCartAssemblerDouble.toDomain(dm2Double)).thenReturn(cart2Double);

        // Act
        Iterable<ShoppingCart> result = _jpaShoppingCartRepo.findAll();

        // Assert
        assertEquals(cartList, result);
    }

    @Test
    void testOfIdentityReturnsShoppingCart() {

        // Arrange
        ShoppingCartId shoppingCartIdDouble = mock(ShoppingCartId.class);
        ShoppingCartDataModel dmDouble = mock(ShoppingCartDataModel.class);
        ShoppingCart shoppingCartDouble = mock(ShoppingCart.class);

        when(shoppingCartIdDouble.toString()).thenReturn("SC-1234ABCD");
        when(_shoppingCartSpringDataRepoDouble.findById("SC-1234ABCD")).thenReturn(Optional.of(dmDouble));
        when(_shoppingCartAssemblerDouble.toDomain(dmDouble)).thenReturn(shoppingCartDouble);

        // Act
        Optional<ShoppingCart> result = _jpaShoppingCartRepo.ofIdentity(shoppingCartIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(shoppingCartDouble, result.get());
    }

    @Test
    void testOfIdentityReturnsEmptyWhenNotFound() {

        // Arrange
        ShoppingCartId shoppingCartIdDouble = mock(ShoppingCartId.class);

        when(shoppingCartIdDouble.toString()).thenReturn("SC-1234ABCD");
        when(_shoppingCartSpringDataRepoDouble.findById("SC-1234ABCD")).thenReturn(Optional.empty());

        // Act
        Optional<ShoppingCart> result = _jpaShoppingCartRepo.ofIdentity(shoppingCartIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testContainsOfIdentityReturnsTrueWhenShoppingCartExists() {

        // Arrange
        ShoppingCartId shoppingCartIdDouble = mock(ShoppingCartId.class);

        when(shoppingCartIdDouble.toString()).thenReturn("SC-1234ABCD");
        when(_shoppingCartSpringDataRepoDouble.existsById("SC-1234ABCD")).thenReturn(true);

        // Act
        boolean result = _jpaShoppingCartRepo.containsOfIdentity(shoppingCartIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void testContainsOfIdentityReturnsFalseWhenShoppingCartDoesNotExist() {

        // Arrange
        ShoppingCartId shoppingCartIdDouble = mock(ShoppingCartId.class);

        when(shoppingCartIdDouble.toString()).thenReturn("SC-1234ABCD");
        when(_shoppingCartSpringDataRepoDouble.existsById("SC-1234ABCD")).thenReturn(false);

        // Act
        boolean result = _jpaShoppingCartRepo.containsOfIdentity(shoppingCartIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void testFindShoppingCartByUserIdReturnsShoppingCart() {

        // Arrange
        UserId userIdDouble = mock(UserId.class);
        ShoppingCartDataModel dmDouble = mock(ShoppingCartDataModel.class);
        ShoppingCart shoppingCartDouble = mock(ShoppingCart.class);

        when(userIdDouble.toString()).thenReturn("email@email.com");
        when(_shoppingCartSpringDataRepoDouble.findByBuyerId("email@email.com")).thenReturn(Optional.of(dmDouble));
        when(_shoppingCartAssemblerDouble.toDomain(dmDouble)).thenReturn(shoppingCartDouble);

        // Act
        Optional<ShoppingCart> result = _jpaShoppingCartRepo.findShoppingCartByUserId(userIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(shoppingCartDouble, result.get());
    }

    @Test
    void testFindShoppingCartByUserIdReturnsEmptyWhenNotFound() {

        // Arrange
        UserId userIdDouble = mock(UserId.class);

        when(userIdDouble.toString()).thenReturn("email@email.com");
        when(_shoppingCartSpringDataRepoDouble.findByBuyerId("email@email.com")).thenReturn(Optional.empty());

        // Act
        Optional<ShoppingCart> result = _jpaShoppingCartRepo.findShoppingCartByUserId(userIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }
}