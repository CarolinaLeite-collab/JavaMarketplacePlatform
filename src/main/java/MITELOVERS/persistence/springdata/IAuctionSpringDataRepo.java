package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.AuctionDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuctionSpringDataRepo extends JpaRepository<AuctionDataModel, String> {
}
