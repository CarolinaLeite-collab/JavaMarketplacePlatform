package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "bids")
public class BidDataModel {

    @Column(name = "userId", nullable = false)
    private String userId;

    @Convert(converter = PriceConverter.class)
    @Column(name = "offerPrice")
    private PriceDataModel offerPrice;

    @Column(name = "bidDate",  nullable = false)
    private String bidDate;

    @Id
    @Column(name = "id",  nullable = false,  unique = true)
    private String bidId;

}
