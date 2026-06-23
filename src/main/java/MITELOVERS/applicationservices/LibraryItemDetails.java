package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;


/**
 * Groups the domain objects needed to fully describe a single library item.
 *
 * <p>
 * Carries the result of resolving an {@code Item} through its
 * {@code Edition}, {@code Publication}, {@code Author} and
 * {@code PublicationType} so that {@link LibraryService} can return pure
 * domain data, leaving DTO mapping to the controller layer.
 * </p>
 */

public record LibraryItemDetails(
        Item item,
        Publication publication,
        Edition edition,
        Author author,
        PublicationType publicationType) {}
