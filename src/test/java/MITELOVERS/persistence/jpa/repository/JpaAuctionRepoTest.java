package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.valueobject.AuctionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.persistence.jpa.assembler.AuctionAssembler;
import MITELOVERS.persistence.jpa.datamodel.AuctionDataModel;
import MITELOVERS.persistence.springdata.IAuctionSpringDataRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaAuctionRepoTest {

    @Mock
    private IAuctionSpringDataRepo _springDataRepo;

    @Mock
    private AuctionAssembler _assemblerDouble;

    @InjectMocks
    private JpaAuctionRepo _jpaAuctionRepo;

    @Test
    void testSaveReturnAuction() {
        //Arrange
        Auction auctionDouble = mock(Auction.class);
        AuctionDataModel dmDouble = mock(AuctionDataModel.class);
        AuctionDataModel savedDouble = mock(AuctionDataModel.class);

        when(_assemblerDouble.toDataModel(auctionDouble)).thenReturn(dmDouble);
        when(_springDataRepo.save(dmDouble)).thenReturn(savedDouble);
        when(_assemblerDouble.toDomain(savedDouble)).thenReturn(auctionDouble);

        //Act
        Auction result = _jpaAuctionRepo.save(auctionDouble);

        //Assert
        assertEquals(auctionDouble, result);
        assertNotNull(result);
    }

    @Test
    void testFindAllReturnIterableOfAuctions() {

        AuctionDataModel dm1Double = mock(AuctionDataModel.class);
        AuctionDataModel dm2Double = mock(AuctionDataModel.class);
        List<AuctionDataModel> dmList = List.of(dm1Double, dm2Double);

        Auction auction1Double = mock(Auction.class);
        Auction auction2Double = mock(Auction.class);
        List<Auction> auctionList = List.of(auction1Double, auction2Double);

        when(_springDataRepo.findAll()).thenReturn(dmList);
        when(_assemblerDouble.toDomain(dm1Double)).thenReturn(auction1Double);
        when(_assemblerDouble.toDomain(dm2Double)).thenReturn(auction2Double);

        Iterable<Auction> result = _jpaAuctionRepo.findAll();

        assertEquals(auctionList, result);
        assertNotNull(result);
    }

    @Test
    void testFindAllKeysReturnIds() {

        AuctionDataModel dm1Double = mock(AuctionDataModel.class);
        AuctionDataModel dm2Double = mock(AuctionDataModel.class);
        List<AuctionDataModel> dmList = List.of(dm1Double, dm2Double);

        when(dm1Double.getAuctionId()).thenReturn("AU-1234ABCD");
        when(dm2Double.getAuctionId()).thenReturn("AU-5678EFGH");
        when(_springDataRepo.findAll()).thenReturn(dmList);

        List<AuctionId> result = _jpaAuctionRepo.findAllKeys();

        assertEquals(2, result.size());
        assertEquals("AU-1234ABCD", result.get(0).toString());
        assertEquals("AU-5678EFGH", result.get(1).toString());
        assertNotNull(result);
    }

    @Test
    void testOfIdentityReturnsAuction() {
        //Arrange
        AuctionId auctionIdDouble = mock(AuctionId.class);
        AuctionDataModel dmDouble = mock(AuctionDataModel.class);
        Auction auctionDouble = mock(Auction.class);

        when(auctionIdDouble.toString()).thenReturn("AU-1234ABCD");
        when(_springDataRepo.findById("AU-1234ABCD")).thenReturn(Optional.of(dmDouble));
        when(_assemblerDouble.toDomain(dmDouble)).thenReturn(auctionDouble);

        //Act
        Optional<Auction> result = _jpaAuctionRepo.ofIdentity(auctionIdDouble);

        //Assert
        assertTrue(result.isPresent());
        assertEquals(auctionDouble, result.get());
    }

    @Test
    void testContainsOfIdentityReturnsTrueWhenAuctionExists() {
        //Arrange
        AuctionId auctionIdDouble = mock(AuctionId.class);

        when(auctionIdDouble.toString()).thenReturn("AU-1234ABCD");
        when(_springDataRepo.existsById("AU-1234ABCD")).thenReturn(true);

        //Act
        boolean result = _jpaAuctionRepo.containsOfIdentity(auctionIdDouble);

        //Assert
        assertTrue(result);
    }

    @Test
    void findByItemsIdSortedShouldDelegateToSpringRepo() {

        ItemId item1 = mock(ItemId.class);
        ItemId item2 = mock(ItemId.class);

        when(item1.toString()).thenReturn("i1");
        when(item2.toString()).thenReturn("i2");

        AuctionDataModel dm = mock(AuctionDataModel.class);
        Auction auction = mock(Auction.class);

        when(_springDataRepo.findAllByItemsIdOrderByAuctionEndDateAsc(List.of("i1", "i2")))
                .thenReturn(List.of(dm));

        when(_assemblerDouble.toDomain(dm)).thenReturn(auction);
        when(auction.getItemsId()).thenReturn(List.of(item1, item2));

        List<ItemId> result = _jpaAuctionRepo.findByItemsIdSorted(List.of(item1, item2));

        assertEquals(List.of(item1, item2), result);
    }

    @Test
    void findByItemsIdSortedShouldReturnEmptyListWhenNoAuctionsMatch() {

        ItemId item1 = mock(ItemId.class);
        when(item1.toString()).thenReturn("i1");

        when(_springDataRepo.findAllByItemsIdOrderByAuctionEndDateAsc(List.of("i1")))
                .thenReturn(List.of());

        List<ItemId> result = _jpaAuctionRepo.findByItemsIdSorted(List.of(item1));

        assertTrue(result.isEmpty());
    }

}