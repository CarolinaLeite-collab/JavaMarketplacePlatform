package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ShoppingCartService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.ShoppingCartLinkProvider;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.ShoppingCartId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.ShoppingCartLineResponseDTO;
import MITELOVERS.dto.response.ShoppingCartResponseDTO;
import MITELOVERS.mapper.ShoppingCartLineResponseDTOMapper;
import MITELOVERS.mapper.ShoppingCartResponseDTOMapper;
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

@WebMvcTest(ShoppingCartRestController.class)
class ShoppingCartRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShoppingCartService _shoppingCartService;

    @MockitoBean
    private ShoppingCartLinkProvider _shoppingCartLinkProvider;

    @MockitoBean
    private UserService _userService;

    @MockitoBean
    private ShoppingCartResponseDTOMapper _shoppingCartResponseDTOMapper;

    @MockitoBean
    private ShoppingCartLineResponseDTOMapper _shoppingCartLineResponseDTOMapper;

    @Test
    void optionsForCartReturnsAllowedMethodsWhenEmailProvided() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        ShoppingCart cartDouble = mock(ShoppingCart.class);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);
        when(_shoppingCartLinkProvider.getAllowedMethodsForCart(userDouble, cartDouble))
                .thenReturn(List.of(HttpMethod.GET, HttpMethod.PATCH, HttpMethod.OPTIONS));

        // Act + Assert
        mockMvc.perform(options("/shopping-carts/SC-A49F78E2")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("GET")))
                .andExpect(header().string("Allow", containsString("PATCH")))
                .andExpect(header().string("Allow", containsString("OPTIONS")));
    }

    @Test
    void optionsForCartReturnsOnlyOptionsWhenEmailBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(options("/shopping-carts/SC-A49F78E2")
                        .header("X-User-Id", ""))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", "OPTIONS"));

        verifyNoInteractions(_userService, _shoppingCartService, _shoppingCartLinkProvider);
    }

    @Test
    void getUserCartReturnsOkWhenOwner() throws Exception {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);

        ShoppingCartResponseDTO dtoDouble = mock(ShoppingCartResponseDTO.class);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);
        when(_shoppingCartResponseDTOMapper.toModel(cartDouble)).thenReturn(dtoDouble);

        // Act + Assert
        mockMvc.perform(get("/shopping-carts/SC-A49F78E2")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void getUserCartReturnsForbiddenWhenNotOwner() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(mock(UserId.class));

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(mock(UserId.class));

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);

        // Act + Assert
        mockMvc.perform(get("/shopping-carts/SC-A49F78E2")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUserCartReturnsForbiddenWhenEmailBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/shopping-carts/SC-A49F78E2")
                        .header("X-User-Id", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    void clearUserCartReturnsOkWhenOwner() throws Exception {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);

        ShoppingCart clearedCartDouble = mock(ShoppingCart.class);
        ShoppingCartResponseDTO dtoDouble = mock(ShoppingCartResponseDTO.class);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);
        when(_shoppingCartService.clearShoppingCartLines("SC-A49F78E2")).thenReturn(clearedCartDouble);
        when(_shoppingCartResponseDTOMapper.toModel(clearedCartDouble)).thenReturn(dtoDouble);

        // Act + Assert
        mockMvc.perform(patch("/shopping-carts/SC-A49F78E2")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void clearUserCartReturnsForbiddenWhenNotOwner() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(mock(UserId.class));

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(mock(UserId.class));

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);

        // Act + Assert
        mockMvc.perform(patch("/shopping-carts/SC-A49F78E2")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void optionsForCartLinesReturnsAllowedMethodsWhenEmailProvided() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        ShoppingCart cartDouble = mock(ShoppingCart.class);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);
        when(_shoppingCartLinkProvider.getAllowedMethodsForCartLines(userDouble, cartDouble))
                .thenReturn(List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS));

        // Act + Assert
        mockMvc.perform(options("/shopping-carts/SC-A49F78E2/shopping-cart-lines")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("GET")))
                .andExpect(header().string("Allow", containsString("POST")))
                .andExpect(header().string("Allow", containsString("OPTIONS")));
    }

    @Test
    void optionsForCartLinesReturnsOnlyOptionsWhenEmailBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(options("/shopping-carts/SC-A49F78E2/shopping-cart-lines")
                        .header("X-User-Id", ""))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", "OPTIONS"));

        verifyNoInteractions(_userService, _shoppingCartService, _shoppingCartLinkProvider);
    }

    @Test
    void addCartLineReturnsCreatedWhenOwner() throws Exception {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);

        ShoppingCart updatedCartDouble = mock(ShoppingCart.class);
        ShoppingCartId updatedCartIdDouble = mock(ShoppingCartId.class);
        when(updatedCartIdDouble.toString()).thenReturn("SC-A49F78E2");
        when(updatedCartDouble.identity()).thenReturn(updatedCartIdDouble);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);
        when(_shoppingCartService.addCartLineToCart("SC-A49F78E2", "DS-1A2B3C4D")).thenReturn(updatedCartDouble);

        // Act + Assert
        mockMvc.perform(post("/shopping-carts/SC-A49F78E2/shopping-cart-lines")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"directSaleId\": \"DS-1A2B3C4D\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void addCartLineReturnsForbiddenWhenNotOwner() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(mock(UserId.class));

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(mock(UserId.class));

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);

        // Act + Assert
        mockMvc.perform(post("/shopping-carts/SC-A49F78E2/shopping-cart-lines")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"directSaleId\": \"DS-1A2B3C4D\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void optionsForCartLineReturnsAllowedMethodsWhenEmailProvided() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        ShoppingCart cartDouble = mock(ShoppingCart.class);
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);
        when(_shoppingCartService.findCartLineByLineCartId("SC-A49F78E2", "SCL-1234ABCD")).thenReturn(lineDouble);
        when(_shoppingCartLinkProvider.getAllowedMethodsForCartLine(userDouble, cartDouble, lineDouble))
                .thenReturn(List.of(HttpMethod.GET, HttpMethod.DELETE, HttpMethod.OPTIONS));

        // Act + Assert
        mockMvc.perform(options("/shopping-carts/SC-A49F78E2/shopping-cart-lines/SCL-1234ABCD")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("GET")))
                .andExpect(header().string("Allow", containsString("DELETE")))
                .andExpect(header().string("Allow", containsString("OPTIONS")));
    }

    @Test
    void optionsForCartLineReturnsOnlyOptionsWhenEmailBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(options("/shopping-carts/SC-A49F78E2/shopping-cart-lines/SCL-1234ABCD")
                        .header("X-User-Id", ""))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", "OPTIONS"));

    }

    @Test
    void getUserCartLineReturnsOkWhenOwner() throws Exception {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);

        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);
        ShoppingCartLineResponseDTO dtoDouble = mock(ShoppingCartLineResponseDTO.class);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);
        when(_shoppingCartService.findCartLineByLineCartId("SC-A49F78E2", "SCL-1234ABCD")).thenReturn(lineDouble);
        when(_shoppingCartLineResponseDTOMapper.toModel(lineDouble)).thenReturn(dtoDouble);
        when(lineDouble.getDirectSaleId()).thenReturn(mock(MITELOVERS.domain.valueobject.DirectSaleId.class));

        // Act + Assert
        mockMvc.perform(get("/shopping-carts/SC-A49F78E2/shopping-cart-lines/SCL-1234ABCD")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserCartLineReturnsForbiddenWhenNotOwner() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(mock(UserId.class));

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(mock(UserId.class));

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);

        // Act + Assert
        mockMvc.perform(get("/shopping-carts/SC-A49F78E2/shopping-cart-lines/SCL-1234ABCD")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUserCartLineReturnsOkWhenOwner() throws Exception {
        // Arrange
        UserId sharedUserId = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(sharedUserId);

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(sharedUserId);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);

        // Act + Assert
        mockMvc.perform(delete("/shopping-carts/SC-A49F78E2/shopping-cart-lines/SCL-1234ABCD")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isOk());

    }

    @Test
    void deleteUserCartLineReturnsForbiddenWhenNotOwner() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(mock(UserId.class));

        ShoppingCart cartDouble = mock(ShoppingCart.class);
        when(cartDouble.getBuyerId()).thenReturn(mock(UserId.class));

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);

        // Act + Assert
        mockMvc.perform(delete("/shopping-carts/SC-A49F78E2/shopping-cart-lines/SCL-1234ABCD")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isForbidden());
    }
}