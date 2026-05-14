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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "direct_sale_items",
            joinColumns = @JoinColumn(name = "direct_sale_id")
    )
    @Column(name = "item_id")
    private List<String> itemsId;

    //@Convert(converter=PriceConverter.class)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "numericValue", column = @Column(name = "numeric_value")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency"))
    })
    private PriceDataModel price;
    private Long timeLimit;
    private Instant creationDate;
}