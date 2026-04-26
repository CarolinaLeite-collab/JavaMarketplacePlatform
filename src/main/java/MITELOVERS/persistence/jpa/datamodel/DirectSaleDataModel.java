package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String itemsId;

    @Embedded
    private PriceDataModel price;
    private String timeLimit;

    public DirectSaleDataModel (String directSaleId,
                                String itemsId,
                                PriceDataModel price,
                                String timeLimit){

        this.directSaleId = directSaleId;
        this.itemsId = itemsId;
        this.price = price;
        this.timeLimit = timeLimit;

    }

}
