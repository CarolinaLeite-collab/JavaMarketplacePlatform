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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemServiceTest {

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
        itemRepoDouble = mock(IItemRepo.class);
        itemFactoryDouble = mock(ItemFactory.class);
        editionRepoDouble = mock(IEditionRepo.class);
        publicationRepoDouble = mock(IPublicationRepo.class);
        authorRepoDouble = mock(IAuthorRepo.class);
        genreRepoDouble = mock(IGenreRepo.class);
        mapperDouble = mock(ItemResponseDTOMapper.class);

        itemService = new ItemService(
                itemRepoDouble,
                itemFactoryDouble,
                editionRepoDouble,
                publicationRepoDouble,
                authorRepoDouble,
                genreRepoDouble,
                mapperDouble
        );
    }

    // ----------------------------------------------------------------
    // registerItem
    // ----------------------------------------------------------------

    @Test
    void registerItemValidRequestReturnsItemResponseDTO() {
        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        Genre genreDouble = mock(Genre.class);
        Item itemDouble = mock(Item.class);
        Item savedItemDouble = mock(Item.class);
        ItemResponseDTO responseDTODouble = mock(ItemResponseDTO.class);

        when(editionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(genreRepoDouble.ofIdentity(any())).thenReturn(Optional.of(genreDouble));
        when(itemFactoryDouble.createItem(any(), any(), any(), any())).thenReturn(itemDouble);
        when(itemRepoDouble.save(itemDouble)).thenReturn(savedItemDouble);
        when(mapperDouble.toResponseDTO(any(), any(), any(), any(), any()))
                .thenReturn(responseDTODouble);

        // Act
        ItemResponseDTO result = itemService.registerItem(
                editionIdDouble,
                Condition.GOOD,
                new Description("Nice copy")
        );

        // Assert
        assertNotNull(result);
        assertEquals(responseDTODouble, result);
    }

    @Test
    void registerItemEditionNotFoundThrowsNoSuchElementException() {
        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        when(editionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(java.util.NoSuchElementException.class, () ->
                itemService.registerItem(
                        editionIdDouble,
                        Condition.GOOD,
                        new Description("Nice copy")
                )
        );
    }

    @Test
    void registerItemPublicationNotFoundThrowsNoSuchElementException() {
        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Edition editionDouble = mock(Edition.class);

        when(editionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(java.util.NoSuchElementException.class, () ->
                itemService.registerItem(
                        editionIdDouble,
                        Condition.GOOD,
                        new Description("Nice copy")
                )
        );
    }

    @Test
    void registerItemAuthorNotFoundThrowsNoSuchElementException() {
        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);

        when(editionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(java.util.NoSuchElementException.class, () ->
                itemService.registerItem(
                        editionIdDouble,
                        Condition.GOOD,
                        new Description("Nice copy")
                )
        );
    }

    @Test
    void registerItemGenreNotFoundThrowsNoSuchElementException() {
        // Arrange
        EditionId editionIdDouble = mock(EditionId.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);

        when(editionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(genreRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(java.util.NoSuchElementException.class, () ->
                itemService.registerItem(
                        editionIdDouble,
                        Condition.GOOD,
                        new Description("Nice copy")
                )
        );
    }

    // ----------------------------------------------------------------
    // getAllItems
    // ----------------------------------------------------------------

    @Test
    void getAllItemsReturnsListOfItemResponseDTOs() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        Genre genreDouble = mock(Genre.class);
        ItemResponseDTO responseDTODouble = mock(ItemResponseDTO.class);

        when(itemRepoDouble.findAll()).thenReturn(List.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(genreRepoDouble.ofIdentity(any())).thenReturn(Optional.of(genreDouble));
        when(mapperDouble.toResponseDTO(itemDouble, editionDouble, publicationDouble, authorDouble, genreDouble))
                .thenReturn(responseDTODouble);

        // Act
        List<ItemResponseDTO> result = itemService.getAllItems();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDTODouble, result.get(0));
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

    // ----------------------------------------------------------------
    // getItemById
    // ----------------------------------------------------------------

    @Test
    void getItemByIdValidIdReturnsItemResponseDTO() {
        // Arrange
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);
        Author authorDouble = mock(Author.class);
        Genre genreDouble = mock(Genre.class);
        ItemResponseDTO responseDTODouble = mock(ItemResponseDTO.class);

        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(editionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(publicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(authorRepoDouble.ofIdentity(any())).thenReturn(Optional.of(authorDouble));
        when(genreRepoDouble.ofIdentity(any())).thenReturn(Optional.of(genreDouble));
        when(mapperDouble.toResponseDTO(itemDouble, editionDouble, publicationDouble, authorDouble, genreDouble))
                .thenReturn(responseDTODouble);

        // Act
        ItemResponseDTO result = itemService.getItemById("3C5D126F8B");

        // Assert
        assertNotNull(result);
        assertEquals(responseDTODouble, result);
    }

    @Test
    void getItemByIdItemNotFoundThrowsNoSuchElementException() {
        // Arrange
        when(itemRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(java.util.NoSuchElementException.class, () ->
                itemService.getItemById("3C5D126F8B"));
    }
}
