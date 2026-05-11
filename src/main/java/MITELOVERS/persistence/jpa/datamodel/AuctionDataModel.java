package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "auctions")
public class AuctionDataModel {

    @Id
    @Column(name = "id", nullable = false,  unique = true)
    private String auctionId;

    @ElementCollection
    @CollectionTable(name = "auctionItems", joinColumns = @JoinColumn(name = "auctionId"))
    @Column(name = "itemsId",  nullable = false)
    private List<String> itemsId;

    @Convert(converter = PriceConverter.class)
    @Column(name = "startingPrice", nullable = false)
    private PriceDataModel startingPrice;

    @Convert(converter = PriceConverter.class)
    @Column(name = "reservePrice", nullable = false)
    private PriceDataModel reservePrice;

    @Convert(converter = PriceConverter.class)
    @Column(name = "outrightPrice",  nullable = false)
    private PriceDataModel outrightPrice;

    @Column(name = "startDate", nullable = false)
    private Instant auctionStartDate;

    @Column(name = "endDate", nullable = false)
    private Instant auctionEndDate;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Convert(converter = PriceConverter.class)
    @Column(name = "finalPrice")
    private PriceDataModel finalPrice;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "auctionId")
    private List<BidDataModel> bids;

}
