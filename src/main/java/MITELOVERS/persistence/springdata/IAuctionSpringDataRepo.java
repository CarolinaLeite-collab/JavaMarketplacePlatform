package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.AuctionDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAuctionSpringDataRepo extends JpaRepository<AuctionDataModel, String> {

    List<AuctionDataModel> findAllByItemsIdOrderByAuctionEndDateAsc(List<String> itemsId);

}
