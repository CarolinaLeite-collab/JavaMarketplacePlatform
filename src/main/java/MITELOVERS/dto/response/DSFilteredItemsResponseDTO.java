package MITELOVERS.dto.response;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/**
 * DTO representing a filtered list of Direct Sales, typically used in
 * controller responses where only the identifiers of matching direct sales
 * need to be returned.
 *
 * <p>This DTO wraps a collection of {@link DirectSaleEntry} elements and
 * extends {@link RepresentationModel} to support HATEOAS link enrichment
 * at the controller layer.</p>
 */

@Getter
public class DSFilteredItemsResponseDTO extends RepresentationModel<DSFilteredItemsResponseDTO> {

    private final List<DirectSaleEntry> directSales;

    public DSFilteredItemsResponseDTO(List<DirectSaleEntry> directSales) {
        this.directSales = directSales;
    }

    @Getter
    public static class DirectSaleEntry extends RepresentationModel<DirectSaleEntry> {

        private final String directSaleId;

        public DirectSaleEntry(String directSaleId) {
            this.directSaleId = directSaleId;
        }
    }

}
