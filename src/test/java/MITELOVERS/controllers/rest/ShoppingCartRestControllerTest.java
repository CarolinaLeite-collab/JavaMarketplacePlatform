package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ShoppingCartService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.ShoppingCartLinkProvider;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.user.User;
import MITELOVERS.mapper.ShoppingCartResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    }

    @Test
    void optionsForCartLineReturnsAllowedMethodsWhenEmailProvided() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        ShoppingCart cartDouble = mock(ShoppingCart.class);
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);

        when(_userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(_shoppingCartService.findCartByCartId("SC-A49F78E2")).thenReturn(cartDouble);
        when(_shoppingCartService.findCartLineByUserId("SC-A49F78E2", "SCL-1234ABCD")).thenReturn(lineDouble);
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
}