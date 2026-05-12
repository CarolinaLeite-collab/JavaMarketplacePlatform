package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Data model object representing {@link MITELOVERS.domain.directsale.DirectSale} information, allowing its persistence in a database.
 */

@Generated
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
    private Long timeLimit;
    private Instant creationDate;
}
