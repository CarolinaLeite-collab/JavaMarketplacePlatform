package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Generated
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sale_lines")
public class SaleLineDataModel {

    @Id
    @Column(name = "sale_line_id")
    private String saleLineId;

    @Column(name = "seller_id", nullable = false)
    private String sellerId;

    @Column(name = "direct_sale_id", nullable = false)
    private String directSaleId;

    @Embedded
    private PriceDataModel price;
}