package MITELOVERS.mapper;

import MITELOVERS.controllers.rest.ItemRestController;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.dto.response.ItemResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ItemResponseDTOMapper implements RepresentationModelAssembler<Item, ItemResponseDTO> {

    private final IEditionRepo _editionRepo;
    private final IPublicationRepo _publicationRepo;
    private final IAuthorRepo _authorRepo;
    private final IGenreRepo _genreRepo;

    public ItemResponseDTOMapper(IEditionRepo editionRepo,
                                 IPublicationRepo publicationRepo,
                                 IAuthorRepo authorRepo,
                                 IGenreRepo genreRepo) {
        _editionRepo     = Objects.requireNonNull(editionRepo,     "EditionRepo is required");
        _publicationRepo = Objects.requireNonNull(publicationRepo, "PublicationRepo is required");
        _authorRepo      = Objects.requireNonNull(authorRepo,      "AuthorRepo is required");
        _genreRepo       = Objects.requireNonNull(genreRepo,       "GenreRepo is required");
    }

    @Override
    public ItemResponseDTO toModel(Item item) {

        Edition edition = _editionRepo.ofIdentity(item.getEditionId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Edition does not exist in the repository"));

        Publication publication = _publicationRepo.ofIdentity(edition.getPublicationId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Publication does not exist in the repository"));

        Author author = _authorRepo.ofIdentity(publication.getAuthorId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Author does not exist in the repository"));

        Genre genre = _genreRepo.ofIdentity(publication.getGenreId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Genre does not exist in the repository"));

        ItemResponseDTO dto = new ItemResponseDTO(
                item.identity().toString(),
                item.getCondition().toString(),
                item.getDescription().toString(),
                item.getSaleStatus().toString(),
                edition.getEditionId().toString(),
                edition.getIdentifier().toString(),
                edition.getEditionLanguage().toString(),
                edition.getPublishingYear().getValue(),
                edition.getPublicationTypeId().toString(),
                publication.getTitle().toString(),
                author.getName().toString(),
                publication.getReleaseYear().getValue(),
                genre.getGenre().toString()
        );

        dto.add(linkTo(methodOn(ItemRestController.class)
                .getItemById(item.identity().toString()))
                .withSelfRel());

        return dto;
    }
}