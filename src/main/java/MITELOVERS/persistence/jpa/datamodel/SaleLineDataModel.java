package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.sale.SaleLine;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data model object representing {@link SaleLine} information,
 * allowing its persistence in a database.
 */

@Generated
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SaleLines")
public class SaleLineDataModel {

    @Id
    @Column(name = "sale_line_id")
    private String saleLineId;

    @Column(name = "seller_id", nullable = false)
    private String sellerId;

    @Column(name = "direct_sale_id", nullable = false)
    private String directSaleId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "numericValue", column = @Column(name = "price_value", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency", nullable = false))
    })
    private PriceDataModel price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private SaleDataModel sale;
}