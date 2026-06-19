package MITELOVERS.mapper;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.dto.response.LibraryItemSummaryDTO;
import org.springframework.stereotype.Component;

@Component
public class LibraryItemSummaryMapper {

    public LibraryItemSummaryDTO toDTO(Item item, Publication publication) {
        return new LibraryItemSummaryDTO(
                item.identity().toString(),
                publication.getTitle().toString(),
                getPictureUrl(item)
        );
    }

    public LibraryItemSummaryDTO toDTO(
            Item item,
            Publication publication,
            Edition edition,
            Author author,
            PublicationType publicationType) {

        return new LibraryItemSummaryDTO(
                item.identity().toString(),
                publication.getTitle().toString(),
                author.getName().toString(),
                publicationType.toString(),
                edition.getIdentifier().toString(),
                getPictureUrl(item)
        );
    }

    private String getPictureUrl(Item item) {
        if (item.getPicture() != null) {
            return item.getPicture().toString();
        }

        return null;
    }
}
