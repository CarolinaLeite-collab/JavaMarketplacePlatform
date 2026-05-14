package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.auction.Bid;
import MITELOVERS.domain.auction.BidFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.BidDataModel;
import MITELOVERS.persistence.jpa.datamodel.PriceDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@AllArgsConstructor
public class BidAssembler {

    private BidFactory _bidFactory;

    public BidDataModel toDataModel(Bid bid) {

        PriceDataModel priceDataModel = new PriceDataModel(
                bid.getOfferPrice().getValue(),
                bid.getOfferPrice().getCurrency().name());

        return new BidDataModel(bid.getUserId().toString(),
                priceDataModel,
                bid.getBidDate().toString(),
                bid.identity().toString());
    }

    public Bid toDomain(BidDataModel bidDataModel) {

        Email email = new Email(bidDataModel.getUserId());

        UserId userId = new UserId(email);

        Price price = new Price(
                bidDataModel.getOfferPrice().getNumericValue(),
                Currency.valueOf(bidDataModel.getOfferPrice().getCurrency()));

        Instant bidDate = Instant.parse(bidDataModel.getBidDate());

        BidId bidId = new BidId(bidDataModel.getBidId());

        return _bidFactory.createBid(userId, price, bidDate, bidId);
    }


}
