package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data model object representing {@link MITELOVERS.domain.directsale.DirectSale} information, allowing its persistence in a database.
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="DirectSales")
public class DirectSaleDataModel {

    @Id
    private String directSaleId;

    private List<String> itemsId;

    @Embedded
    private PriceDataModel price;
    private String timeLimit;
}
