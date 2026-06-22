package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.SaleService;
import MITELOVERS.applicationservices.ShoppingCartService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.SaleLinkProvider;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.SaleId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.SaleLineResponseDTO;
import MITELOVERS.dto.response.SaleResponseDTO;
import MITELOVERS.mapper.SaleLineResponseDTOMapper;
import MITELOVERS.mapper.SaleResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SaleRestController.class)
class SaleRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SaleService _saleService;

    @MockitoBean
    private ShoppingCartService _shoppingCartService;

    @MockitoBean
    private UserService _userService;

    @MockitoBean
    private SaleLinkProvider _saleLinkProvider;

    @MockitoBean
    private SaleResponseDTOMapper _saleMapper;

    @MockitoBean
    private SaleLineResponseDTOMapper _saleLineMapper;

    @Test
    void optionsForSalesReturnsAllowedMethodsWhenEmailProvided() throws Exception {
        // Arrange
        User userDouble = mock(User.class);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_saleLinkProvider.getAllowedMethodsForSales(userDouble))
                .thenReturn(List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS));

        // Act + Assert
        mockMvc.perform(options("/sales")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("GET")))
                .andExpect(header().string("Allow", containsString("POST")))
                .andExpect(header().string("Allow", containsString("OPTIONS")));
    }

    @Test
    void optionsForSalesReturnsOnlyOptionsWhenEmailBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(options("/sales")
                        .header("X-User-Id", ""))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", "OPTIONS"));
    }

    @Test
    void getUserSalesReturnsOkWhenFound() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        Sale saleDouble = mock(Sale.class);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_saleService.findUserSales(userDouble)).thenReturn(List.of(saleDouble));

        // Act + Assert
        mockMvc.perform(get("/sales")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserSalesReturnsForbiddenWhenEmailBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/sales")
                        .header("X-User-Id", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verifyNoInteractions(_userService, _saleService, _saleLinkProvider);
    }

    @Test
    void createSaleFromCartReturnsCreatedWhenOwner() throws Exception {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);

        SaleId saleIdDouble = mock(SaleId.class);
        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_saleId()).thenReturn(saleIdDouble);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);
        when(_saleService.createSaleFromCart(any(), eq("pedro@aeiou.com"))).thenReturn(saleDouble);

        // Act + Assert
        mockMvc.perform(post("/sales")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"shoppingCartId\": \"SC-A49F78E2\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void createSaleFromCartReturnsForbiddenWhenNotOwner() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(mock(UserId.class));

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(mock(UserId.class));

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);

        // Act + Assert
        mockMvc.perform(post("/sales")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"shoppingCartId\": \"SC-A49F78E2\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createSaleFromCartReturnsForbiddenWhenEmailBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/sales")
                        .header("X-User-Id", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"shoppingCartId\": \"SC-A49F78E2\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void optionsForSaleReturnsAllowedMethodsWhenEmailProvided() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        Sale saleDouble = mock(Sale.class);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_saleService.findSaleById(any())).thenReturn(saleDouble);
        when(_saleLinkProvider.getAllowedMethodsForSale(userDouble, saleDouble))
                .thenReturn(List.of(HttpMethod.GET, HttpMethod.OPTIONS));

        // Act + Assert
        mockMvc.perform(options("/sales/SA-1234ABCD")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("GET")))
                .andExpect(header().string("Allow", containsString("OPTIONS")));
    }

    @Test
    void optionsForSaleReturnsOnlyOptionsWhenEmailBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(options("/sales/SA-1234ABCD")
                        .header("X-User-Id", ""))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", "OPTIONS"));
    }

    @Test
    void getSaleByIdReturnsOkWhenOwner() throws Exception {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_buyerId()).thenReturn(sharedUserId);

        SaleResponseDTO dtoDouble = mock(SaleResponseDTO.class);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_saleService.findSaleById(any())).thenReturn(saleDouble);
        when(_saleMapper.toModel(saleDouble)).thenReturn(dtoDouble);

        // Act + Assert
        mockMvc.perform(get("/sales/SA-1234ABCD")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getSaleByIdReturnsForbiddenWhenNotOwner() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(mock(UserId.class));

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_buyerId()).thenReturn(mock(UserId.class));

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_saleService.findSaleById(any())).thenReturn(saleDouble);

        // Act + Assert
        mockMvc.perform(get("/sales/SA-1234ABCD")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void getSaleByIdReturnsForbiddenWhenEmailBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/sales/SA-1234ABCD")
                        .header("X-User-Id", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void optionsForSaleLineReturnsAllowedMethodsWhenEmailProvided() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        Sale saleDouble = mock(Sale.class);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_saleService.findSaleById(any())).thenReturn(saleDouble);
        when(_saleLinkProvider.getAllowedMethodsForSaleLine(userDouble, saleDouble))
                .thenReturn(List.of(HttpMethod.GET, HttpMethod.OPTIONS));

        // Act + Assert
        mockMvc.perform(options("/sales/SA-1234ABCD/sale-lines/SL-1234ABCD")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("GET")))
                .andExpect(header().string("Allow", containsString("OPTIONS")));
    }

    @Test
    void optionsForSaleLineReturnsOnlyOptionsWhenEmailBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(options("/sales/SA-1234ABCD/sale-lines/SL-1234ABCD")
                        .header("X-User-Id", ""))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", "OPTIONS"));
    }

    @Test
    void getSaleLineByIdReturnsOkWhenOwner() throws Exception {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_buyerId()).thenReturn(sharedUserId);

        SaleLine saleLineDouble = mock(SaleLine.class);
        SaleLineResponseDTO dtoDouble = mock(SaleLineResponseDTO.class);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_saleService.findSaleById(any())).thenReturn(saleDouble);
        when(_saleService.getSaleLineById(any(), any())).thenReturn(saleLineDouble);
        when(_saleLineMapper.toModel(saleLineDouble)).thenReturn(dtoDouble);

        // Act + Assert
        mockMvc.perform(get("/sales/SA-1234ABCD/sale-lines/SL-1234ABCD")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getSaleLineByIdReturnsForbiddenWhenNotOwner() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(mock(UserId.class));

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_buyerId()).thenReturn(mock(UserId.class));

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_saleService.findSaleById(any())).thenReturn(saleDouble);

        // Act + Assert
        mockMvc.perform(get("/sales/SA-1234ABCD/sale-lines/SL-1234ABCD")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void getSaleLineByIdReturnsForbiddenWhenEmailBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/sales/SA-1234ABCD/sale-lines/SL-1234ABCD")
                        .header("X-User-Id", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}