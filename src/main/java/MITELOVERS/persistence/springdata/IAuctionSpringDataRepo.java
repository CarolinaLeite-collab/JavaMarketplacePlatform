package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.AuctionDataModel;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("jpa")
public interface IAuctionSpringDataRepo extends JpaRepository<AuctionDataModel, String> {

    List<AuctionDataModel> findAllByItemsIdOrderByAuctionEndDateAsc(List<String> itemsId);

}
