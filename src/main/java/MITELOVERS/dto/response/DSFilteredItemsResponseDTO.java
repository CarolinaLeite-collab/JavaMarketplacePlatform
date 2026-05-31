package MITELOVERS.dto.response;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

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
