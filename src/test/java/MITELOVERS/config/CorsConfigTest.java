package MITELOVERS.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"bootstrap", "jpa"})
@AutoConfigureMockMvc
class CorsConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void corsHeadersPresentForAllowedOrigin() throws Exception {
        // Arrange
        // Act
        var result = mockMvc.perform(
                options("/my-library/")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "GET")
        );

        // Assert
        result.andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
                .andExpect(header().string("Access-Control-Allow-Methods", containsString("GET")));
    }

    @Test
    void corsHeadersNotPresentForDisallowedOrigin() throws Exception {
        // Arrange
        // Act
        var result = mockMvc.perform(
                options("/my-library/")
                        .header("Origin", "http://malicious-site.com")
                        .header("Access-Control-Request-Method", "GET")
        );

        // Assert
        result.andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    void corsConfigurerIsNotNull() {
        // Arrange
        CorsConfig config = new CorsConfig();
        ReflectionTestUtils.setField(config, "allowedOrigins", "http://localhost:5173");

        // SUT
        WebMvcConfigurer configurer = config.corsConfigurer();

        // Assert
        assertNotNull(configurer);
    }
}