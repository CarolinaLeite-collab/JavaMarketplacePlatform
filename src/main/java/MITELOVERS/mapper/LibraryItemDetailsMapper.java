package MITELOVERS.mapper;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.dto.response.LibraryItemDetailsDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper responsible for converting domain objects into {@link LibraryItemDetailsDTO}.
 *
 * <p>
 * This class transforms core domain entities such as {@link Edition},
 * {@link Publication}, {@link PublicationType}, and {@link Author}
 * into a simplified DTO used for presentation or API responses.
 * </p>
 *
 * <p>
 * It also handles extraction of a human-readable identifier from the
 * edition, supporting both {@link MITELOVERS.domain.valueobject.ISBN}
 * and {@link MITELOVERS.domain.valueobject.ISSN}. If no valid identifier
 * is available, a default value is returned.
 * </p>
 */

@Component
public class LibraryItemDetailsMapper {

    public LibraryItemDetailsDTO toDTO(
            Edition edition,
            Author author,
            PublicationType publicationType) {

        return new LibraryItemDetailsDTO(
                author.getName().toString(),
                publicationType.toString(),
                edition.getIdentifier().toString()

        );
    }
}
