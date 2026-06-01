package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.item.ItemFactory;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.Condition;
import MITELOVERS.domain.valueobject.Description;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.dto.response.ItemResponseDTO;
import MITELOVERS.mapper.ItemResponseDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    static final String ITEM_REPO_REQUIRED        = "ItemRepo is required";
    static final String ITEM_FACTORY_REQUIRED      = "ItemFactory is required";
    static final String EDITION_REPO_REQUIRED      = "EditionRepo is required";
    static final String PUBLICATION_REPO_REQUIRED  = "PublicationRepo is required";
    static final String AUTHOR_REPO_REQUIRED       = "AuthorRepo is required";
    static final String GENRE_REPO_REQUIRED        = "GenreRepo is required";
    static final String MAPPER_REQUIRED            = "ItemResponseDTOMapper is required";
    static final String EDITION_NOT_FOUND          = "Edition does not exist in the repository";
    static final String PUBLICATION_NOT_FOUND      = "Publication does not exist in the repository";
    static final String AUTHOR_NOT_FOUND           = "Author does not exist in the repository";
    static final String GENRE_NOT_FOUND            = "Genre does not exist in the repository";
    static final String ITEM_NOT_FOUND             = "Item does not exist in the repository";

    IItemRepo itemRepoDouble;
    ItemFactory itemFactoryDouble;
    IEditionRepo editionRepoDouble;
    IPublicationRepo publicationRepoDouble;
    IAuthorRepo authorRepoDouble;
    IGenreRepo genreRepoDouble;
    ItemResponseDTOMapper mapperDouble;
    ItemService itemService;

    @BeforeEach
    void setUp() {
        itemRepoDouble        = mock(IItemRepo.class);
        itemFactoryDouble     = mock(ItemFactory.class);
        editionRepoDouble     = mock(IEditionRepo.class);
        publicationRepoDouble = mock(IPublicationRepo.class);
        authorRepoDouble      = mock(IAuthorRepo.class);
        genreRepoDouble       = mock(IGenreRepo.class);
        mapperDouble          = mock(ItemResponseDTOMapper.class);

        itemService = new ItemService(
                itemRepoDouble, itemFactoryDouble, editionRepoDouble,
                publicationRepoDouble, authorRepoDouble, genreRepoDouble, mapperDouble
        );
    }

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

    @Test
    void constructorThrowsWhenItemRepoIsNull() {
        // Act
        NullPointerException ex = assertThrows(NullPointerException.class, () ->
                new ItemService(null, itemFactoryDouble, editionRepoDouble,
                        publicationRepoDouble, authorRepoDouble, genreRepoDouble, mapperDouble));
        // Assert
        assertEquals(ITEM_REPO_REQUIRED, ex.getMessage());
    }

    @Test
    void constructorThrowsWhenItemFactoryIsNull() {
        // Act
        NullPointerException ex = assertThrows(NullPointerException.class, () ->
                new ItemService(itemRepoDouble, null, editionRepoDouble,
                        publicationRepoDouble, authorRepoDouble, genreRepoDouble, mapperDouble));
        // Assert
        assertEquals(ITEM_FACTORY_REQUIRED, ex.getMessage());
    }

    @Test
    void constructorThrowsWhenEditionRepoIsNull() {
        // Act
        NullPointerException ex = assertThrows(NullPointerException.class, () ->
                new ItemService(itemRepoDouble, itemFactoryDouble, null,
                        publicationRepoDouble, authorRepoDouble, genreRepoDouble, mapperDouble));
        // Assert
        assertEquals(EDITION_REPO_REQUIRED, ex.getMessage());
    }

    @Test
    void constructorThrowsWhenPublicationRepoIsNull() {
        // Act
        NullPointerException ex = assertThrows(NullPointerException.class, () ->
                new ItemService(itemRepoDouble, itemFactoryDouble, editionRepoDouble,
                        null, authorRepoDouble, genreRepoDouble, mapperDouble));
        // Assert
        assertEquals(PUBLICATION_REPO_REQUIRED, ex.getMessage());
    }

    @Test
    void constructorThrowsWhenAuthorRepoIsNull() {
        // Act
        NullPointerException ex = assertThrows(NullPointerException.class, () ->
                new ItemService(itemRepoDouble, itemFactoryDouble, editionRepoDouble,
                        publicationRepoDouble, null, genreRepoDouble, mapperDouble));
        // Assert
        assertEquals(AUTHOR_REPO_REQUIRED, ex.getMessage());
    }

    @Test
    void constructorThrowsWhenGenreRepoIsNull() {
        // Act
        NullPointerException ex = assertThrows(NullPointerException.class, () ->
                new ItemService(itemRepoDouble, itemFactoryDouble, editionRepoDouble,
                        publicationRepoDouble, authorRepoDouble, null, mapperDouble));
        // Assert
        assertEquals(GENRE_REPO_REQUIRED, ex.getMessage());
    }

    @Test
    void constructorThrowsWhenMapperIsNull() {
        // Act
        NullPointerException ex = assertThrows(NullPointerException.class, () ->
                new ItemService(itemRepoDouble, itemFactoryDouble, editionRepoDouble,
                        publicationRepoDouble, authorRepoDouble, genreRepoDouble, null));
        // Assert
        assertEquals(MAPPER_REQUIRED, ex.getMessage());
    }

    // ----------------------------------------------------------------
    // registerItem
    // ----------------------------------------------------------------

    @Test
    void registerItemValidRequestReturnsItemResponseDTO() {
        // Arrange
        EditionId editionIdDouble     = mock(EditionId.class);
        Edition editionDouble         = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble           = mock(Author.class);
        Genre genreDouble             = mock(Genre.class);
        Item itemDouble               = mock(Item.class);
        Item savedItemDouble          = mock(Item.class);
        ItemResponseDTO responseDTO   = mock(ItemResponseDTO.class);

        when(editionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(genreRepoDouble.ofIdentity(any())).thenReturn(Optional.of(genreDouble));
        when(itemFactoryDouble.createItem(any(), any(), any())).thenReturn(itemDouble);
        when(itemRepoDouble.save(itemDouble)).thenReturn(savedItemDouble);
        when(mapperDouble.toResponseDTO(savedItemDouble, editionDouble, publicationDouble, authorDouble, genreDouble))
                .thenReturn(responseDTO);

        // Act
        ItemResponseDTO result = itemService.registerItem(editionIdDouble, Condition.GOOD, new Description("Nice copy"));

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(itemFactoryDouble).createItem(any(), any(), any());
        verify(itemRepoDouble).save(itemDouble);
        verify(mapperDouble).toResponseDTO(savedItemDouble, editionDouble, publicationDouble, authorDouble, genreDouble);
    }

    @Test
    void registerItemEditionNotFoundThrowsNoSuchElementException() {
        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        when(editionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () ->
                itemService.registerItem(editionIdDouble, Condition.GOOD, new Description("Nice copy")));

        // Assert
        assertEquals(EDITION_NOT_FOUND, ex.getMessage());
    }

    @Test
    void registerItemPublicationNotFoundThrowsNoSuchElementException() {
        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Edition editionDouble     = mock(Edition.class);
        when(editionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () ->
                itemService.registerItem(editionIdDouble, Condition.GOOD, new Description("Nice copy")));

        // Assert
        assertEquals(PUBLICATION_NOT_FOUND, ex.getMessage());
    }

    @Test
    void registerItemAuthorNotFoundThrowsNoSuchElementException() {
        // Arrange
        EditionId editionIdDouble     = mock(EditionId.class);
        Edition editionDouble         = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        when(editionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () ->
                itemService.registerItem(editionIdDouble, Condition.GOOD, new Description("Nice copy")));

        // Assert
        assertEquals(AUTHOR_NOT_FOUND, ex.getMessage());
    }

    @Test
    void registerItemGenreNotFoundThrowsNoSuchElementException() {
        // Arrange
        EditionId editionIdDouble     = mock(EditionId.class);
        Edition editionDouble         = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble           = mock(Author.class);
        when(editionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(genreRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () ->
                itemService.registerItem(editionIdDouble, Condition.GOOD, new Description("Nice copy")));

        // Assert
        assertEquals(GENRE_NOT_FOUND, ex.getMessage());
    }

    // ----------------------------------------------------------------
    // getAllItems
    // ----------------------------------------------------------------

    @Test
    void getAllItemsReturnsListOfItemResponseDTOs() {
        // Arrange
        Item itemDouble               = mock(Item.class);
        Edition editionDouble         = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble           = mock(Author.class);
        Genre genreDouble             = mock(Genre.class);
        ItemResponseDTO responseDTO   = mock(ItemResponseDTO.class);

        when(itemRepoDouble.findAll()).thenReturn(List.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(genreRepoDouble.ofIdentity(any())).thenReturn(Optional.of(genreDouble));
        when(mapperDouble.toResponseDTO(itemDouble, editionDouble, publicationDouble, authorDouble, genreDouble))
                .thenReturn(responseDTO);

        // Act
        List<ItemResponseDTO> result = itemService.getAllItems();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDTO, result.get(0));
        verify(mapperDouble).toResponseDTO(itemDouble, editionDouble, publicationDouble, authorDouble, genreDouble);
    }

    @Test
    void getAllItemsEmptyReturnsEmptyList() {
        // Arrange
        when(itemRepoDouble.findAll()).thenReturn(List.of());

        // Act
        List<ItemResponseDTO> result = itemService.getAllItems();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllItemsEditionNotFoundThrowsNoSuchElementException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        when(itemRepoDouble.findAll()).thenReturn(List.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> itemService.getAllItems());

        // Assert
        assertEquals(EDITION_NOT_FOUND, ex.getMessage());
    }

    @Test
    void getAllItemsPublicationNotFoundThrowsNoSuchElementException() {
        // Arrange
        Item itemDouble        = mock(Item.class);
        Edition editionDouble  = mock(Edition.class);
        when(itemRepoDouble.findAll()).thenReturn(List.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> itemService.getAllItems());

        // Assert
        assertEquals(PUBLICATION_NOT_FOUND, ex.getMessage());
    }

    @Test
    void getAllItemsAuthorNotFoundThrowsNoSuchElementException() {
        // Arrange
        Item itemDouble               = mock(Item.class);
        Edition editionDouble         = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        when(itemRepoDouble.findAll()).thenReturn(List.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> itemService.getAllItems());

        // Assert
        assertEquals(AUTHOR_NOT_FOUND, ex.getMessage());
    }

    @Test
    void getAllItemsGenreNotFoundThrowsNoSuchElementException() {
        // Arrange
        Item itemDouble               = mock(Item.class);
        Edition editionDouble         = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble           = mock(Author.class);
        when(itemRepoDouble.findAll()).thenReturn(List.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(genreRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> itemService.getAllItems());

        // Assert
        assertEquals(GENRE_NOT_FOUND, ex.getMessage());
    }

    // ----------------------------------------------------------------
    // getItemById
    // ----------------------------------------------------------------

    @Test
    void getItemByIdValidIdReturnsItemResponseDTO() {
        // Arrange
        Item itemDouble               = mock(Item.class);
        Edition editionDouble         = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble           = mock(Author.class);
        Genre genreDouble             = mock(Genre.class);
        ItemResponseDTO responseDTO   = mock(ItemResponseDTO.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(genreRepoDouble.ofIdentity(any())).thenReturn(Optional.of(genreDouble));
        when(mapperDouble.toResponseDTO(itemDouble, editionDouble, publicationDouble, authorDouble, genreDouble))
                .thenReturn(responseDTO);

        // Act
        ItemResponseDTO result = itemService.getItemById("3C5D126F8B");

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(mapperDouble).toResponseDTO(itemDouble, editionDouble, publicationDouble, authorDouble, genreDouble);
    }

    @Test
    void getItemByIdItemNotFoundThrowsNoSuchElementException() {
        // Arrange
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () ->
                itemService.getItemById("3C5D126F8B"));

        // Assert
        assertEquals(ITEM_NOT_FOUND, ex.getMessage());
    }

    @Test
    void getItemByIdEditionNotFoundThrowsNoSuchElementException() {
        // Arrange
        Item itemDouble = mock(Item.class);
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () ->
                itemService.getItemById("3C5D126F8B"));

        // Assert
        assertEquals(EDITION_NOT_FOUND, ex.getMessage());
    }

    @Test
    void getItemByIdPublicationNotFoundThrowsNoSuchElementException() {
        // Arrange
        Item itemDouble       = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () ->
                itemService.getItemById("3C5D126F8B"));

        // Assert
        assertEquals(PUBLICATION_NOT_FOUND, ex.getMessage());
    }

    @Test
    void getItemByIdAuthorNotFoundThrowsNoSuchElementException() {
        // Arrange
        Item itemDouble               = mock(Item.class);
        Edition editionDouble         = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () ->
                itemService.getItemById("3C5D126F8B"));

        // Assert
        assertEquals(AUTHOR_NOT_FOUND, ex.getMessage());
    }

    @Test
    void getItemByIdGenreNotFoundThrowsNoSuchElementException() {
        // Arrange
        Item itemDouble               = mock(Item.class);
        Edition editionDouble         = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble           = mock(Author.class);
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(genreRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () ->
                itemService.getItemById("3C5D126F8B"));

        // Assert
        assertEquals(GENRE_NOT_FOUND, ex.getMessage());
    }
}