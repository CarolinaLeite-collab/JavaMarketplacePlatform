package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "auctions")
public class AuctionDataModel {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String auctionId;

    @ElementCollection
    @CollectionTable(name = "auction_items", joinColumns = @JoinColumn(name = "auction_id"))
    @Column(name = "items_id", nullable = false)
    private List<String> itemsId;

    @Convert(converter = PriceConverter.class)
    @Column(name = "starting_price", nullable = false)
    private PriceDataModel startingPrice;

    @Convert(converter = PriceConverter.class)
    @Column(name = "reserve_price", nullable = false)
    private PriceDataModel reservePrice;

    @Convert(converter = PriceConverter.class)
    @Column(name = "outright_price", nullable = true)
    private PriceDataModel outrightPrice;

    @Column(name = "start_date", nullable = false)
    private Instant auctionStartDate;

    @Column(name = "end_date", nullable = false)
    private Instant auctionEndDate;

    @Column(name = "user_id", nullable = true)
    private String userId;

    @Convert(converter = PriceConverter.class)
    @Column(name = "final_price")
    private PriceDataModel finalPrice;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "auction_id")
    private List<BidDataModel> bids;

    @Column(name="seller", nullable = false)
    private String seller;
}