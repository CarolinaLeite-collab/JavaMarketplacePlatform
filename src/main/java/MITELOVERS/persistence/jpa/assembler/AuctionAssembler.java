package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.auction.AuctionFactory;
import MITELOVERS.domain.auction.Bid;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.AuctionDataModel;
import MITELOVERS.persistence.jpa.datamodel.BidDataModel;
import MITELOVERS.persistence.jpa.datamodel.PriceDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class AuctionAssembler {

    private final BidAssembler bidAssembler;
    private final AuctionFactory auctionFactory;

    public AuctionDataModel toDataModel(Auction auction) {

        PriceDataModel startingPrice = new PriceDataModel(
                auction.getStartingPrice().getValue(),
                auction.getStartingPrice().getCurrency().toString());

        PriceDataModel reservePrice = new PriceDataModel(
                auction.getReservePrice().getValue(),
                auction.getReservePrice().getCurrency().toString());

        PriceDataModel outrightPrice = null;
        if (auction.getOutrightPrice() != null) {
            outrightPrice = new PriceDataModel(
                    auction.getOutrightPrice().getValue(),
                    auction.getOutrightPrice().getCurrency().toString());
        }

        PriceDataModel finalPrice = null;
        if (auction.getFinalPrice() != null) {
            finalPrice = new PriceDataModel(
                    auction.getFinalPrice().getValue(),
                    auction.getFinalPrice().getCurrency().toString());
        }

        List<BidDataModel> bids = new ArrayList<>();
        for (Bid bid : auction.getBids()) {
            bids.add(bidAssembler.toDataModel(bid));
        }

        List<String> itemsIds = new ArrayList<>();
        for (ItemId itemId : auction.getItemsId()) {
            itemsIds.add(itemId.toString());
        }

        String userId = null;
        if (auction.getUserId() != null) {
            userId = auction.getUserId().toString();
        }

        String seller = null;
        if(auction.getSeller() != null) {
            seller = auction.getSeller().toString();
        }

        return new AuctionDataModel(
                auction.identity().toString(),
                itemsIds,
                startingPrice,
                reservePrice,
                outrightPrice,
                auction.getAuctionStartDate(),
                auction.getAuctionEndDate(),
                userId,
                finalPrice,
                bids,
                seller);
    }

    public Auction toDomain(AuctionDataModel auctionDM) {
        AuctionId auctionId = new AuctionId(auctionDM.getAuctionId());

        List<ItemId> itemsId = new ArrayList<>();
        for (String itemIdStr : auctionDM.getItemsId()) {
            itemsId.add(new ItemId(itemIdStr));
        }

        Price startingPrice = new Price(
                auctionDM.getStartingPrice().getNumericValue(),
                Currency.valueOf(auctionDM.getStartingPrice().getCurrency()));

        Price reservePrice = new Price(
                auctionDM.getReservePrice().getNumericValue(),
                Currency.valueOf(auctionDM.getReservePrice().getCurrency()));

        Price outrightPrice = null;
        if (auctionDM.getOutrightPrice() != null) {
            outrightPrice = new Price(
                    auctionDM.getOutrightPrice().getNumericValue(),
                    Currency.valueOf(auctionDM.getOutrightPrice().getCurrency()));
        }

        ZonedDateTime auctionStartDate =
                auctionDM.getAuctionStartDate().atZone(ZoneId.systemDefault());

        ZonedDateTime auctionEndDate =
                auctionDM.getAuctionEndDate().atZone(ZoneId.systemDefault());

        UserId userId = null;
        if (auctionDM.getUserId() != null) {
            Email email = new Email(auctionDM.getUserId());
            userId = new UserId(email);
        }

        UserId seller = null;
        if (auctionDM.getSeller() != null) {
            seller = new UserId(new Email(auctionDM.getSeller()));
        }

        Price finalPrice = null;
        if (auctionDM.getFinalPrice() != null) {
            finalPrice = new Price(
                    auctionDM.getFinalPrice().getNumericValue(),
                    Currency.valueOf(auctionDM.getFinalPrice().getCurrency()));
        }

        List<Bid> bids = new ArrayList<>();
        for (BidDataModel bidIdStr : auctionDM.getBids()) {
            bids.add(bidAssembler.toDomain(bidIdStr));
        }

        return auctionFactory.createAuction(
                auctionId,
                itemsId,
                startingPrice,
                reservePrice,
                outrightPrice,
                auctionStartDate,
                auctionEndDate,
                userId,
                seller,
                finalPrice,
                bids);
    }
}