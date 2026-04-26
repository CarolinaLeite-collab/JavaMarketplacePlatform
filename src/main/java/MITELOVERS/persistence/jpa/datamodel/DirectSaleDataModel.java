package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data model object representing {@link MITELOVERS.domain.directsale.DirectSale} information, allowing its persistence in a database.
 */

@Getter
@NoArgsConstructor
@Entity
@Table(name="DirectSales")
public class DirectSaleDataModel {
    @Id
    private String directSaleId;
    @ElementCollection
    private List<String> itemsId;

    @Embedded
    private PriceDataModel price;
    private String timeLimit;

    public DirectSaleDataModel (String directSaleId,
                                List<String> itemsId,
                                PriceDataModel price,
                                String timeLimit){

        this.directSaleId = directSaleId;
        this.itemsId = itemsId;
        this.price = price;
        this.timeLimit = timeLimit;

    }

}
