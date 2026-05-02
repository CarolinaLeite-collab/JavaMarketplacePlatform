package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "direct_sale_items",
            joinColumns = @JoinColumn(name = "direct_sale_id")
    )
    @Column(name = "item_id")
    private List<String> itemsId;

    @Convert(converter=PriceConverter.class)
    @Column(name="price")
    private PriceDataModel price;
    private Long timeLimit;
    private Instant creationDate;
}
