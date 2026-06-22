package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.repository.ISaleRepo;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleFactory;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.sale.SaleLineFactory;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;
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
class SaleServiceTest {

    @Mock
    private ISaleRepo _saleRepo;

    @Mock
    private ShoppingCartService _shoppingCartService;

    @Mock
    private PaymentService _paymentService;

    @Mock
    private DirectSaleService _directSaleService;

    @Mock
    private ItemService _itemService;

    @Mock
    private SaleFactory _saleFactory;

    @Mock
    private SaleLineFactory _saleLineFactory;

    @InjectMocks
    private SaleService _service;

    @Test
    void findUserSalesReturnsListOfSalesWhenFound() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(userIdDouble);

        Sale sale1Double = mock(Sale.class);
        Sale sale2Double = mock(Sale.class);

        when(_saleRepo.findByUserId(userIdDouble)).thenReturn(List.of(sale1Double, sale2Double));

        // Act
        List<Sale> result = _service.findUserSales(userDouble);

        // Assert
        assertEquals(2, result.size());
        assertSame(sale1Double, result.get(0));
        assertSame(sale2Double, result.get(1));
    }

    @Test
    void findUserSalesReturnsEmptyListWhenNoSalesFound() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(userIdDouble);

        when(_saleRepo.findByUserId(userIdDouble)).thenReturn(List.of());

        // Act
        List<Sale> result = _service.findUserSales(userDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findSaleByIdReturnsSaleWhenFound() {
        // Arrange
        SaleId saleIdDouble = mock(SaleId.class);
        Sale saleDouble = mock(Sale.class);

        when(_saleRepo.ofIdentity(saleIdDouble)).thenReturn(Optional.of(saleDouble));

        // Act
        Sale result = _service.findSaleById(saleIdDouble);

        // Assert
        assertSame(saleDouble, result);
    }

    @Test
    void findSaleByIdThrowsWhenNotFound() {
        // Arrange
        SaleId saleIdDouble = mock(SaleId.class);

        when(_saleRepo.ofIdentity(saleIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findSaleById(saleIdDouble));
    }

    @Test
    void getSaleLineByIdReturnsSaleLineWhenFound() {
        // Arrange
        SaleId saleIdDouble = mock(SaleId.class);
        SaleLineId saleLineId = new SaleLineId("SL-1234ABCD");

        SaleLine saleLineDouble = mock(SaleLine.class);
        when(saleLineDouble.identity()).thenReturn(saleLineId);

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_saleLines()).thenReturn(List.of(saleLineDouble));

        when(_saleRepo.ofIdentity(saleIdDouble)).thenReturn(Optional.of(saleDouble));

        // Act
        SaleLine result = _service.getSaleLineById(saleIdDouble, saleLineId);

        // Assert
        assertSame(saleLineDouble, result);
    }

    @Test
    void getSaleLineByIdThrowsWhenLineNotFound() {
        // Arrange
        SaleId saleIdDouble = mock(SaleId.class);
        SaleLineId saleLineId = new SaleLineId("SL-1234ABCD");

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_saleLines()).thenReturn(List.of());

        when(_saleRepo.ofIdentity(saleIdDouble)).thenReturn(Optional.of(saleDouble));

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.getSaleLineById(saleIdDouble, saleLineId));
    }

    @Test
    void getSaleLineByIdThrowsWhenSaleNotFound() {
        // Arrange
        SaleId saleIdDouble = mock(SaleId.class);
        SaleLineId saleLineId = new SaleLineId("SL-1234ABCD");

        when(_saleRepo.ofIdentity(saleIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.getSaleLineById(saleIdDouble, saleLineId));
    }

    @Test
    void createSaleFromCartThrowsWhenCartIsEmpty() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of());

        when(_shoppingCartService.findCartByCartId(any(ShoppingCartId.class))).thenReturn(cartDouble);

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> _service.createSaleFromCart(cartIdDouble));
    }

    @Test
    void createSaleFromCartThrowsWhenCartNotFound() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);

        when(_shoppingCartService.findCartByCartId(any(ShoppingCartId.class)))
                .thenThrow(new NoSuchElementException("ShoppingCart not found!"));

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.createSaleFromCart(cartIdDouble));
    }

    @Test
    void createSaleFromCartReturnsSaleWhenPaymentSuccessful() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("ABCDEF1234");

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        when(lineDouble.getDirectSaleId()).thenReturn(directSaleIdDouble);
        when(lineDouble.getSellerId()).thenReturn(mock(UserId.class));
        when(lineDouble.getPriceAtAddition()).thenReturn(mock(Price.class));

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of(lineDouble));
        when(cartDouble.getBuyerId()).thenReturn(mock(UserId.class));

        SaleLine saleLineDouble = mock(SaleLine.class);
        when(saleLineDouble.get_directSaleId()).thenReturn(directSaleIdDouble);

        Price totalAmountDouble = mock(Price.class);
        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_totalAmount()).thenReturn(totalAmountDouble);
        when(saleDouble.get_saleLines()).thenReturn(List.of(saleLineDouble));

        DirectSale directSaleDouble = mock(DirectSale.class);
        when(directSaleDouble.getItemsId()).thenReturn(List.of(itemIdDouble));

        when(_shoppingCartService.findCartByCartId(any(ShoppingCartId.class))).thenReturn(cartDouble);
        when(_saleLineFactory.createSaleLine(any(), any(), any())).thenReturn(saleLineDouble);
        when(_saleFactory.createSale(any(), any())).thenReturn(saleDouble);
        when(_paymentService.isPaymentSuccessful(totalAmountDouble)).thenReturn(true);
        when(_directSaleService.getDirectSaleById("DS-1A2B3C4DE")).thenReturn(directSaleDouble);

        // Act
        Sale result = _service.createSaleFromCart(cartIdDouble);

        // Assert
        assertSame(saleDouble, result);
    }

    @Test
    void createSaleFromCartReturnsCancelledSaleWhenPaymentFails() {
        // Arrange
        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        when(lineDouble.getDirectSaleId()).thenReturn(mock(DirectSaleId.class));
        when(lineDouble.getSellerId()).thenReturn(mock(UserId.class));
        when(lineDouble.getPriceAtAddition()).thenReturn(mock(Price.class));

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getCartLines()).thenReturn(List.of(lineDouble));
        when(cartDouble.getBuyerId()).thenReturn(mock(UserId.class));

        SaleLine saleLineDouble = mock(SaleLine.class);
        Price totalAmountDouble = mock(Price.class);

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_totalAmount()).thenReturn(totalAmountDouble);

        when(_shoppingCartService.findCartByCartId(cartIdDouble)).thenReturn(cartDouble);
        when(_saleLineFactory.createSaleLine(any(), any(), any())).thenReturn(saleLineDouble);
        when(_saleFactory.createSale(any(), any())).thenReturn(saleDouble);
        when(_paymentService.isPaymentSuccessful(totalAmountDouble)).thenReturn(false);
        when(_saleRepo.save(saleDouble)).thenReturn(saleDouble);

        // Act
        Sale result = _service.createSaleFromCart(cartIdDouble);

        // Assert
        assertSame(saleDouble, result);
    }
}