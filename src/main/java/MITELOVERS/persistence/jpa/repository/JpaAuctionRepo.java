package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.repository.IAuctionRepo;
import MITELOVERS.domain.valueobject.AuctionId;
import MITELOVERS.persistence.jpa.assembler.AuctionAssembler;
import MITELOVERS.persistence.jpa.datamodel.AuctionDataModel;
import MITELOVERS.persistence.springdata.IAuctionSpringDataRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaAuctionRepo implements IAuctionRepo{

    @Autowired
    private IAuctionSpringDataRepo springDataRepo;

    @Autowired
    private AuctionAssembler assembler;

    @Override
    public Auction save(Auction auction){
        AuctionDataModel dm = assembler.toDataModel(auction);
        AuctionDataModel saved = springDataRepo.save(dm);
        return assembler.toDomain(saved);
    }

    @Override
    public Iterable<Auction> findAll(){
        List<AuctionDataModel> list = springDataRepo.findAll();
        List<Auction> listDomain = new ArrayList<>();
        for(AuctionDataModel dm : list){
            listDomain.add(assembler.toDomain(dm));
        }
        return listDomain;
    }

    @Override
    public List<AuctionId> findAllKeys() {
        List<AuctionDataModel> list = springDataRepo.findAll();
        List<AuctionId> listDomain = new ArrayList<>();

        for(AuctionDataModel dm : list){
            listDomain.add(new AuctionId(dm.getAuctionId()));
        }
        return listDomain;
    }

    @Override
    public Optional<Auction> ofIdentity(AuctionId auctionId) {
        return springDataRepo.findById(auctionId.toString())
                .map(assembler::toDomain);
    }

    @Override
    public boolean containsOfIdentity(AuctionId auctionId) {
        return springDataRepo.existsById(auctionId.toString());
    }



}
