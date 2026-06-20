package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.LibraryItemDetails;
import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.controllers.linkprovider.LibraryLinkProvider;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.LibrarySort;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.LibraryItemResponseDTO;
import MITELOVERS.mapper.LibraryItemResponseDTOMapper;
import MITELOVERS.mapper.LibrarySortRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryRestController.class)
@Import(CustomRestExceptionHandler.class)
class LibraryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibraryService libraryService;

    @MockitoBean
    private LibraryLinkProvider libraryLinkProvider;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private LibrarySortRequestMapper sortRequestMapper;

    @MockitoBean
    private LibraryItemResponseDTOMapper libraryItemResponseDTOMapper;

    @BeforeEach
    void setUp() {
        when(sortRequestMapper.toDomain(null))
                .thenReturn(LibrarySort.NONE);
    }

    @Test
    void shouldReturn200WithItemsWhenLibraryExists() throws Exception {
        // Arrange
        Item itemDouble = mock(Item.class);
        Publication publicationDouble = mock(Publication.class);
        Edition editionDouble = mock(Edition.class);
        Author authorDouble = mock(Author.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);

        LibraryItemDetails details = new LibraryItemDetails(
                itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble);

        LibraryItemResponseDTO dto = new LibraryItemResponseDTO(
                "3C5D126F8B",
                "1984",
                "George Orwell",
                "Book",
                "9780451524935",
                "https://example.com/1984.jpg"
        );

        when(libraryService.getListOfItemInfoInMyLibrary(
                any(UserId.class),
                eq(LibrarySort.NONE)
        )).thenReturn(List.of(details));

        when(libraryItemResponseDTOMapper.toDTO(
                itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble
        )).thenReturn(dto);

        // Act
        var result = mockMvc.perform(get("/my-library")
                .header("X-User-Id", "pedro@aeiou.com"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath(
                        "$._embedded.libraryItemResponseDTOList[0]._links.self"
                ).exists())
                .andExpect(jsonPath(
                        "$._embedded.libraryItemResponseDTOList[0].authorName"
                ).value("George Orwell"))
                .andExpect(jsonPath(
                        "$._embedded.libraryItemResponseDTOList[0].publicationType"
                ).value("Book"))
                .andExpect(jsonPath(
                        "$._embedded.libraryItemResponseDTOList[0].identifier"
                ).value("9780451524935"))
                .andExpect(jsonPath("$._links.sort").exists())
                .andExpect(jsonPath("$._links.sort.href")
                        .value("/my-library{?sort}"))
                .andExpect(jsonPath("$._links.sort.templated").value(true));
    }

    @Test
    void shouldReturn200WithEmptyListWhenLibraryIsEmpty() throws Exception {
        // Arrange
        when(libraryService.getListOfItemInfoInMyLibrary(
                any(UserId.class),
                eq(LibrarySort.NONE)
        )).thenReturn(List.of());

        // Act
        var result = mockMvc.perform(get("/my-library")
                .header("X-User-Id", "pedro@aeiou.com"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void shouldReturn200WithEmptyListWhenNoLibraryExists() throws Exception {
        // Arrange
        when(libraryService.getListOfItemInfoInMyLibrary(
                any(UserId.class),
                eq(LibrarySort.NONE)
        )).thenReturn(List.of());

        // Act
        var result = mockMvc.perform(get("/my-library")
                .header("X-User-Id", "naoexiste@aeiou.com"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void shouldSortLibraryByAuthor() throws Exception {
        // Arrange
        when(sortRequestMapper.toDomain("author"))
                .thenReturn(LibrarySort.AUTHOR);

        when(libraryService.getListOfItemInfoInMyLibrary(
                any(UserId.class),
                eq(LibrarySort.AUTHOR)
        )).thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/my-library")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .param("sort", "author"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn200WithItemDetailsWhenItemExists() throws Exception {
        // Arrange
        Item itemDouble = mock(Item.class);
        Publication publicationDouble = mock(Publication.class);
        Edition editionDouble = mock(Edition.class);
        Author authorDouble = mock(Author.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);

        LibraryItemDetails details = new LibraryItemDetails(
                itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble);

        LibraryItemResponseDTO dto = new LibraryItemResponseDTO(
                "3C5D126F8B",
                "1984",
                "George Orwell",
                "BOOK",
                "no identifier",
                null
        );

        when(libraryService.getItemDetail(any())).thenReturn(details);
        when(libraryItemResponseDTOMapper.toDTO(
                itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble
        )).thenReturn(dto);

        // Act
        var result = mockMvc.perform(get("/my-library/3C5D126F8B"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$.authorName").value("George Orwell"))
                .andExpect(jsonPath("$.publicationType").value("BOOK"))
                .andExpect(jsonPath("$.identifier").value("no identifier"));
    }

    @Test
    void shouldReturn201WhenItemAddedToLibrary() throws Exception {
        // Arrange
        Item itemDouble = mock(Item.class);
        Publication publicationDouble = mock(Publication.class);
        Edition editionDouble = mock(Edition.class);
        Author authorDouble = mock(Author.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);

        LibraryItemDetails details = new LibraryItemDetails(
                itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble);

        LibraryItemResponseDTO dto = new LibraryItemResponseDTO(
                "3C5D126F8B",
                "1984",
                "George Orwell",
                "Book",
                "9780451524935",
                "https://example.com/1984.jpg"
        );

        when(libraryService.addItemToLibrary(any(), any())).thenReturn(details);
        when(libraryItemResponseDTOMapper.toDTO(
                itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble
        )).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(post("/my-library")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\": \"3C5D126F8B\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$.authorName").value("George Orwell"))
                .andExpect(jsonPath("$.publicationType").value("Book"))
                .andExpect(jsonPath("$.identifier").value("9780451524935"));
    }

    @Test
    void optionsShouldReturn200WithLinksForAuthorizedUser() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        when(userService.getUserByEmail("pedro@aeiou.com")).thenReturn(userDouble);
        when(libraryLinkProvider.getLinks(userDouble)).thenReturn(List.of(
                Link.of("/my-library").withRel("library"),
                Link.of("/my-library").withRel("library-add")
        ));

        // Act & Assert
        mockMvc.perform(options("/my-library")
                        .param("email", "pedro@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("GET")))
                .andExpect(header().string("Allow", containsString("POST")))
                .andExpect(header().string("Allow", containsString("OPTIONS")))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.library").exists())
                .andExpect(jsonPath("$._links.library-add").exists());
    }

    @Test
    void optionsShouldReturn200WithNoLinksForUnauthorizedUser() throws Exception {
        // Arrange
        User userDouble = mock(User.class);
        when(userService.getUserByEmail("readonly@aeiou.com")).thenReturn(userDouble);
        when(libraryLinkProvider.getLinks(userDouble)).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(options("/my-library")
                        .param("email", "readonly@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", containsString("GET")))
                .andExpect(header().string("Allow", containsString("POST")))
                .andExpect(header().string("Allow", containsString("OPTIONS")))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void shouldReturn400WhenHeaderIsMissing() throws Exception {
        // Act
        var result = mockMvc.perform(get("/my-library"));

        // Assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenItemIdIsBlank() throws Exception {
        mockMvc.perform(post("/my-library")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\": \" \"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenItemIdIsMissing() throws Exception {
        mockMvc.perform(post("/my-library")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}